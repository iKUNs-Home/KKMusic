package com.ikunkun.kunmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ikunkun.kunmusic.adapt.FragmentAdapter;
import com.ikunkun.kunmusic.service.MusicService;
import com.ikunkun.kunmusic.tools.ImageFilter;
import com.ikunkun.kunmusic.views.apCoverFragment;
import com.ikunkun.kunmusic.views.apLyricFragment;

import java.util.ArrayList;
import java.util.List;

public class AudioPlayer extends AppCompatActivity implements View.OnClickListener {

    private static ImageView apBlurBK;
    private static ImageButton apNext;
    private static ImageButton apPrevious;
    private static ImageButton apPlay;
    private static ImageButton apBack;

    private static SeekBar mSeekBar;
    private static TextView apProgress, apTotal;
    private ObjectAnimator animator;
    private MusicService.MusicControl musicControl;
    private apCoverFragment.controlAnimator animatorControl;
    //        MusicServiceConn conn;
    Intent mIntent;
    private boolean isUnbind = false;
    private ServiceConnection mcn;

    private static ViewPager apViewPager;

    public static Handler handler2 = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 207:
                    apPlay.setBackgroundResource(R.drawable.play);
                    System.out.println("207");
                    break;
                case 208:
                    apPlay.setBackgroundResource(R.drawable.pause);
                    System.out.println("208");
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        apBlurBK = findViewById(R.id.apBlurBackground);
        Resources res = AudioPlayer.this.getResources();
        Bitmap background = BitmapFactory.decodeResource(res, R.drawable.cover1);
        Bitmap BlurBackground = ImageFilter.blurBitmap(this, background, 25f);
        apBlurBK.setImageBitmap(BlurBackground);
        init();
    }

    public boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfoList = activityManager.getRunningServices(30);
        if (!(serviceInfoList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceInfoList.size(); i++) {
            if (serviceInfoList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    private void init() {
        apProgress = findViewById(R.id.apProgress);
        apTotal = findViewById(R.id.apTotal);
        mSeekBar = findViewById(R.id.apSeekBar);
        apNext = findViewById(R.id.ib_next);
        apPrevious = findViewById(R.id.ib_previous);
        apPlay = findViewById(R.id.ib_pause);
        apBack = findViewById(R.id.ib_back);

        apViewPager = findViewById(R.id.centerView);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new apCoverFragment());
        fragments.add(new apLyricFragment());
        FragmentAdapter fragmentAdapter = new FragmentAdapter(fragments, getSupportFragmentManager());
        apViewPager.setAdapter(fragmentAdapter);
        animatorControl = new apCoverFragment.controlAnimator();

        apBack.setOnClickListener(this);
        apNext.setOnClickListener(this);
        apPrevious.setOnClickListener(this);
        apPlay.setOnClickListener(this);
        mIntent = new Intent(this, MusicService.class);
        mcn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                musicControl = (MusicService.MusicControl) iBinder;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
//        startService(mIntent);
        if (!isServiceRunning(getApplicationContext(), "MusicService")) {
            bindService(mIntent, mcn, BIND_AUTO_CREATE);
        }
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i == mSeekBar.getMax()) {
                    animator.pause();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                musicControl.seekTo(progress);
            }
        });
//        ImageView apCover = findViewById(R.id.apCover);
//        animator = ObjectAnimator.ofFloat(apCover, "rotation", 0f, 360.0f);
//        animator.setDuration(10000);
//        animator.setInterpolator(new LinearInterpolator());
//        animator.setRepeatCount(-1);
    }

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");
            mSeekBar.setMax(duration);
            mSeekBar.setProgress(currentPosition);

            int minute = duration / 1000 / 60;
            int second = duration / 1000 % 60;
            String strMinute = null;
            String strSecond = null;
            if (minute < 10) {
                strMinute = "0" + minute;
            } else {
                strMinute = minute + "";
            }
            if (second < 10) {
                strSecond = "0" + second;
            } else {
                strSecond = second + "";
            }
            apTotal.setText(strMinute + ":" + strSecond);
            minute = currentPosition / 1000 / 60;
            second = currentPosition / 1000 % 60;
            if (minute < 10) {
                strMinute = "0" + minute;
            } else {
                strMinute = minute + "";
            }
            if (second < 10) {
                strSecond = "0" + second;
            } else {
                strSecond = second + "";
            }
            apProgress.setText(strMinute + ":" + strSecond);
        }
    };


    private void unbind(boolean isUnbind) {
        if (!isUnbind) {
            musicControl.pausePlay();
            unbindService(mcn);
            stopService(mIntent);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_previous:
//                musicControl.play();
//                animator.start();
                break;
            case R.id.ib_pause:
//                System.out.println("666" + musicControl.curPosition());
                musicControl.play();
                Message msg = MainActivity.handler.obtainMessage();
                if (musicControl.isPlaying()) {
                    apPlay.setBackgroundResource(R.drawable.play);
                    msg.what = 107;
                    if (animatorControl.isPausedAnimator()) {
                        animatorControl.resumeAnimator();
                    } else {
                        animatorControl.startAnimator();
                    }
                } else {
                    apPlay.setBackgroundResource(R.drawable.pause);
                    msg.what = 108;
                    animatorControl.pauseAnimator();
                }
                MainActivity.handler.sendMessage(msg);
                break;
            case R.id.ib_next:
//                musicControl.continuePlay(nowProgress);
//                animator.start();
//            case R.id.apTuichu:
//                unbind(isUnbind);
//                isUnbind = true;
//                finish();
                break;
            case R.id.ib_back:
                moveTaskToBack(true);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        System.out.println("apDestroy");
        super.onDestroy();
//        unbind(isUnbind);
    }
}