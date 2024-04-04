package com.eric.lifecycle;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * @Author: chen
 * @datetime: 2021/10/25
 * @desc:
 */
public class ReportFragment extends Fragment {

    private Observer observer;

    private static final String TAG = "ReportFragment";

    public boolean isActive = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        isActive = true;

    }


    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        isActive = true;
        observer.onMStart(true);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        isActive = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        isActive = false;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        isActive = false;
    }


    interface Observer {

        void onMCreate(@Nullable Bundle savedInstanceState);

        void onMStart(boolean b);

        void onMStop();
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

}
