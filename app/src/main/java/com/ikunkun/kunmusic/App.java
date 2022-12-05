package com.ikunkun.kunmusic;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

public class App extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        LitePal.initialize(this);
    }

    public static Context getContext() {
        return sContext;
    }
}

