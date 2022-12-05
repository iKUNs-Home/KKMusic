package com.ikunkun.kunmusic.adapt;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPageAdapt extends PagerAdapter
{
    private List<View> mViews;      //存储视图
    private List<String> mTitles;   //存储标题

    public ViewPageAdapt(List<View> Views) {
        this(Views, null);
    }

    //构造函数
    public ViewPageAdapt(List<View> Views, List<String> Titles) {
        this.mViews = Views;
        this.mTitles = Titles;
        if (mTitles == null) {
            mTitles = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.size() > 0 ? mTitles.get(position) : "";
    }
}
