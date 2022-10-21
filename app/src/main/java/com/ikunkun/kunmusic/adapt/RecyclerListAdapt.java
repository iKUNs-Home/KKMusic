package com.ikunkun.kunmusic.adapt;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.SearchActivity;
import com.ikunkun.kunmusic.comn.MusicInfo;

import java.util.List;

public class RecyclerListAdapt extends RecyclerView.Adapter {

    ImageView mzCover;
    TextView mzName;
    TextView mzSinger;
    Button mzDownload;
    List<MusicInfo> musicInfoList;
    View listView;


    //定义Adapter首先需要一个ViewHolder（用来装布局控件，连接布局控件的容器）
    //这里定义的mzListHolder继承自ViewHolder需要把search_list里的控件连接到mzListHolder用id的方式连接，放入ViewHolder里
    class mzListHolder extends RecyclerView.ViewHolder {

        public mzListHolder(@NonNull View itemView) {
            super(itemView);
            mzCover = itemView.findViewById(R.id.mzListCover);
            mzName = itemView.findViewById(R.id.mzListName);
            mzSinger = itemView.findViewById(R.id.mzListSinger);
            mzDownload = itemView.findViewById(R.id.mzListDownload);
            listView = itemView;
        }
    }


    //构造函数
    public RecyclerListAdapt(List<MusicInfo> musicInfoList) {
        this.musicInfoList = musicInfoList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View musicView = View.inflate(parent.getContext(), R.layout.search_list, null);
        mzListHolder mzList = new mzListHolder(musicView);
        return mzList;
    }


    //这个函数用来设置布局控件中每个项目(通过position)的数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        /**
            网络图片不能加载问题(可加载https不能加载http)：
                需在AndroidManifest.xml文件下添加安全协议
         **/
        Glide.with(listView).load(musicInfoList.get(position).getPageImg()).into(mzCover);
        mzName.setText(musicInfoList.get(position).getMusicName());
        mzSinger.setText(musicInfoList.get(position).getMusicSinger());
    }


    @Override
    public int getItemViewType(int position) {
        // 给每个ItemView指定不同的类型，这样在RecyclerView看来，这些ItemView全是不同的，不能复用
        return position;
    }

    //返回列表大小
    @Override
    public int getItemCount() {
        if (musicInfoList != null) {
            return musicInfoList.size();
        }
        return 0;
    }
}
