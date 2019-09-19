package cn.dxs.dagger2;

import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import cn.dxs.dagger2.cook.Chef;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    private static final String TAG = "MainActivity";

    @Inject
    Chef chef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, chef.cook());
    }
}
