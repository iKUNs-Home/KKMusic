package com.ikunkun.kunmusic.adapt;

import static android.content.Context.MODE_PRIVATE;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ikunkun.kunmusic.AudioPlayer;
import com.ikunkun.kunmusic.MainActivity;
import com.ikunkun.kunmusic.LoginActivity;
import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.comn.MusicInfo;
import com.ikunkun.kunmusic.comn.UserInfo;
import com.ikunkun.kunmusic.service.MusicService;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecyclerListAdapt extends RecyclerView.Adapter implements View.OnClickListener {
    ImageView mzListDownload;
    ImageView mzCover;
    TextView mzName;
    TextView mzSinger;
    //    Button mzDownload;
    List<MusicInfo> musicInfoList;
    View listView;
    Context context;
    LinearLayout songItem;

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        initPopWindow(view, position);
    }

    //定义Adapter首先需要一个ViewHolder（用来装布局控件，连接布局控件的容器）
    //这里定义的mzListHolder继承自ViewHolder需要把search_list里的控件连接到mzListHolder用id的方式连接，放入ViewHolder里
    class mzListHolder extends RecyclerView.ViewHolder {

        public mzListHolder(@NonNull View itemView) {
            super(itemView);
            mzCover = itemView.findViewById(R.id.mzListCover);
            mzName = itemView.findViewById(R.id.mzListName);
            mzSinger = itemView.findViewById(R.id.mzListSinger);
//            mzDownload = itemView.findViewById(R.id.mzListDownload);
            listView = itemView;
            mzListDownload = itemView.findViewById(R.id.mzListDownload);
            songItem = itemView.findViewById(R.id.each_songItem);
        }
    }


    //构造函数
    public RecyclerListAdapt(List<MusicInfo> musicInfoList) {
        this.musicInfoList = musicInfoList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LitePal.initialize(context);
        View musicView = View.inflate(parent.getContext(), R.layout.search_list, null);
        mzListHolder mzList = new mzListHolder(musicView);
//        音乐的“更多”选项按钮
        mzListDownload.setOnClickListener(this);
        return mzList;
    }

    private void initPopWindow(View v, int position) {
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
                    } else {

                        UserInfo userInfo = new UserInfo();
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
                        userInfo.setIlikename(userInfo.getIlikename() + "---" + musicInfoList.get(position).getMusicName());
//                    歌手
                        userInfo.setIlikesinger(userInfo.getIlikesinger() + "---" + musicInfoList.get(position).getMusicSinger());
//                    url
//                      userInfo.setIlikes(userInfo.getIlikesinger()+"---"+musicInfoList.get(position).getMusicSinger());
                        System.out.println("length:" + userInfo.getIlikesinger().split("---").length + "user:" + userInfo.getUserName());
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
        int pos = position;
        /**
         网络图片不能加载问题(可加载https不能加载http)：
         需在AndroidManifest.xml文件下添加安全协议
         **/
        if (musicInfoList.get(position).getBmpDraw() != null) {
            mzCover.setImageDrawable(musicInfoList.get(position).getBmpDraw());
        } else {
            System.out.println("null");
            Glide.with(listView).load(musicInfoList.get(position).getPageImg()).into(mzCover);
        }
        mzName.setText(musicInfoList.get(position).getMusicName());
        mzSinger.setText(musicInfoList.get(position).getMusicSinger());
        mzListDownload.setTag(position);


        songItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println(666);
                if (musicInfoList.get(pos).getMusicId() != null) {
                    MusicSearchByID(musicInfoList.get(pos).getMusicId(), pos);
                }else {
                    MusicLocal(musicInfoList.get(pos).getMusicPath(),pos);
                }

            }
        });
    }

    public void MusicLocal(String localPath,int position){
        boolean apIsActive;
        SharedPreferences sp = context.getSharedPreferences("apStatus", MODE_PRIVATE);
        apIsActive = sp.getBoolean("apActive", false);
        System.out.println("apIsActive-recyclerList " + apIsActive);
        System.out.println(localPath);
        Bundle bundle = new Bundle();
        bundle.putString("musicPath", localPath);

        if (apIsActive) {
            Message msg = AudioPlayer.musicSetHandler.obtainMessage();
            msg.setData(bundle);
            msg.what = 310;
            AudioPlayer.musicSetHandler.sendMessage(msg);

            Intent ap = new Intent(context, AudioPlayer.class);
            context.startActivity(ap);
        }else {
            Message msg = MainActivity.handler.obtainMessage();
            msg.setData(bundle);
            msg.what = 311;
            MainActivity.handler.sendMessage(msg);
        }
    }

    public void MusicSearchByID(String id, int position) {
//        String serverUrl = "http://172.17.115.69:3000/";
        String serverUrl = MainActivity.getApiMusicIP();

        //fullUrl最终访问的url
        String fullUrl = serverUrl + "song/url?id=" + id;
        System.out.println("请求的url=" + fullUrl);

        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
        Request request = new Request.Builder().url(fullUrl).method("GET", null).build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //4.请求加入调度，重写回调方法
        call.enqueue(new Callback() {
            //请求失败执行的方法
            @Override
            public void onFailure(Call call, IOException e) {
                //向handle发送消息
                Message msg = AudioPlayer.musicSetHandler.obtainMessage();
                msg.what = 301;
                AudioPlayer.musicSetHandler.sendMessage(msg);
            }

            //请求成功执行的方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Gson gson = new Gson();
                JSONObject responseJSON = gson.fromJson(body, JSONObject.class);
                JSONArray result = responseJSON.getJSONArray("data");
//                System.out.println(result);
                String url = null;
                //遍历搜索结果
                for (int i = 0; i < result.size(); i++) {
                    JSONObject song = result.getJSONObject(i);
//                    final JSONObject music = new JSONObject();
//                    final int id = song.getIntValue("id");//歌曲id
//                    music.put("musicId", id);
//            ids.append("," + id);
//                    music.put("songName", song.getString("name"));//歌曲名
//            music.put("mvId", song.get("mv"));// mv的id
//            final int dt = song.getIntValue("dt");
//            music.put("durationTime", dt);// 时长，单位毫秒
//                    final JSONObject al = song.getJSONObject("al");
//            music.put("albumName", al.getString("name"));// 专辑名称
//                    music.put("songPicUrl", al.getString("picUrl"));// 封面链接
//                    music.put("singer", song.getJSONArray("ar"));// 歌手
                    url = song.getString("url");
//                    music.put("MusicUrl", url);
//                    System.out.println(url);
//                    //初始化音乐搜索数据列表
//                    initMusicData(music);
                    musicInfoList.get(position).setMusicUrl(url);
                    System.out.println(musicInfoList.get(position).getMusicUrl());
                }

//                Intent ap = new Intent(context, AudioPlayer.class);
//                context.startActivity(ap);

                //向handle发送消息
                boolean apIsActive;
                SharedPreferences sp = context.getSharedPreferences("apStatus", MODE_PRIVATE);
                apIsActive = sp.getBoolean("apActive", false);
                System.out.println("apIsActive-searchList " + apIsActive);

                Bundle bundle = new Bundle();
                bundle.putString("musicUrl", url);
                bundle.putString("musicCover", musicInfoList.get(position).getPageImg());
                bundle.putString("musicSinger", musicInfoList.get(position).getMusicSinger());
                bundle.putString("musicName", musicInfoList.get(position).getMusicName());

                if (apIsActive) {
                    Message msg = AudioPlayer.musicSetHandler.obtainMessage();
                    msg.setData(bundle);
                    msg.what = 320;
                    AudioPlayer.musicSetHandler.sendMessage(msg);

                    Intent ap = new Intent(context, AudioPlayer.class);
                    context.startActivity(ap);
                }else {
                    Message msg = MainActivity.handler.obtainMessage();
                    msg.setData(bundle);
                    msg.what = 321;
                    MainActivity.handler.sendMessage(msg);
                }

//                Intent ap = new Intent(context, AudioPlayer.class);
//                context.startActivity(ap);
            }
        });
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

    public interface OnItemClickListener {
        void onClick(View view);
    }
}
