package com.ikunkun.kunmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.ikunkun.kunmusic.adapt.FragmentAdapter;
import com.ikunkun.kunmusic.views.AboutFragment;
import com.ikunkun.kunmusic.views.CommunityFragment;
import com.ikunkun.kunmusic.views.HomeFragment;
import com.ikunkun.kunmusic.views.MineFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnView;    //底部导航栏
    ViewPager viewPager;            //中间滑动页
    Toolbar toolbar;                //顶部工具栏
    DrawerLayout drawerLayout;      //左边滑动抽屉
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

        //获取布局控件等的id
        bnView = findViewById(R.id.bottom_nav_view);
        viewPager = findViewById(R.id.view_pager);
        toolbar = findViewById(R.id.tool_bar);
        drawerLayout = findViewById(R.id.drawer_layout);

        //设置顶部为自定义的toolbar
//        getSupportActionBar().hide();
        setSupportActionBar(toolbar);

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
        NavigationView nav_view=(NavigationView)findViewById(R.id.navigation_view);
        View headerView=nav_view.getHeaderView(0);
        TextView nav_name=(TextView)headerView.findViewById(R.id.nav_name);;
        Intent getData=getIntent();
        //getExtras()得到intent所附带的值
        String userName=getData.getStringExtra("userName");
        //通过key获取相应的value
        if(userName!=null) {
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