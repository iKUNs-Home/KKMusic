package com.ikunkun.kunmusic;

import static com.ikunkun.kunmusic.App.curUserMusicList;
import static com.ikunkun.kunmusic.adapt.RecyclerListAdapt.base64ToBitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.ikunkun.kunmusic.adapt.FragmentAdapter;
import com.ikunkun.kunmusic.adapt.RecyclerListAdapt;
import com.ikunkun.kunmusic.comn.MusicInfo;
import com.ikunkun.kunmusic.comn.UserInfo;
import com.ikunkun.kunmusic.service.MusicService;
import com.ikunkun.kunmusic.tools.DownloadUtil;
import com.ikunkun.kunmusic.tools.ImageFilter;
import com.ikunkun.kunmusic.views.AboutFragment;
import com.ikunkun.kunmusic.views.CommunityFragment;
import com.ikunkun.kunmusic.views.HomeFragment;
import com.ikunkun.kunmusic.views.MineFragment;
import com.ikunkun.kunmusic.views.TestFragment;
import com.ikunkun.kunmusic.views.apCoverFragment;
import com.jaeger.library.StatusBarUtil;
import com.xiaoyouProject.searchbox.SearchFragment;
import com.xiaoyouProject.searchbox.custom.IOnSearchClickListener;
import com.xiaoyouProject.searchbox.entity.CustomLink;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnView;    //???????????????
    ViewPager viewPager;            //???????????????
    Toolbar toolbar;                //???????????????
    DrawerLayout drawerLayout;      //??????????????????

    private static ImageButton pcbPlay;
    private static Intent mIntent;
    private static ServiceConnection mcn;
    private static MusicService.MusicControl musicControl;
    private static apCoverFragment.controlAnimator animatorControl;
    private static Context mContext;
    private static final String apiMusicIP = "http://192.168.152.202:3000/";
    private static TextView pcbName, pcbSinger;
    private static Bundle curMusicInfo;
    private static long downloadId;
    private static DownloadManager downloadManager;
    private static ShapeableImageView pcbCover;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    public void setStatusBarTranslucent() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this,
                0, null);
        StatusBarUtil.setLightMode(this);
    }


    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 300:
                case 310:
                    System.out.println("----------------");
                    curMusicInfo = msg.getData();
                    String musicName = curMusicInfo.getString("musicName");
                    String musicSinger = curMusicInfo.getString("musicSinger");
                    String mzCover = curMusicInfo.getString("musicCover");
                    String mzBase = curMusicInfo.getString("musicBase");

                    if (mzCover == null) {
                        if (mzBase == null) {
                            pcbCover.setImageResource(R.drawable.cover1);
                        }else {
                            System.out.println("base:" + mzBase);
                            Bitmap resource = base64ToBitmap(mzBase);
                            pcbCover.setImageBitmap(resource);
                        }
//            Drawable drawable=new BitmapDrawable(resource);
//            coverControl.setApCoverDrawable(drawable);
                    } else {
                        Glide.with(mContext).asBitmap().load(mzCover).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                pcbCover.setImageBitmap(resource);
                            }
                        });
                    }
                    pcbName.setText(musicName);
                    pcbSinger.setText(musicSinger);
                    break;
                case 107:
                    pcbPlay.setBackgroundResource(R.drawable.play);
                    System.out.println("107");
                    break;
                case 108:
                    pcbPlay.setBackgroundResource(R.drawable.pause);
                    System.out.println("108");
                    break;
                case 311:
//                    Bundle bundle2 = msg.getData();
                    curMusicInfo = msg.getData();
                    System.out.println("curMusicInfo" + curMusicInfo);
                    String musicPath = curMusicInfo.getString("musicUrl");
//                    String musicName = bundle2.getString("musicName");
//                    String musicSinger = bundle2.getString("musicSinger");
//                    pcbName.setText(musicName);
//                    pcbSinger.setText(musicSinger);
                    System.out.println("Handler " + musicPath);

                    String mzCover2 = curMusicInfo.getString("musicCover");
                    String mzBase2 = curMusicInfo.getString("musicBase");

                    if (mzCover2 == null) {
                        if (mzBase2 == null) {
                            pcbCover.setImageResource(R.drawable.cover1);
                        }else {
                            System.out.println("base:" + mzBase2);
                            Bitmap resource = base64ToBitmap(mzBase2);
                            pcbCover.setImageBitmap(resource);
                        }
//            Drawable drawable=new BitmapDrawable(resource);
//            coverControl.setApCoverDrawable(drawable);
                    } else {
                        Glide.with(mContext).asBitmap().load(mzCover2).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                pcbCover.setImageBitmap(resource);
                            }
                        });
                    }

                    musicControl.ReSetMusic(musicPath, curMusicInfo);
                    break;
            }
        }
    };

    public static Handler handler2 = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            curMusicInfo = msg.getData();
            String musicName = curMusicInfo.getString("musicName");
            String musicSinger = curMusicInfo.getString("musicSinger");
            pcbName.setText(musicName);
            pcbSinger.setText(musicSinger);
            pcbPlayMZ();
        }
    };

    public void pcbInit() {
        pcbName = findViewById(R.id.pcb_songName);
        pcbSinger = findViewById(R.id.pcb_singName);

        curMusicInfo = new Bundle();

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
        pcbPlay = findViewById(R.id.pcb_play);
        animatorControl = new apCoverFragment.controlAnimator();
        pcbPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pcbPlayMZ();
            }
        });

        RelativeLayout pcb = (RelativeLayout) findViewById(R.id.pcb);
        pcb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, AudioPlayer.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("mzName", pcbName.getText().toString());
//                bundle.putString("mzSinger", pcbSinger.getText().toString());
                in.putExtras(curMusicInfo);
                startActivity(in);
            }
        });
    }

    public static void pcbPlayMZ() {
        boolean apIsActive;
        SharedPreferences sp = mContext.getSharedPreferences("apStatus", MODE_PRIVATE);
        apIsActive = sp.getBoolean("apActive", false);
        System.out.println("apIsActive " + apIsActive);

        musicControl.play();
        if (!apIsActive) {
//                Message msg = AudioPlayer.handler2.obtainMessage();
            if (musicControl.isPlaying()) {
                pcbPlay.setBackgroundResource(R.drawable.play);
//                    msg.what = 207;
//                    if (animatorControl.isPausedAnimator()) {
//                        animatorControl.resumeAnimator();
//                    } else {
//                        animatorControl.startAnimator();
//                    }
            } else {
//                    msg.what = 208;
                pcbPlay.setBackgroundResource(R.drawable.pause);
//                    animatorControl.pauseAnimator();
            }
        } else {
            Message msg = AudioPlayer.handler2.obtainMessage();
            if (musicControl.isPlaying()) {
                pcbPlay.setBackgroundResource(R.drawable.play);
                msg.what = 207;
                if (animatorControl.isPausedAnimator()) {
                    animatorControl.resumeAnimator();
                } else {
                    animatorControl.startAnimator();
                }
            } else {
                msg.what = 208;
                pcbPlay.setBackgroundResource(R.drawable.pause);
                animatorControl.pauseAnimator();
            }
            AudioPlayer.handler2.sendMessage(msg);
        }
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    //
//        setContentView(R.layout.nav_header);
//        Intent getData=getIntent();
//        TextView textView=(TextView)findViewById(R.id.nav_name);
//        //.getExtras()??????intent???????????????
//        Bundle bundle=getData.getExtras();
//        //??????key???????????????value
//        String userName=bundle.getString("userName");
//        textView.setText(userName);
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("?????????MainActivity");

        super.onCreate(savedInstanceState);
        //???????????????
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        pcbCover = findViewById(R.id.pcb_cover);

        pcbInit();
        RelativeLayout relativeLayout = findViewById(R.id.pcbControl);


//        LitePal.initialize(this);
//        String user = "1";
//        List<UserInfo> list = LitePal.where("userName = ?", user).find(UserInfo.class);
//        list.get(0).getUserMusicListName().split("----");
//        list.get(0).getUserMusicListSinger().split("----");
//        list.get(0).getUserMusicListUrl().split("----");
//        list.get(0).getUserMusicListCoverUrl().split("----");
//        curUserMusicList.addAll();


        //????????????????????????id
        bnView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        //???????????????????????????toolbar
//        getSupportActionBar().hide();
        setSupportActionBar(toolbar);

        //??????????????????
        SearchFragment searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            /**
             *  ???????????????????????????
             * @param keyword ??????????????????
             */
            @Override
            public void onSearchClick(String keyword) {
                System.out.println("keyword = " + keyword);
                //??????????????????activity????????????????????????????????????
//                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
//                searchIntent.putExtra("keyword", keyword);
//                startActivity(searchIntent);
//                bnView.setVisibility(View.GONE);
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
//                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                com.ikunkun.kunmusic.views.SearchFragment searchFragment1 = new com.ikunkun.kunmusic.views.SearchFragment();
                Bundle bundle = new Bundle();
                bundle.putString("keyword", keyword);
                searchFragment1.setArguments(bundle);
                fragmentTransaction.replace(R.id.mainFragment, searchFragment1, "searchFragment");
                fragmentTransaction.commit();
            }

            /**
             *  ????????????????????????????????????
             * @param data ?????????????????????
             */
            @Override
            public void onLinkClick(Object data) {

            }

            /**
             *  ?????????????????????????????????
             * @param keyword ??????????????????
             */
            @Override
            public void onTextChange(String keyword) {
//                // ???????????????
//                List<CustomLink<String>> data = new ArrayList<>();
//                data.add(new CustomLink<>("??????1", "??????1"));
//                data.add(new CustomLink<>("??????2", "??????2"));
//                data.add(new CustomLink<>("??????3", "??????3"));
//                // ????????????????????????????????????????????????
//                searchFragment.setLinks(data);
            }
        });

        //????????????Toolbar????????????????????????
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.music_search:
                        //???????????????
                        searchFragment.showFragment(getSupportFragmentManager(), SearchFragment.TAG);

//                        Intent intent = new Intent(MainActivity.this,SearchActivity.class);
//                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        //??????drawerLayout???toolbar??????
        ActionBarDrawerToggle barDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        barDrawerToggle.syncState();
        drawerLayout.addDrawerListener(barDrawerToggle);

        //????????????????????????????????????????????????(??????)
        drawerLayout.setScrimColor(Color.TRANSPARENT);


        //?????????viewpage(?????????)???????????????
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new MineFragment());
        fragments.add(new CommunityFragment());
//        fragments.add(new AboutFragment());

        //?????????viewpager?????????????????????
        FragmentAdapter fragmentAdapter = new FragmentAdapter(fragments, getSupportFragmentManager());
        //?????????????????????????????????
        viewPager.setAdapter(fragmentAdapter);

        //??????????????????????????????????????????
        NavigationView nav_view = (NavigationView) findViewById(R.id.navigation_view);
        View headerView = nav_view.getHeaderView(0);
        TextView nav_name = (TextView) headerView.findViewById(R.id.nav_name);

        Intent getData = getIntent();
        //getExtras()??????intent???????????????
        String userName = getData.getStringExtra("userName");
        System.out.println("userName:" + userName);
        //??????key???????????????value
        if (userName != null) {
            nav_name.setText(userName);
        }
        //???????????????????????? ??????????????????
        bnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //????????????????????????????????????
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment localFragment = fragmentManager.findFragmentByTag("localFragment");
                Fragment searchFragment = fragmentManager.findFragmentByTag("searchFragment");
                if (localFragment != null) {
                    fragmentTransaction.remove(localFragment);
                }
                if (searchFragment != null) {
                    fragmentTransaction.remove(searchFragment);
                }
                fragmentTransaction.commit();
                //????????????id
                int menuId = item.getItemId();
                //??????????????????:Fragment
                switch (menuId) {
                    case R.id.tab_home:
                        viewPager.setCurrentItem(0);
                        for (MusicInfo musicInfo : curUserMusicList) {
                            System.out.println(musicInfo);
                        }
                        break;
                    case R.id.tab_mine:
                        viewPager.setCurrentItem(1);
//                        Intent in = new Intent(MainActivity.this, AudioPlayer.class);
//                        startActivity(in);
                        break;
                    case R.id.tab_community:
                        viewPager.setCurrentItem(2);
//                        FragmentManager fragmentManager = getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        TestFragment testFragment = new TestFragment();
//                        fragmentTransaction.replace(R.id.mainFragment, testFragment);
//                        fragmentTransaction.remove(testFragment);
//                        fragmentTransaction.commit();
                        break;
//                    case R.id.tab_about:
//                        viewPager.setCurrentItem(3);
////                        bnView.setVisibility(View.GONE);
////                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
////                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
////                        FragmentManager fragmentManager = getSupportFragmentManager();
////                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                        com.ikunkun.kunmusic.views.SearchFragment searchFragment1 = new com.ikunkun.kunmusic.views.SearchFragment();
////                        Bundle bundle = new Bundle();
////                        bundle.putString("keyword","666");
////                        searchFragment1.setArguments(bundle);
////                        fragmentTransaction.add(R.id.mainFragment,searchFragment1);
////                        fragmentTransaction.commit();
//                        System.out.println(Environment.getExternalStorageDirectory().getPath()+"/Music/ikunMusic");
//                        break;
                }

                return false;
            }
        });


        //ViewPager ??????????????????
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //?????????????????????????????? menu(???????????????) ?????????????????????
                bnView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public static String getApiMusicIP() {
        return apiMusicIP;
    }

}

