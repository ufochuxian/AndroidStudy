package com.eric.rxjava;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;


abstract class Observable<T> {

    private static final String TAG = "Observable";

    //1. 创建被观察者
    public static Observable create(Observable observable) {
        return observable;
    }

    //2. 订阅方法
    public abstract void subscribe(Observer<T> observer);

    // 自定义flatmap操作符
    public Observable<T> flatMap() {
        return new Observable<T>() {
            @Override
            public void subscribe(final Observer<T> observerC) {
                Observer<T> observerB = new Observer<T>() {

                    @Override
                    public void onNext(T t) {
                        Log.v(CustomRxjava.FLATMAP_TAG, "observerB 的onNext");
                        observerC.onNext(t);
                    }

                    @Override
                    public void onComplete(T t) {
                        Log.v(CustomRxjava.FLATMAP_TAG, "observerB的 onComplete");
                        observerC.onComplete(t);
                    }

                    @Override
                    public void onError(T t) {
                        Log.v(CustomRxjava.FLATMAP_TAG, "observerB 的onError");
                        observerC.onError(t);
                    }
                };
                Log.v(CustomRxjava.FLATMAP_TAG, "this:" + this);
                Log.v(CustomRxjava.FLATMAP_TAG, "Observable this:" + Observable.this);
                Observable.this.subscribe(observerB);
            }
        };
    }

    // 自定义map操作符
    public <R> Observable<R> map(final Function<R, T> function) {
        return new Observable<R>() {
            @Override
            public void subscribe(final Observer<R> observerC) {
                Observer<T> observerB = new Observer<T>() {
                    @Override
                    public void onNext(T t) {
                        //重点：经过自定义的函数将输入参数，转换为另外的形式
                        R apply = function.apply(t);
                        Log.v(CustomRxjava.FLATMAP_TAG, "observerB 的onNext,转换结果 " + apply.toString());
                        observerC.onNext(apply);
                    }

                    @Override
                    public void onComplete(T t) {
                        Log.v(CustomRxjava.FLATMAP_TAG, "observerB的 onComplete");
                        R apply = function.apply(t);
                        observerC.onComplete(apply);
                    }

                    @Override
                    public void onError(T t) {
                        Log.v(CustomRxjava.FLATMAP_TAG, "observerB 的onError");
                        R apply = function.apply(t);
                        observerC.onError(apply);
                    }
                };
                Log.v(CustomRxjava.FLATMAP_TAG, "this:" + this);
                Log.v(CustomRxjava.FLATMAP_TAG, "Observable this:" + Observable.this);
                Observable.this.subscribe(observerB);
            }
        };
    }

    //自定义subscribeOn操作符
    public Observable<T> subscribeOn() {
        return new Observable<T>() {
            @Override
            public void subscribe(final Observer<T> observerC) {

                //切换到子线程
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Observer<T> observerB = new Observer<T>() {
                            @Override
                            public void onNext(T t) {
                                Log.v(CustomRxjava.FLATMAP_TAG, "subscribeOn操作符 onNext,thread:" + Thread.currentThread().getName());
                                observerC.onNext(t);
                            }

                            @Override
                            public void onComplete(T t) {
                                observerC.onComplete(t);

                            }

                            @Override
                            public void onError(T t) {
                                observerC.onError(t);

                            }
                        };
                        Observable.this.subscribe(observerB);
                    }
                }).start();
            }
        };
    }

    Handler mHandler = new Handler(Looper.getMainLooper());

    public Observable<T> observerOn() {
        return new Observable<T>() {
            @Override
            public void subscribe(final Observer<T> observerC) {
                Observer<T> observerB = new Observer<T>() {
                    @Override
                    public void onNext(final T t) {
                        Log.v(CustomRxjava.FLATMAP_TAG, "[observerB] onNext方法,thread:" + Thread.currentThread().getName());
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                //这里要填写在onNext(事件响应的时候)，才可以切换回主线程
                                observerC.onNext(t);
                            }
                        });

                    }

                    @Override
                    public void onComplete(T t) {
                        observerC.onComplete(t);

                    }

                    @Override
                    public void onError(T t) {
                        observerC.onError(t);

                    }
                };
                Observable.this.subscribe(observerB);
            }
        };
    }

}
