package com.ikunkun.kunmusic.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.Nullable;

import com.ikunkun.kunmusic.AudioPlayer;
import com.ikunkun.kunmusic.MainActivity;
import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.views.MusicRoundProgressView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class MusicService extends Service {

    private MediaPlayer mPlayer;
    private Timer mTimer;

    public MusicService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.jjx);
//        mPlayer = new MediaPlayer();
//        String url = "http://m701.music.126.net/20221130184826/aaadbc481b4d281241baaa8101cb252e/jdymusic/obj/wo3DlMOGwrbDjj7DisKw/8401700683/e96a/c59c/453c/401771d515f981fc8d991109ef088f5c.mp3";
//        try {
//            mPlayer.setDataSource(url);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mPlayer.prepareAsync();
//        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                System.out.println("Voice文件准备完毕");
//                System.out.println("Voice文件时长 " + mPlayer.getDuration() / 1000 + " ");
////                mPlayer.start();
//            }
//        });
//        mPlayer = new MediaPlayer();
    }

    @Override
    public void onDestroy() {
        System.out.println("msDestroy");
        super.onDestroy();
//        if (mPlayer == null) return;
//        if (mPlayer.isPlaying()) mPlayer.stop();
//        mPlayer.reset();
//        mPlayer.release();
//        mPlayer = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicControl();
    }

    public void addTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
//                    Looper.prepare();;
                    if (mPlayer == null) return;
                    int duration = mPlayer.getDuration();
                    int currentPosition = mPlayer.getCurrentPosition();
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration", duration);
                    bundle.putInt("currentPosition", currentPosition);

                    boolean apIsActive;
                    SharedPreferences sp = getSharedPreferences("apStatus", MODE_PRIVATE);
                    apIsActive = sp.getBoolean("apActive", false);
                    System.out.println("apIsActive2 " + apIsActive);

                    Message msg2 = MusicRoundProgressView.handler.obtainMessage();
                    msg2.setData(bundle);
                    MusicRoundProgressView.handler.sendMessage(msg2);

                    if (apIsActive) {
                        Message msg = AudioPlayer.handler.obtainMessage();
                        msg.setData(bundle);
                        AudioPlayer.handler.sendMessage(msg);
                    }
//                    Looper.loop();
                }
            };
            mTimer.schedule(task, 5, 500);
        }
    }

    public class MusicControl extends Binder {

        public void ReSetMusic(String musicUrl) {
            System.out.println("Service " + musicUrl);
            if (mPlayer.isPlaying()) mPlayer.stop();
            mPlayer.reset();
            mPlayer.release();
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(musicUrl);

            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    System.out.println("Voice文件准备完毕");
                    System.out.println("Voice文件时长 " + mPlayer.getDuration() / 1000 + " ");

                    boolean apIsActive;
                    SharedPreferences sp = getSharedPreferences("apStatus", MODE_PRIVATE);
                    apIsActive = sp.getBoolean("apActive", false);
                    System.out.println("apIsActive2 " + apIsActive);

                    if (apIsActive) {
                        Message msg = AudioPlayer.handler3.obtainMessage();
                        AudioPlayer.handler3.sendMessage(msg);
                    } else {
                        Message msg = MainActivity.handler2.obtainMessage();
                        MainActivity.handler2.sendMessage(msg);
                    }
                }
            });
        }

        public void play() {
            try {
                if (!mPlayer.isPlaying()) {
                    mPlayer.start();
                    addTimer();
                } else {
                    mPlayer.pause();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public boolean isPlaying() {
            return mPlayer.isPlaying();
        }

        public void pausePlay() {
            mPlayer.pause();
        }


        public void seekTo(int progress) {
            mPlayer.seekTo(progress);
        }
    }

}