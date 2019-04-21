package com.example.learningplatform.app;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //获取context
        mContext = getApplicationContext();
    }
    //创建一个静态的方法，以便获取context对象
    public static Context getContext(){
        return mContext;
    }
}
