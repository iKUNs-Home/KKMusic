package com.ikunkun.kunmusic.adapt;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ikunkun.kunmusic.LoginActivity;
import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.comn.MusicInfo;
import com.ikunkun.kunmusic.comn.UserInfo;

import org.litepal.LitePal;

import java.util.List;
import java.util.Objects;

public class RecyclerListAdapt extends RecyclerView.Adapter implements View.OnClickListener{
    ImageView mzListDownload;
    ImageView mzCover;
    TextView mzName;
    TextView mzSinger;
    List<MusicInfo> musicInfoList;
    View listView;
    Context context;

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        initPopWindow(view,position);
    }

    //定义Adapter首先需要一个ViewHolder（用来装布局控件，连接布局控件的容器）
    //这里定义的mzListHolder继承自ViewHolder需要把search_list里的控件连接到mzListHolder用id的方式连接，放入ViewHolder里
    class mzListHolder extends RecyclerView.ViewHolder {

        public mzListHolder(@NonNull View itemView) {
            super(itemView);
            mzCover = itemView.findViewById(R.id.mzListCover);
            mzName = itemView.findViewById(R.id.mzListName);
            mzSinger = itemView.findViewById(R.id.mzListSinger);
            listView = itemView;
            mzListDownload=itemView.findViewById(R.id.mzListDownload);
        }
    }


    //构造函数
    public RecyclerListAdapt(List<MusicInfo> musicInfoList) {
        this.musicInfoList = musicInfoList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LitePal.initialize(context);
        View musicView = View.inflate(parent.getContext(), R.layout.search_list, null);
        mzListHolder mzList = new mzListHolder(musicView);
//        音乐的“更多”选项按钮
        mzListDownload.setOnClickListener(this);
        return mzList;
    }

    private void initPopWindow(View v,int position) {
        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.item_popip, null, false);
        Button btn_xixi = (Button) view.findViewById(R.id.btn_xixi);
        Button btn_hehe = (Button) view.findViewById(R.id.btn_hehe);
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                700, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.setAnimationStyle(R.style.popwindow_anim_style);  //设置加载动画
        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效
        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, -510, 0);
        //设置popupWindow里的按钮的事件
//        下载按钮
        btn_xixi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "你点击了嘻嘻~", Toast.LENGTH_SHORT).show();
                popWindow.dismiss();
            }
        });
//        收藏按钮
        btn_hehe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                判断是否以及登录
                if (LoginActivity.tempuser != null && LoginActivity.tempuser.getUserName() != null) {
                    String user = LoginActivity.tempuser.getUserName();
                    List<UserInfo> list = LitePal.where(" userName = ?", user).find(UserInfo.class);
                    int flag = 0;
//                    判断是否已经收藏
                    for (int i = 0; i < list.get(0).getIlikename().split("---").length; i++) {
                        if (list.get(0).getIlikename().split("---")[i].equals(musicInfoList.get(position).getMusicName()) &&
                                list.get(0).getIlikesinger().split("---")[i].equals(musicInfoList.get(position).getMusicSinger())) {
                            flag = 1;
                        }
                    }
                    if (flag == 1) {
                        Toast.makeText(context, "已在收藏列表中~", Toast.LENGTH_SHORT).show();
                    }else {

                        UserInfo userInfo=new UserInfo();
                        userInfo.setUserName(list.get(0).getUserName());
                        userInfo.setUserPwd(list.get(0).getUserPwd());
                        userInfo.setUserId(list.get(0).getUserId());
                        userInfo.setIlikename(list.get(0).getIlikename());
                        userInfo.setIlikesinger(list.get(0).getIlikesinger());
//                      userInfo.setIlikeurl(list.get(0).getIlikeurl());
//                    删除旧的数据
                        list.get(0).delete();
                        System.out.println(musicInfoList.get(position).getMusicName() + "---" + musicInfoList.get(position).getMusicSinger());

//                    歌名
                        userInfo.setIlikename(userInfo.getIlikename()+"---"+musicInfoList.get(position).getMusicName());
//                    歌手
                        userInfo.setIlikesinger(userInfo.getIlikesinger()+"---"+musicInfoList.get(position).getMusicSinger());
//                    url
//                      userInfo.setIlikes(userInfo.getIlikesinger()+"---"+musicInfoList.get(position).getMusicSinger());
                        System.out.println("length:"+userInfo.getIlikesinger().split("---").length+"user:"+userInfo.getUserName());
//                        System.out.println("test3"+userInfo.getIlikename());
//                    保存新的数据
                        userInfo.save();
                        Toast.makeText(context, "收藏成功~", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "请先登录~", Toast.LENGTH_SHORT).show();
                }

                popWindow.dismiss();
            }
        });
    }


    //这个函数用来设置布局控件中每个项目(通过position)的数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        /**
            网络图片不能加载问题(可加载https不能加载http)：
                需在AndroidManifest.xml文件下添加安全协议
         **/
        if(musicInfoList.get(position).getBmpDraw()!=null){
            mzCover.setImageDrawable(musicInfoList.get(position).getBmpDraw());
        }else {
            System.out.println("null");
            Glide.with(listView).load(musicInfoList.get(position).getPageImg()).into(mzCover);
        }
        mzName.setText(musicInfoList.get(position).getMusicName());
        mzSinger.setText(musicInfoList.get(position).getMusicSinger());
        mzListDownload.setTag(position);
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
