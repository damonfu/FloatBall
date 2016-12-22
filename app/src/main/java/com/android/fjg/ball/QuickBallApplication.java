package com.android.fjg.ball;

import android.app.Application;

/**
 * Created by fujianguo on 16-12-21.
 */

public class QuickBallApplication extends Application {

    private static QuickBallApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static QuickBallApplication getInstance() {
        return sInstance;
    }
}
