package com.ikunkun.kunmusic;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;
import com.ikunkun.kunmusic.adapt.FragmentAdapter;
import com.ikunkun.kunmusic.service.MusicService;
import com.ikunkun.kunmusic.tools.ImageFilter;
import com.ikunkun.kunmusic.views.apCoverFragment;
import com.ikunkun.kunmusic.views.apLyricFragment;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class AudioPlayer extends AppCompatActivity implements View.OnClickListener {

    public static ImageView apBlurBK;
    private static ImageButton apNext;
    private static ImageButton apPrevious;
    private static ImageButton apPlay;
    private static ImageButton apBack;
    private static ImageButton apPlayMode;
    private static ImageButton apLike;

    private static SeekBar mSeekBar;
    public static TextView apProgress, apTotal, apName, apSinger;
    private ObjectAnimator animator;
    private static MusicService.MusicControl musicControl;
    private static apCoverFragment.controlAnimator animatorControl;
    //        MusicServiceConn conn;
    Intent mIntent;
    private boolean isUnbind = false;
    private ServiceConnection mcn;
    private static Context mContext;

    private static ViewPager apViewPager;

    static apCoverFragment.coverControl coverControl;

    public static Bundle apBundle = new Bundle();

    public void setStatusBarTranslucent() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this,
                0, null);
        StatusBarUtil.setLightMode(this);
    }

    public static Handler musicSetHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 300:
                    apBundle = msg.getData();
                    String musicUrl = apBundle.getString("musicUrl");
                    String musicCoverUrl = apBundle.getString("musicCover");
                    System.out.println("Handler " + musicUrl);
                    System.out.println("Handler " + musicCoverUrl);

                    musicControl.ReSetMusic(musicUrl, apBundle);
                    System.out.println("2222");
                    Glide.with(mContext).asBitmap().load(musicCoverUrl).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Bitmap BlurBackground = ImageFilter.blurBitmap(mContext, resource, 25f);
                            apBlurBK.setImageBitmap(BlurBackground);
                            coverControl.setApCover(resource);
                        }
                    });
//                    apPlayMZ();
                    break;
                case 301:
                    Toast.makeText(mContext, "??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    break;
                case 310:
                    apBundle = msg.getData();
                    String musicPath = apBundle.getString("musicUrl");
                    String mzCover = apBundle.getString("musicCover");
                    String mzBase = apBundle.getString("musicBase");

                    if (mzCover == null) {
                        if (mzBase == null) {
                            coverControl.setApCoverResource(R.drawable.cover1);
                            Bitmap resource = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.cover1);
                            Bitmap BlurBackground = ImageFilter.blurBitmap(mContext, resource, 25f);
                            AudioPlayer.apBlurBK.setImageBitmap(BlurBackground);
                        }else {
                            System.out.println("base:" + mzBase);
                            Bitmap resource = base64ToBitmap(mzBase);
                            Bitmap BlurBackground = ImageFilter.blurBitmap(mContext, resource, 25f);
                            AudioPlayer.apBlurBK.setImageBitmap(BlurBackground);
                            coverControl.setApCover(resource);
                        }
//            Drawable drawable=new BitmapDrawable(resource);
//            coverControl.setApCoverDrawable(drawable);
                    } else {
                        Glide.with(mContext).asBitmap().load(mzCover).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Bitmap BlurBackground = ImageFilter.blurBitmap(mContext, resource, 25f);
                                apBlurBK.setImageBitmap(BlurBackground);
                                coverControl.setApCover(resource);
                            }
                        });
                    }
//                    String musicName = bundle2.getString("musicName");
//                    String musicSinger = bundle2.getString("musicSinger");
//                    apName.setText(musicName);
//                    apSinger.setText(musicSinger);

//                        Bitmap resource=base64ToBitmap(mzbase);
//                        Bitmap BlurBackground = ImageFilter.blurBitmap(mContext, resource, 25f);
//                        apBlurBK.setImageBitmap(BlurBackground);
//                        coverControl.setApCover(resource);

                    System.out.println("Handler " + musicPath);
                    musicControl.ReSetMusic(musicPath, apBundle);
            }
        }
    };

    public static Handler handler3 = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            apBundle = msg.getData();
            String musicName = apBundle.getString("musicName");
            String musicSinger = apBundle.getString("musicSinger");
            String mzCover = apBundle.getString("musicCover");
            String mzBase = apBundle.getString("musicBase");

            apName.setText(musicName);
            apSinger.setText(musicSinger);

            if (mzCover == null) {
                if (mzBase == null) {
                    coverControl.setApCoverResource(R.drawable.cover1);
                    Bitmap resource = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.cover1);
                    Bitmap BlurBackground = ImageFilter.blurBitmap(mContext, resource, 25f);
                    AudioPlayer.apBlurBK.setImageBitmap(BlurBackground);
                }else {
                    System.out.println("base:" + mzBase);
                    Bitmap resource = base64ToBitmap(mzBase);
                    Bitmap BlurBackground = ImageFilter.blurBitmap(mContext, resource, 25f);
                    AudioPlayer.apBlurBK.setImageBitmap(BlurBackground);
                    coverControl.setApCover(resource);
                }
//            Drawable drawable=new BitmapDrawable(resource);
//            coverControl.setApCoverDrawable(drawable);
            } else {
                Glide.with(mContext).asBitmap().load(mzCover).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap BlurBackground = ImageFilter.blurBitmap(mContext, resource, 25f);
                        apBlurBK.setImageBitmap(BlurBackground);
                        coverControl.setApCover(resource);
                    }
                });
            }
            apPlayMZ();
        }
    };

    public static Handler handler2 = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

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
        System.out.println("?????????AudioPlayer");
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_audio_player);
        mContext = getApplicationContext();

        SharedPreferences sp = getSharedPreferences("apStatus", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("apActive", true);
        editor.apply();
        System.out.println("truetruetrue");

        mContext = getApplicationContext();

//        apBlurBK = findViewById(R.id.apBlurBackground);
//        Resources res = mContext.getResources();
//        Bitmap background = BitmapFactory.decodeResource(res, R.drawable.cover1);
//        Bitmap BlurBackground = ImageFilter.blurBitmap(this, background, 25f);
//        apBlurBK.setImageBitmap(BlurBackground);
        init();

//        Bundle bundle = getIntent().getExtras();
//        String mzName = bundle.getString("musicName");
//        String mzSinger = bundle.getString("musicSinger");
//        String mzCover = bundle.getString("musicCover");
//
//        Bundle bundle = getIntent().getExtras();
//        String mzName = bundle.getString("musicName");
//        String mzSinger = bundle.getString("musicSinger");
//        String mzCover = bundle.getString("musicCover");


//        apName.setText(mzName);
//        apSinger.setText(mzSinger);

//        Glide.with(mContext).asBitmap().load(mzCover).into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                Bitmap BlurBackground = ImageFilter.blurBitmap(mContext, resource, 25f);
//                apBlurBK.setImageBitmap(BlurBackground);
//                coverControl.setApCover(resource);
//            }
//        });
    }

    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length );
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

        apName = findViewById(R.id.apName);
        apSinger = findViewById(R.id.apSinger);

        apProgress = findViewById(R.id.apProgress);
        apTotal = findViewById(R.id.apTotal);
        mSeekBar = findViewById(R.id.apSeekBar);
        apNext = findViewById(R.id.ib_next);
        apPrevious = findViewById(R.id.ib_previous);
        apPlay = findViewById(R.id.ib_pause);
        apBack = findViewById(R.id.ib_back);
        apLike = findViewById(R.id.doYouLike);
        apPlayMode = findViewById(R.id.playMode);

        apViewPager = findViewById(R.id.centerView);
        List<Fragment> fragments = new ArrayList<>();
        Fragment apCoverFragment = new apCoverFragment();
        apBundle = getIntent().getExtras();
        apCoverFragment.setArguments(apBundle);
        fragments.add(apCoverFragment);
        fragments.add(new apLyricFragment());
        FragmentAdapter fragmentAdapter = new FragmentAdapter(fragments, getSupportFragmentManager());
        apViewPager.setAdapter(fragmentAdapter);
        animatorControl = new apCoverFragment.controlAnimator();

        coverControl = new apCoverFragment.coverControl();

        apLike.setOnClickListener(this);
        apPlayMode.setOnClickListener(this);
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
            apBlurBK = findViewById(R.id.apBlurBackground);
            Resources res = mContext.getResources();
            Bitmap background = BitmapFactory.decodeResource(res, R.drawable.cover2);
            Bitmap BlurBackground = ImageFilter.blurBitmap(this, background, 25f);
            apBlurBK.setImageBitmap(BlurBackground);
//            System.out.println(6666666);
//            animatorControl.startAnimator();
        }
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i == mSeekBar.getMax()) {
                    animatorControl.pauseAnimator();
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

            if (musicControl.isPlaying()) {
                apPlay.setBackgroundResource(R.drawable.play);
                if (!animatorControl.isAnimatorNull() && !animatorControl.isStartAnimator()) {
                    animatorControl.startAnimator();
                }
            } else {
                apPlay.setBackgroundResource(R.drawable.pause);
            }
        }
    };


    private void unbind(boolean isUnbind) {
        if (!isUnbind) {
            musicControl.pausePlay();
            unbindService(mcn);
            stopService(mIntent);
        }
    }

    public static void apPlayMZ() {
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
    }

    @Override
    public void onClick(View view) {
        SharedPreferences sp = getSharedPreferences("apStatus", MODE_PRIVATE);

        switch (view.getId()) {
            case R.id.ib_previous:
                musicControl.preMusic();
                break;
            case R.id.ib_pause:
                apPlayMZ();
                break;
            case R.id.ib_next:
//                musicControl.continuePlay(nowProgress);
//                animator.start();
                musicControl.nextMusic();

                break;
            case R.id.ib_back:
                moveTaskToBack(true);
                break;
            case R.id.playMode:
                boolean playMode = sp.getBoolean("playMode", false);
                SharedPreferences.Editor editor = sp.edit();
                if (!playMode) {
                    musicControl.changePlayMode();
                    apPlayMode.setBackgroundResource(R.drawable.recycle);
                    editor.putBoolean("playMode", true);
                    editor.apply();
                    int height = getWindowManager().getDefaultDisplay().getHeight();
                    Toast toast1 = Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT);
                    toast1.setGravity(Gravity.TOP, 0, height / 10);
                    toast1.show();

                } else {
                    musicControl.changePlayMode();
                    apPlayMode.setBackgroundResource(R.drawable.sequence);
                    editor.putBoolean("playMode", false);
                    editor.apply();
                    int height = getWindowManager().getDefaultDisplay().getHeight();
                    Toast toast1 = Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT);
                    toast1.setGravity(Gravity.TOP, 0, height / 10);
                    toast1.show();
                }
                break;
            case R.id.doYouLike:
                SharedPreferences.Editor editor2 = sp.edit();
                boolean doyoulike = sp.getBoolean("doyoulike", false);
                if (!doyoulike) {
                    apLike.setBackgroundResource(R.drawable.like);
                    editor2.putBoolean("doyoulike", true);
                    editor2.apply();
                    int height = getWindowManager().getDefaultDisplay().getHeight();
                    Toast toast1 = Toast.makeText(mContext, "?????????", Toast.LENGTH_SHORT);
                    toast1.setGravity(Gravity.TOP, 0, height * 2 / 3);
                    toast1.show();
                } else {
                    apLike.setBackgroundResource(R.drawable.unlike);
                    editor2.putBoolean("doyoulike", false);
                    editor2.apply();
                    int height = getWindowManager().getDefaultDisplay().getHeight();
                    Toast toast1 = Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT);
                    toast1.setGravity(Gravity.TOP, 0, height * 2 / 3);
                    toast1.show();
                }


                break;
//            case R.id.apTuichu:
//                unbind(isUnbind);
//                isUnbind = true;
//                finish();
        }
    }

    @Override
    protected void onDestroy() {
        System.out.println("apDestroy");

        SharedPreferences sp = getSharedPreferences("apStatus", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("apActive", false);
        editor.apply();
//
//        editor.clear();
//        editor.apply();

        super.onDestroy();
//        unbind(isUnbind);
    }
}