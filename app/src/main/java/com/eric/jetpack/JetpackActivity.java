package com.eric.jetpack;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.eric.androidstudy.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * @Author: chen
 * @datetime: 2021/10/22
 * @desc:
 */
public class JetpackActivity extends AppCompatActivity {

    private static final String TAG = "JetpackActivity";

    //这是第一个版本，如果不适用livedata的话，那么数据是不具备生命周期感知能力的
    private int x = 0;

    //使用livedata来进行包裹数据，让数据具备生命周期感知的能力
    MutableLiveData<Integer> liveData = new MutableLiveData(x);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_jetpack);
        TextView num_txt = findViewById(R.id.num_txt);


        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                emitter.onNext(0 + "");

            }
        }).interval(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
//                        Log.i(TAG, "x : " + x);
//                        num_txt.setText(String.valueOf(x++));
                        liveData.setValue(x++);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "ERROR:" + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        liveData.observe(this, new androidx.lifecycle.Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                //此处使用这种方式的话，那么数据就具备了生命周期感知的能力，在用户界面退出到后台的时候
                //这里就不会调用，从而避免无效浪费渲染资源
                Integer value = liveData.getValue();
                Log.i(TAG, "livedata value : " + x);
                num_txt.setText(String.valueOf(value));
            }
        });

    }
}
