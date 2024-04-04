package com.eric.operatprs;

import android.util.Log;

import com.eric.operatprs.bean.Student;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * @Author: chen
 * @datetime: 2021/10/18
 * @desc:
 */
public class JustOperator {

    private static final String TAG = "JustOperator";

    public void flowable() {
        Flowable.just("Hello world", "nice to meet you").subscribe(
                s -> {
                    Log.v(TAG, s);
                }
        );
    }

    public void just() {
        Observable.just(1, 2, 3).subscribe(
                x -> {
                    Log.v(TAG, "just: " + x);
                }

        );
    }

    public void zipWith() {
        Disposable d = Observable.range(0, 30)
                .doOnNext(integer -> {
                    Log.i(TAG, "zipWidth" + integer);
                })
                .interval(100, TimeUnit.MILLISECONDS)
                .zipWith(Observable.range(10, 30), (i1, i2) -> i1 + "-" + i2)
                .take(30)
                .subscribe();
    }

    public void interval() {
        Disposable disposable = null;
        disposable = Observable.range(0, 30)
                //定时器
                .interval(100, TimeUnit.MILLISECONDS)
                .take(10)//限制次数
                .doOnComplete(() -> {
                    Log.i(TAG, "interval onComplete");
                })
                .doOnLifecycle(disposable1 -> {
                    Log.i(TAG, "interval onSubscribe");
                }, () -> {
                    Log.i(TAG, "interval onDisposable");

                })
                .subscribe(i -> {
                            Log.i(TAG, "interval: " + i);
                        }

                );
        //解除订阅关系
//        disposable.dispose();
    }

    // 带迭代器，针对list等实现了迭代器接口的集合类使用 还有fromArray操作符
    public void fromIterable() {
        List list = new ArrayList();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");

        Observable.fromIterable(list)
                .map(o -> {
                    Log.i(TAG, "map thread: " + Thread.currentThread().getName());
                    return o + " join map";
                })
                //被观察者 在io线程中进行操作
                .subscribeOn(Schedulers.io())
                //观察者 在主线程中进行操作
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    Log.i(TAG, "onNext thread:" + Thread.currentThread().getName() + ", s: " + s);
                });
    }


    /**
     * 变化操作符
     * <p>
     * map
     * <p>
     * flatMap
     * <p>
     * conactMap
     */

    public static final String json = "{\"uid\":\"00001\",\"userName\":\"Freeman\",\"telNumber\":\"13000000000\"}";

    // map操作符的作用，作用是将一个对象 转换为 另一个对象
    //适用场景： 比如从一个接口请求回来json数据，需要经过fastjson处理，转换为javabean，等等类似的变换操作
    public void testMap() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                emitter.onNext(json);
            }
        }).map((Function<String, Person>) s -> {
            //将json字符串，通过gson转换工具，进行转换javabean对象
            Gson gson = new Gson();
            Person person = gson.fromJson(s, Person.class);
            return person;
        }).subscribe(new Observer<Person>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Person person) {
                Log.i(TAG, "经过map转换后的person firstname: " + person);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    /**
     * flatmap和map的区别是，
     * map操作符返回的是一个"结果集"
     * flatmap操作符返回的是包含结果集的Observable(返回结果不同)
     * <p>
     * 转换对象的能力不同：
     * map只能一对一转换
     * flatmap能够进行一对一转换，也能一对多，多对多进行转换，一个对象可以转换为另一个对象。还可以转换成
     * 对象数组，再使用from,just系列操作符进行再次转换，剔除嵌套结构
     * <p>
     * <p>
     * map适用于一对一转换，当然也可以配合flatmap进行使用
     * <p>
     * flatmap适用于一对多，多对多的场景
     */

    public void testFlatMap() {
        Student student1 = new Student();
        List courselist = new ArrayList();
        courselist.add("语文");
        courselist.add("英语");
        courselist.add("地理");
        courselist.add("文学");
        student1.courses = courselist;

        Student student2 = new Student();
        List courselis2 = new ArrayList();
        courselist.add("化学");
        courselist.add("数学");
        courselist.add("生物");
        courselist.add("物理");
        student1.courses = courselist;

        //因为每个学生有多门课程，那么这是一个二层的数据结构
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);

        Observable.fromIterable(students)
                .flatMap(new Function<Student, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Student student) throws Throwable {
                        //这里面返回的再次使用fromIterable操作符，就解开了这种两层嵌套的结构
                        return Observable.fromIterable(student.courses);
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Log.i(TAG, "课程:" + s);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, e.toString());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //一个打印出所有学生的所有课程的场景 （每个学生下面有多门课程这种场景）
    //https://www.jianshu.com/p/c820afafd94b
    //使用map的话，需要先得到每个学生的所有课程的List，然后再遍历list，获取到每个学生的课程

    //使用flatmap的话，那么可以得到学生的所有课程的List组成的Observable对象，然后再继续订阅，进行转换，
    //相当于多层map的转换嵌套。


    /**
     * 合并操作符
     */
    public void testMerge() {
        Observable<Integer> observable1 = Observable.just(1, 2, 3);
        Observable<String> observable2 = Observable.just("a", "b", "c");

        Observable.merge(observable1, observable2).subscribe((Consumer<Serializable>) serializable -> {

            Log.i(TAG, "merge :" + serializable);

        });
    }

    public void testZip() {
        Observable<String> observable1 = Observable.just("1", "2", "3");
        Observable<String> observable2 = Observable.just("a", "b", "c");

        Observable.zip(observable1, observable2, new BiFunction<String, String, Object>() {
            @Override
            public Object apply(String s, String s2) throws Throwable {
                //合并规则
                return s + s2;
            }

            ;
        }).subscribe(o -> {
            Log.v(TAG, "zip:" + o);
        });
    }

    //reduce操作符 将每次的结果集 按照指定的规则 再次进行累计操作
    public void testReduce() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8).reduce(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Throwable {
                //自定义规则
                return integer + integer2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Log.v(TAG, "reduce:" + integer);
            }
        });
    }

    /**
     * 异步操作
     */

    public void testAsync() {

        Observable<String> ov1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                Thread.sleep(1000 * 3);
                emitter.onNext("create1");
                Log.v(TAG, Thread.currentThread().getName() + " 我等待了3s之后，才打印的");
            }
        }).subscribeOn(Schedulers.newThread());

        Observable<String> ov2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                Thread.sleep(1000);
                emitter.onNext("create2");
                Log.v(TAG, Thread.currentThread().getName() + " 我等待了1s之后，才打印的");
            }
        })
                .subscribeOn(Schedulers.newThread());

        //执行时间长的那个执行完了就完了
        Observable.concat(ov1, ov2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        Log.v(TAG, "都执行结束了");
                    }
                });

        // 顺序执行两个被观察者
        Observable.merge(ov1, ov2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        Log.v(TAG, "都执行结束了");
                    }
                });


    }


}