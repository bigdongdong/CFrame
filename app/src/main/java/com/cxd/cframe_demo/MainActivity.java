package com.cxd.cframe_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cxd.cframe.base.CBaseActivity;

public class MainActivity extends CBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void configure(Config c) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreateView() {

    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void setListeners() {

    }
}
