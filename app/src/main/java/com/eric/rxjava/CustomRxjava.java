package com.eric.rxjava;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;

class CustomRxjava {

    //执行结果
//            2021-06-24 21:28:37.854 31276-31276/com.eric.rxjava V/CustomRxjava: [subscribe]
//            2021-06-24 21:28:37.854 31276-31276/com.eric.rxjava V/CustomRxjava: [onNext],hello :0
//            2021-06-24 21:28:37.854 31276-31276/com.eric.rxjava V/CustomRxjava: [onNext],hello :1
//            2021-06-24 21:28:37.854 31276-31276/com.eric.rxjava V/CustomRxjava: [onNext],hello :2
//            2021-06-24 21:28:37.854 31276-31276/com.eric.rxjava V/CustomRxjava: [onNext],hello :3
//            2021-06-24 21:28:37.854 31276-31276/com.eric.rxjava V/CustomRxjava: [onNext],hello :4
//            2021-06-24 21:28:37.854 31276-31276/com.eric.rxjava V/CustomRxjava: [onNext],hello :5
//            2021-06-24 21:28:37.854 31276-31276/com.eric.rxjava V/CustomRxjava: [onNext],hello :6
//            2021-06-24 21:28:37.854 31276-31276/com.eric.rxjava V/CustomRxjava: [onNext],hello :7
//            2021-06-24 21:28:37.854 31276-31276/com.eric.rxjava V/CustomRxjava: [onNext],hello :8
//            2021-06-24 21:28:37.854 31276-31276/com.eric.rxjava V/CustomRxjava: [onNext],hello :9
//            2021-06-24 21:28:37.854 31276-31276/com.eric.rxjava V/CustomRxjava: [onComplete]


    private static final String TAG = "CustomRxjava";
    public static final String FLATMAP_TAG = "CustomFlatMap";

    public void testRxjava() {
        Observable.create(new Observable<String>() {
            @Override
            public void subscribe(Observer<String> observer) {
                Log.v(TAG, "[subscribe]");
                for (int i = 0; i < 10; i++) {
                    observer.onNext("hello :" + i);
                }
                observer.onComplete("观察者执行结束啦");
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onNext(String o) {
                Log.v(TAG, "[onNext]," + o.toString());


            }

            @Override
            public void onComplete(String o) {
                Log.v(TAG, "[onComplete]");

            }

            @Override
            public void onError(String o) {
                Log.v(TAG, "[onError]," + o.toString());

            }
        });
    }


//    2021-06-25 12:04:49.616 26218-26218/com.eric.rxjava V/CustomFlatMap: this:com.eric.rxjava.Observable$1@2aa984d
//2021-06-25 12:04:49.616 26218-26218/com.eric.rxjava V/CustomFlatMap: Observable this:com.eric.rxjava.CustomRxjava$4@5987f02
//            2021-06-25 12:04:49.616 26218-26218/com.eric.rxjava V/CustomFlatMap: observerB 的subscribe
//2021-06-25 12:04:49.616 26218-26218/com.eric.rxjava V/CustomFlatMap: observerB 的onNext
//2021-06-25 12:04:49.616 26218-26218/com.eric.rxjava V/CustomFlatMap: observerC 的onNext

    public void testFlatMap() {
        Observable.create(new Observable<String>() {
            @Override
            public void subscribe(Observer<String> observer) {
                Log.v(FLATMAP_TAG, "observerB 的subscribe");
                observer.onNext("第一个subscribe的observer的onNext");
//                observer.onComplete("第一个subscribe的observer的onComplete");
            }
        })
                .flatMap()
                .subscribe(new Observer<String>() {

                    @Override
                    public void onNext(String o) {
                        Log.v(FLATMAP_TAG, "observerC 的onNext");


                    }

                    @Override
                    public void onComplete(String o) {
                        Log.v(FLATMAP_TAG, "observerC 的onComplete");

                    }

                    @Override
                    public void onError(String o) {
                        Log.v(FLATMAP_TAG, "observerC 的onError");


                    }
                });

    }

    public void testSubscribeOn() {
        Observable.create(new Observable() {
            @Override
            public void subscribe(Observer observer) {
                Log.v(FLATMAP_TAG, "[subscribeOn] subscribe,thread:" + Thread.currentThread().getName());
                observer.onNext("测试testSubscribeOn操作符");

            }
        }).subscribeOn()
                .observerOn()
                .subscribe(new Observer() {
                    @Override
                    public void onNext(Object o) {
                        Log.v(FLATMAP_TAG, "最后的observer的onNext方法,thread:" + Thread.currentThread().getName());

                    }

                    @Override
                    public void onComplete(Object o) {

                    }

                    @Override
                    public void onError(Object o) {

                    }
                });
    }


    /**
     * 自定义实现变换操作符 map操作符
     * <p>
     * 结合上面实现的flatmap的上下调度 再自定义一些Function<R,T>操作接口方法，那么就能够实现
     * 在一段流程中，插入一些自定义的函数，达到需求
     * <p>
     * 场景：从一个网络url，下载转换为bitmap对象，在进行存储到硬盘上
     * <p>
     * 好处：可以随意组合操作符，经过自己的function的实现，那么就完成了需求。代码的可读性很好
     */


    public void testMap() {
        Observable.create(new Observable<String>() {
            @Override
            public void subscribe(Observer<String> observer) {
                Log.v(FLATMAP_TAG, "observerB 的subscribe");
                observer.onNext("http://baidu.com/demo.png");
//                observer.onComplete("第一个subscribe的observer的onComplete");
            }
        }).map(new Function<Bitmap, String>() {
            @Override
            public Bitmap apply(String s) {
                return downloadBitmap();
            }
        }).map(new Function<File, Bitmap>() {
            @Override
            public File apply(Bitmap bitmap) {
                return new File("本地文件");
            }
        }).subscribe(new Observer<File>() {
            @Override
            public void onNext(File file) {
                Log.v(FLATMAP_TAG, "onNext file: " + file.toString());
            }

            @Override
            public void onComplete(File file) {
                Log.v(FLATMAP_TAG, "onComplete bitmap:" + file.toString());

            }

            @Override
            public void onError(File file) {
                Log.v(FLATMAP_TAG, "onError bitmap:" + file.toString());

            }
        });
    }

    private Bitmap downloadBitmap() {
        return Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
    }

}
