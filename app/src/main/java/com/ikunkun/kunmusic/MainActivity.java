package com.ikunkun.kunmusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.ikunkun.kunmusic.adapt.FragmentAdapter;
import com.ikunkun.kunmusic.adapt.RecyclerListAdapt;
import com.ikunkun.kunmusic.service.MusicService;
import com.ikunkun.kunmusic.views.AboutFragment;
import com.ikunkun.kunmusic.views.CommunityFragment;
import com.ikunkun.kunmusic.views.HomeFragment;
import com.ikunkun.kunmusic.views.MineFragment;
import com.ikunkun.kunmusic.views.apCoverFragment;
import com.jaeger.library.StatusBarUtil;
import com.xiaoyouProject.searchbox.SearchFragment;
import com.xiaoyouProject.searchbox.custom.IOnSearchClickListener;
import com.xiaoyouProject.searchbox.entity.CustomLink;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnView;    //底部导航栏
    ViewPager viewPager;            //中间滑动页
    Toolbar toolbar;                //顶部工具栏
    DrawerLayout drawerLayout;      //左边滑动抽屉

    private static ImageButton pcbPlay;
    private Intent mIntent;
    private ServiceConnection mcn;
    private MusicService.MusicControl musicControl;
    private apCoverFragment.controlAnimator animatorControl;
    public void setStatusBarTranslucent() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this,
                0, null);
        StatusBarUtil.setLightMode(this);
    }


    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 107:
                    pcbPlay.setBackgroundResource(R.drawable.play);
                    System.out.println("107");
                    break;
                case 108:
                    pcbPlay.setBackgroundResource(R.drawable.pause);
                    System.out.println("108");
                    break;
            }
        }
    };

    public void pcbInit() {
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
                musicControl.play();
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
        });

        RelativeLayout pcb = (RelativeLayout) findViewById(R.id.pcb);
        pcb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, AudioPlayer.class);
                startActivity(in);
            }
        });
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
//        //.getExtras()得到intent所附带的值
//        Bundle bundle=getData.getExtras();
//        //通过key获取相应的value
//        String userName=bundle.getString("userName");
//        textView.setText(userName);
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //设置主布局
        setContentView(R.layout.activity_main);

        pcbInit();

        //获取布局控件等的id
        bnView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        //设置顶部为自定义的toolbar
//        getSupportActionBar().hide();
        setSupportActionBar(toolbar);

        //初始化搜索框
        SearchFragment searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            /**
             *  点击搜索按钮时触发
             * @param keyword 搜索的关键词
             */
            @Override
            public void onSearchClick(String keyword) {
                System.out.println("keyword = " + keyword);
                //开启搜索列表activity，同时向其传送搜索关键词
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                searchIntent.putExtra("keyword", keyword);
                startActivity(searchIntent);
            }

            /**
             *  点击关键词预测链接时触发
             * @param data 链接携带的数据
             */
            @Override
            public void onLinkClick(Object data) {

            }

            /**
             *  当搜索框内容改变时触发
             * @param keyword 搜索的关键词
             */
            @Override
            public void onTextChange(String keyword) {
//                // 数据初始化
//                List<CustomLink<String>> data = new ArrayList<>();
//                data.add(new CustomLink<>("链接1", "数据1"));
//                data.add(new CustomLink<>("链接2", "数据2"));
//                data.add(new CustomLink<>("链接3", "数据3"));
//                // 这里我们设置关键词预测显示的内容
//                searchFragment.setLinks(data);
            }
        });

        //设置顶部Toolbar的菜单键事件监听
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.music_search:
                        //显示搜索框
                        searchFragment.showFragment(getSupportFragmentManager(), SearchFragment.TAG);

//                        Intent intent = new Intent(MainActivity.this,SearchActivity.class);
//                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        //关联drawerLayout和toolbar控件
        ActionBarDrawerToggle barDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        barDrawerToggle.syncState();
        drawerLayout.addDrawerListener(barDrawerToggle);

        //设置抽屉划出时阴影部分的背景颜色(透明)
        drawerLayout.setScrimColor(Color.TRANSPARENT);


        //初始化viewpage(滑动页)的每个分页
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new MineFragment());
        fragments.add(new CommunityFragment());
        fragments.add(new AboutFragment());

        //初始化viewpager的自定义适配器
        FragmentAdapter fragmentAdapter = new FragmentAdapter(fragments, getSupportFragmentManager());
        //给中间滑动页设置适配器
        viewPager.setAdapter(fragmentAdapter);

        //登录以后设置抽屉显示的用户名
        NavigationView nav_view = (NavigationView) findViewById(R.id.navigation_view);
        View headerView = nav_view.getHeaderView(0);
        TextView nav_name = (TextView) headerView.findViewById(R.id.nav_name);

        Intent getData = getIntent();
        //getExtras()得到intent所附带的值
        String userName = getData.getStringExtra("userName");
        System.out.println("userName:"+userName);
        //通过key获取相应的value
        if (userName != null) {
            nav_name.setText(userName);
        }
        //给底部导航栏设置 点击事件监听
        bnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //获取按钮id
                int menuId = item.getItemId();
                //转跳指定页面:Fragment
                switch (menuId) {
                    case R.id.tab_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.tab_mine:
                        viewPager.setCurrentItem(1);
                        Intent in = new Intent(MainActivity.this, AudioPlayer.class);
                        startActivity(in);
                        break;
                    case R.id.tab_community:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.tab_about:
                        viewPager.setCurrentItem(3);
                        break;
                }

                return false;
            }
        });


        //ViewPager 滑动事件监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //将滑动到的页面对应的 menu(底部导航栏) 设置为选中状态
                bnView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

}