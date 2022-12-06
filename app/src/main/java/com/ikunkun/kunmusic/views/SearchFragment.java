package com.ikunkun.kunmusic.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.ikunkun.kunmusic.MainActivity;
import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.adapt.RecyclerListAdapt;
import com.ikunkun.kunmusic.comn.MusicInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends Fragment {

    List<MusicInfo> musicInfoList = new ArrayList<>();  //音乐列表

    /**
     * 用handler接收okhttp传回的数据来初始化搜索列表UI
     */
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 100:
                    System.out.println("second" + musicInfoList.get(0).getPageImg());
                    initUI();
                    break;
                case 101:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_search, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //监听返回键
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_BACK) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentByTag("searchFragment");
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });

        //接收搜索关键词keyword
        Bundle bundle = getArguments();
//        Intent dataIntent = getIntent();
        String keyword = bundle.getString("keyword");
        System.out.println("keyword " + keyword);

        //调用API接口查询搜索音乐
        musicAPISearch(keyword);
    }

    public void initUI() {
        System.out.println("-----------------------------");
        for (MusicInfo musicInfo : musicInfoList) {
            System.out.println(musicInfo.toString());
        }
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerListAdapt recyclerListAdapt = new RecyclerListAdapt(musicInfoList,recyclerView);
//        recyclerListAdapt.setListener(new RecyclerListAdapt.OnItemClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println(66666);
//                Toast.makeText(SearchActivity.this, "6666", Toast.LENGTH_SHORT).show();
//            }
//        });
        recyclerView.setAdapter(recyclerListAdapt);
    }

    /**
     * 使用okhttp访问url接收返回值
     */
    public void musicAPISearch(String keyword) {
//        String serverUrl = "http://172.17.36.223:3000/";
        String serverUrl = MainActivity.getApiMusicIP();

        //fullUrl最终访问的url
        String fullUrl = serverUrl + "search?keywords=" + keyword;
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
                Message msg = handler.obtainMessage();
                msg.what = 101;
                handler.sendMessage(msg);
            }

            //请求成功执行的方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Gson gson = new Gson();
                JSONObject responseJSON = gson.fromJson(body, JSONObject.class);
                JSONArray result = responseJSON.getJSONObject("result").getJSONArray("songs");
                System.out.println(result);

                //遍历搜索结果
                for (int i = 0; i < result.size(); i++) {
                    JSONObject song = result.getJSONObject(i);
                    final JSONObject music = new JSONObject();
                    long id = song.getLongValue("id");//歌曲id
                    music.put("musicId", id);
//            ids.append("," + id);
                    music.put("songName", song.getString("name"));//歌曲名
//            music.put("mvId", song.get("mv"));// mv的id
//            final int dt = song.getIntValue("dt");
//            music.put("durationTime", dt);// 时长，单位毫秒
                    final JSONObject al = song.getJSONObject("al");
//            music.put("albumName", al.getString("name"));// 专辑名称
                    music.put("songPicUrl", al.getString("picUrl"));// 封面链接
                    music.put("singer", song.getJSONArray("ar"));// 歌手

                    //初始化音乐搜索数据列表
                    initMusicData(music);
                }

                //向handle发送消息
                Message msg = handler.obtainMessage();
                msg.what = 100;
                handler.sendMessage(msg);
            }
        });


//        // 处理返回结果
//        result.stream().map(s -> {
//            JSONObject song = (JSONObject) s;
//            final JSONObject music = new JSONObject();
//            final int id = song.getIntValue("id");//歌曲id
//            music.put("musicId", id);
////            ids.append("," + id);
//            music.put("songName", song.getString("name"));//歌曲名
////            music.put("mvId", song.get("mv"));// mv的id
////            final int dt = song.getIntValue("dt");
////            music.put("durationTime", dt);// 时长，单位毫秒
//            final JSONObject al = song.getJSONObject("al");
////            music.put("albumName", al.getString("name"));// 专辑名称
//            music.put("songPicUrl", al.getString("picUrl"));// 封面链接
//            music.put("singer", song.getJSONArray("ar"));// 歌手
//            return music;
//        }).forEach(music -> initMusicData(music));

    }

    /**
     * 初始化音乐搜索数据列表
     *
     * @param music
     */
    public void initMusicData(JSONObject music) {
        StringBuilder AllSinger = new StringBuilder();
        JSONArray singers = music.getJSONArray("singer");
        AllSinger.append(singers.getJSONObject(0).getString("name"));

        //调试用
        System.out.print(music.getString("songName") + " ");
        for (int i = 1; i < singers.size(); i++) {
            AllSinger.append("/" + singers.getJSONObject(i).getString("name"));
            System.out.print(singers.getJSONObject(i).getString("name") + " ");
        }
        System.out.println(music.get("musicId"));

        MusicInfo musicInfo = new MusicInfo();
        musicInfo.setMusicId(Long.toString(music.getLongValue("musicId")));
        musicInfo.setMusicName(music.getString("songName"));
        musicInfo.setPageImg(music.getString("songPicUrl"));
        musicInfo.setMusicSinger(AllSinger.toString());
        musicInfoList.add(musicInfo);
    }

}
