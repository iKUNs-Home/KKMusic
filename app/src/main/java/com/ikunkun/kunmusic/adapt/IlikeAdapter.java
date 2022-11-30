package com.ikunkun.kunmusic.adapt;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ikunkun.kunmusic.LoginActivity;
import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.comn.UserInfo;

import org.litepal.LitePal;

import java.util.List;

public class IlikeAdapter extends AppCompatActivity {
    private static android.widget.ListView ListView;
//    含下标为0的数组
    private static String[] tempmznames;
    private static String[] tempmzsinger;
//    删去下标为0的数组
    private static String[] mznames;
    private static String[] mzsinger;
    List<UserInfo> list;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        从数据库获取数据
        list = LitePal.where(" userName = ?", LoginActivity.tempuser.getUserName()).find(UserInfo.class);
//        此方法划分数组下标为0式，为null，需删去
        tempmznames=list.get(0).getIlikename().split("---");
        tempmzsinger=list.get(0).getIlikesinger().split("---");
//        删去后的数据
        mznames=new String[tempmznames.length-1];
        mzsinger=new String[tempmznames.length-1];
        for(int i=1;i<tempmznames.length;i++){
            System.out.println(tempmznames[i]+"---"+tempmzsinger[i]);
            mznames[i-1]=tempmznames[i];
            mzsinger[i-1]=tempmzsinger[i];
        }
        setContentView(R.layout.activity_ilike);
        ListView = findViewById(R.id.Ilike_list);
        MyBaseAdapter mAdapter = new MyBaseAdapter();
        ListView.setAdapter(mAdapter);
    }

    private class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mznames.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view = View.inflate(IlikeAdapter.this, R.layout.search_list, null);
                TextView mzListName = view.findViewById(R.id.mzListName);
                TextView mzListSinger= view.findViewById(R.id.mzListSinger);
                mzListName.setText(mznames[i]);
                mzListSinger.setText(mzsinger[i]);
            return view;
        }
    }
}
