package com.ikunkun.kunmusic;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.ikunkun.kunmusic.comn.MusicInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private static Context sContext;
    public static List<MusicInfo> curUserMusicList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();

        System.out.println("这里是app");
        SharedPreferences sp = getSharedPreferences("apStatus", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        sp = getSharedPreferences("localMusicListStatus",MODE_PRIVATE);
        editor = sp.edit();
        editor.clear();
        editor.apply();
        sp = getSharedPreferences("iLikeMusicListStatus",MODE_PRIVATE);
        editor = sp.edit();
        editor.clear();
        editor.apply();

        LitePal.initialize(this);
    }

    public static Context getContext() {
        return sContext;
    }
}

