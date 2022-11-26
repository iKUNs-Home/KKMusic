package com.ikunkun.kunmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.Nullable;

import com.ikunkun.kunmusic.AudioPlayer;
import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.views.MusicRoundProgressView;

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
                    Message msg = AudioPlayer.handler.obtainMessage();
                    Message msg2 = MusicRoundProgressView.handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration", duration);
                    bundle.putInt("currentPosition", currentPosition);
                    msg.setData(bundle);
                    msg2.setData(bundle);
                    MusicRoundProgressView.handler.sendMessage(msg2);
                    AudioPlayer.handler.sendMessage(msg);
//                    Looper.loop();
                }
            };
            mTimer.schedule(task, 5, 500);
        }
    }

    public class MusicControl extends Binder {
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