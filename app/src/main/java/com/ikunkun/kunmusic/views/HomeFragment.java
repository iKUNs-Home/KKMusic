package com.ikunkun.kunmusic.views;

import android.Manifest;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.adapt.Home_LocalAdapter;
import com.ikunkun.kunmusic.adapt.RecyclerListAdapt;
import com.ikunkun.kunmusic.adapt.ViewPageAdapt;
import com.ikunkun.kunmusic.comn.MusicInfo;
import com.ikunkun.kunmusic.tools.MusicUtils;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    ViewPager viewPager;    //首页嵌套的滑动页
    TabLayout tabLayout;    //首页嵌套顶部导航栏
    List<View> views;       //view列表
    List<String> titles;    //标题列表
    public List<MusicInfo> list=new ArrayList<MusicInfo>();
    private androidx.recyclerview.widget.RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ////获取布局控件等的id
        viewPager = view.findViewById(R.id.home_view_pager);
        tabLayout = view.findViewById(R.id.tab_layout_HomeTop);

        //类findViewById(),自动实例化，动态添加布局
        View view1 = LayoutInflater.from(view.getContext()).inflate(R.layout.fragment_home_app, null);
        View view2 = LayoutInflater.from(view.getContext()).inflate(R.layout.fragment_home_local,null);
//        LocalMusicFragment fragment = new LocalMusicFragment();
//        View view2 = new View(fragment.getActivity());

        //添加views列表
        views = new ArrayList<>();
        views.add(view1);
        views.add(view2);

        //添加页面标题
        titles = new ArrayList<>();
        titles.add("APP首页");
        titles.add("本地音乐");

        //初始化viewPage适配器
        ViewPageAdapt viewPageAdapt = new ViewPageAdapt(views, titles);
//        Home_LocalAdapter home_localAdapter=new Home_LocalAdapter(view2.getContext());
        viewPager.setAdapter(viewPageAdapt);

        //为顶部导航栏设置标题
        for (String title : titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }

    }

}
