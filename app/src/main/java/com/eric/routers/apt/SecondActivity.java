package com.eric.routers.apt;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eric.router_annotation.Router;
import com.eric.rxjava.R;

/**
 * @Author: chen
 * @datetime: 2021/6/29
 * @desc:
 */
@Router("/food/main")
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.eric.rxjava.R.layout.activity_second);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        View view = getWindow().getDecorView();


    }
}
