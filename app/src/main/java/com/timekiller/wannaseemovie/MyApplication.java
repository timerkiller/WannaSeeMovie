package com.timekiller.wannaseemovie;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by vke on 2016/10/5.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

//        LogCollector.setDebugMode(true);
//        LogCollector.init(getApplicationContext(), "123", null);
//        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
//
//        JPushInterface.init(getApplicationContext());
//        LogCollector.upload(false);
    }
}
