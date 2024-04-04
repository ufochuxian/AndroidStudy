package com.eric.lifecycle;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * @Author: chen
 * @datetime: 2021/10/25
 * @desc:
 */
public class LifeCycleActivity extends Activity implements ReportFragment.Observer {

    public static final String LIFECYCLE = "androidX.lifecycle";

    /**
     * Fragment可以感知到Activity的生命周期，所以这里设计一个包含Fragment的Activity
     * 那么就可以感知到activity的生命周期了
     *
     * @param savedInstanceState
     */


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ReportFragment fragment = (ReportFragment) getFragmentManager().findFragmentByTag(LIFECYCLE);
        if (fragment == null) {
            fragment = new ReportFragment();
            fragment.setObserver(this);
        }

        with(fragment);
    }

    private void with(Fragment fragment) {
        getFragmentManager().beginTransaction().add(fragment, LIFECYCLE).commitAllowingStateLoss();
    }

    @Override
    public void onMCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onMStart(boolean b) {
        onStart();
    }

    @Override
    public void onMStop() {


    }
}
