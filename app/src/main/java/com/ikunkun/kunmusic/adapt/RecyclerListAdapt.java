package com.ikunkun.kunmusic.adapt;

import static android.content.Context.MODE_PRIVATE;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.ikunkun.kunmusic.App;
import com.ikunkun.kunmusic.AudioPlayer;
import com.ikunkun.kunmusic.MainActivity;
import com.ikunkun.kunmusic.LoginActivity;
import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.comn.MusicInfo;
import com.ikunkun.kunmusic.comn.UserInfo;
import com.ikunkun.kunmusic.service.MusicService;
import com.ikunkun.kunmusic.tools.DownloadUtil;

import org.litepal.LitePal;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
    private androidx.recyclerview.widget.RecyclerView recyclerView;

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        initPopWindow(view, position);
    }

    //??????Adapter??????????????????ViewHolder?????????????????????????????????????????????????????????
    //???????????????mzListHolder?????????ViewHolder?????????search_list?????????????????????mzListHolder???id????????????????????????ViewHolder???
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


    //????????????
    public RecyclerListAdapt(List<MusicInfo> musicInfoList
            , androidx.recyclerview.widget.RecyclerView recyclerView) {
        this.musicInfoList = musicInfoList;
        this.recyclerView = recyclerView;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LitePal.initialize(context);
        View musicView = View.inflate(parent.getContext(), R.layout.search_list, null);
        mzListHolder mzList = new mzListHolder(musicView);
//        ?????????????????????????????????
        mzListDownload.setOnClickListener(this);
        return mzList;
    }

    private void initPopWindow(View v, int position) {
        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.item_popip, null, false);
        Button btn_download = (Button) view.findViewById(R.id.btn_download);
        Button btn_like = (Button) view.findViewById(R.id.btn_like);
        Button btn_start = (Button) view.findViewById(R.id.btn_start);
        Button btn_nolike = (Button) view.findViewById(R.id.btn_nolike);
        Button btn_delet = (Button) view.findViewById(R.id.btn_delet);
        //1.????????????PopupWindow???????????????????????????View?????????
        final PopupWindow popWindow = new PopupWindow(view,
                700, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.setAnimationStyle(R.style.popwindow_anim_style);  //??????????????????
        //?????????????????????PopupWindow?????????PopupWindow????????????????????????????????????
        //???????????????????????????????????????PopupWindow????????????????????????????????????????????????
        //PopupWindow????????????????????????????????????????????????????????????????????????????????????
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // ??????????????????true?????????touch??????????????????
                // ????????? PopupWindow???onTouchEvent?????????????????????????????????????????????dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //??????popWindow???????????????????????????
        //??????popupWindow???????????????????????????????????????View???x??????????????????y???????????????
        popWindow.showAsDropDown(v, -610, 0);
        //??????popupWindow?????????????????????
/**
 * ????????????
 */
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "????????????...", Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, "??????????????????~", Toast.LENGTH_SHORT).show();
                MusicSearchByIDForDownload(musicInfoList.get(position).getMusicId(), position);
                popWindow.dismiss();
            }
        });

/**
 * ????????????
 */
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ????????????????????????
                if (LoginActivity.tempuser != null && LoginActivity.tempuser.getUserName() != null) {
                    String user = LoginActivity.tempuser.getUserName();
                    List<UserInfo> list = LitePal.where(" userName = ?", user).find(UserInfo.class);
                    int flag = 0;
//                    ????????????????????????
                    for (int i = 0; i < list.get(0).getIlikename().split("---").length; i++) {
                        if (list.get(0).getIlikename().split("---")[i].equals(musicInfoList.get(position).getMusicName()) &&
                                list.get(0).getIlikesinger().split("---")[i].equals(musicInfoList.get(position).getMusicSinger())) {
                            flag = 1;
                        }
                    }
                    if (flag == 1) {
                        Toast.makeText(context, "?????????????????????~", Toast.LENGTH_SHORT).show();
                    } else {
                        if(musicInfoList.get(position).getMusicId()==null){
                            UserInfo userInfo = new UserInfo();
                            userInfo.setUserName(list.get(0).getUserName());
                            userInfo.setUserPwd(list.get(0).getUserPwd());
                            userInfo.setUserId(list.get(0).getUserId());
                            userInfo.setIlikename(list.get(0).getIlikename());
                            userInfo.setIlikesinger(list.get(0).getIlikesinger());
                            userInfo.setIlikeurl(list.get(0).getIlikeurl());
                            userInfo.setILikeCoverUrl(list.get(0).getILikeCoverUrl());
//                    ??????????????????
                            list.get(0).delete();
                            System.out.println(musicInfoList.get(position).getMusicName() + "---" + musicInfoList.get(position).getMusicSinger());

//                    ??????
                            userInfo.setIlikename(userInfo.getIlikename() + "---" + musicInfoList.get(position).getMusicName());
//                    ??????
                            userInfo.setIlikesinger(userInfo.getIlikesinger() + "---" + musicInfoList.get(position).getMusicSinger());
//                    ??????
                            userInfo.setILikeCoverUrl(userInfo.getILikeCoverUrl() + "---" + musicInfoList.get(position).getPageImg());
                            System.out.println("list " + musicInfoList.get(position).getPageImg());

//                    url
//                userInfo.setIlikesinger(userInfo.getIlikeurl() + "---" + musicInfoList.get(position).getMusicUrl());
                            System.out.println("position " + position);
                            if (musicInfoList.get(position).getMusicPath() != null) {
                                System.out.println("like = " + musicInfoList.get(position).getMusicPath());
                                userInfo.setIlikeurl(userInfo.getIlikeurl() + "---" + musicInfoList.get(position).getMusicPath());
                            } else {
                                System.out.println("like = " + musicInfoList.get(position).getMusicUrl());
                                userInfo.setIlikeurl(userInfo.getIlikeurl() + "---" + musicInfoList.get(position).getMusicUrl());
                            }

//                            MusicInfo musicInfo = new MusicInfo();
//                            musicInfo.setMusicName(musicInfoList.get(position).getMusicName());
//                            musicInfo.setMusicSinger(musicInfoList.get(position).getMusicSinger());
//                            musicInfo.setMusicPath(musicInfoList.get(position).getMusicUrl());
//                            musicInfo.setPageImg(musicInfoList.get(position).getPageImg());
//                            App.curUserMusicList.add(musicInfo);

//                      userInfo.setIlikes(userInfo.getIlikesinger()+"---"+musicInfoList.get(position).getMusicSinger());
                            System.out.println("length:" + userInfo.getIlikesinger().split("---").length + "user:" + userInfo.getUserName());
//                        System.out.println("test3"+userInfo.getIlikename());
//                    ??????????????????
                            userInfo.save();
                        }else {
                            MusicSearchByIDForLike(musicInfoList.get(position).getMusicId(), list, position);
                        }

                        Toast.makeText(context, "????????????~", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "????????????~", Toast.LENGTH_SHORT).show();
                }

                popWindow.dismiss();
            }
        });
/**
 * ????????????
 */
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "???????????????...", Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * ??????????????????
         */
        btn_nolike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "???????????????...", Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * ????????????
         */
        btn_delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "???????????????...", Toast.LENGTH_SHORT).show();
            }
        });
    }



    //???????????????????????????????????????????????????(??????position)?????????
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int pos = position;
        /**
         ??????????????????????????????(?????????https????????????http)???
         ??????AndroidManifest.xml???????????????????????????
         **/
//        if (musicInfoList.get(position).getBmp_pic() != null) {
//            mzCover.setImageDrawable(getBitmap(musicInfoList.get(position).getBmp_pic()));
//        } else {
//            System.out.println("null");
//        }

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    Glide.with(listView).resumeRequests();
//                }else {
//                    Glide.with(listView).pauseRequests();
//                }
//            }
//        });
        Bitmap musicimage = null;
        if (musicInfoList.get(pos).getBase64() == null) {
            System.out.println("image is null");
        } else {
            musicimage = base64ToBitmap(musicInfoList.get(pos).getBase64());
        }
        if (musicimage != null) {
            Drawable bmpDraw = new BitmapDrawable(musicimage);
            Glide.with(listView).load(bmpDraw).into(mzCover);
//            mzCover.setImageBitmap(musicimage);
//             mzCover.setImageDrawable(bmpDraw);
        } else {
            Glide.with(listView).load(musicInfoList.get(pos).getPageImg()).into(mzCover);
        }
        if (musicInfoList.get(position).getMusicName() == null) {
            mzName.setText("????????????");
        } else {
            mzName.setText(musicInfoList.get(position).getMusicName());
        }
        if (musicInfoList.get(position).getMusicSinger() == null) {
            mzSinger.setText("????????????");
        } else {
            mzSinger.setText(musicInfoList.get(position).getMusicSinger());
        }
        mzListDownload.setTag(position);


        songItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println(666);
                if (musicInfoList.get(pos).getMusicId() != null) {
                    MusicSearchByID(musicInfoList.get(pos).getMusicId(), pos);
                } else {
                    MusicLocal(musicInfoList.get(pos).getMusicPath(), pos);
                }

            }
        });
    }

    public void MusicLocal(String localPath, int position) {
        boolean apIsActive;
        SharedPreferences sp = context.getSharedPreferences("apStatus", MODE_PRIVATE);
        apIsActive = sp.getBoolean("apActive", false);
        System.out.println("apIsActive-recyclerList " + apIsActive);
        System.out.println(localPath);
        Bundle bundle = new Bundle();
        bundle.putString("musicUrl", localPath);
        bundle.putString("musicCover", musicInfoList.get(position).getPageImg());
        bundle.putString("musicSinger", musicInfoList.get(position).getMusicSinger());
        bundle.putString("musicName", musicInfoList.get(position).getMusicName());
        bundle.putString("musicBase",musicInfoList.get(position).getBase64());
        System.out.println(bundle);

        if (apIsActive) {
            Message msg = AudioPlayer.musicSetHandler.obtainMessage();
            msg.setData(bundle);
            msg.what = 310;
            AudioPlayer.musicSetHandler.sendMessage(msg);

            Message msg2 = MainActivity.handler.obtainMessage();
            msg2.setData(bundle);
            msg2.what = 310;
            MainActivity.handler.sendMessage(msg2);

            Intent ap = new Intent(context, AudioPlayer.class);
            context.startActivity(ap);
        } else {
            Message msg = MainActivity.handler.obtainMessage();
            msg.setData(bundle);
            System.out.println(bundle);
            msg.what = 311;
            MainActivity.handler.sendMessage(msg);
        }
    }

    public void MusicSearchByID(String id, int position) {
//        String serverUrl = "http://172.17.115.69:3000/";
        String serverUrl = MainActivity.getApiMusicIP();

        //fullUrl???????????????url
        String fullUrl = serverUrl + "song/url?id=" + id;
        System.out.println("?????????url=" + fullUrl);

        //1.??????OkHttpClient??????
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.??????Request?????????????????????url????????????????????????,?????????????????????
        Request request = new Request.Builder().url(fullUrl).method("GET", null).build();
        //3.????????????call??????,????????????Request????????????
        Call call = okHttpClient.newCall(request);
        //4.???????????????????????????????????????
        call.enqueue(new Callback() {
            //???????????????????????????
            @Override
            public void onFailure(Call call, IOException e) {
                //???handle????????????
                Message msg = AudioPlayer.musicSetHandler.obtainMessage();
                msg.what = 301;
                AudioPlayer.musicSetHandler.sendMessage(msg);
            }

            //???????????????????????????
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Gson gson = new Gson();
                JSONObject responseJSON = gson.fromJson(body, JSONObject.class);
                JSONArray result = responseJSON.getJSONArray("data");
//                System.out.println(result);
                String url = null;
                //??????????????????
                for (int i = 0; i < result.size(); i++) {
                    JSONObject song = result.getJSONObject(i);
//                    final JSONObject music = new JSONObject();
//                    final int id = song.getIntValue("id");//??????id
//                    music.put("musicId", id);
//            ids.append("," + id);
//                    music.put("songName", song.getString("name"));//?????????
//            music.put("mvId", song.get("mv"));// mv???id
//            final int dt = song.getIntValue("dt");
//            music.put("durationTime", dt);// ?????????????????????
//                    final JSONObject al = song.getJSONObject("al");
//            music.put("albumName", al.getString("name"));// ????????????
//                    music.put("songPicUrl", al.getString("picUrl"));// ????????????
//                    music.put("singer", song.getJSONArray("ar"));// ??????
                    url = song.getString("url");
//                    music.put("MusicUrl", url);
//                    System.out.println(url);
//                    //?????????????????????????????????
//                    initMusicData(music);
                    musicInfoList.get(position).setMusicUrl(url);
                    System.out.println(musicInfoList.get(position).getMusicUrl());
                }

//                Intent ap = new Intent(context, AudioPlayer.class);
//                context.startActivity(ap);

                //???handle????????????
                boolean apIsActive;
                SharedPreferences sp = context.getSharedPreferences("apStatus", MODE_PRIVATE);
                apIsActive = sp.getBoolean("apActive", false);
                System.out.println("apIsActive-searchList " + apIsActive);

                Bundle bundle = new Bundle();
                bundle.putString("musicUrl", url);
                bundle.putString("musicCover", musicInfoList.get(position).getPageImg());
                bundle.putString("musicSinger", musicInfoList.get(position).getMusicSinger());
                bundle.putString("musicName", musicInfoList.get(position).getMusicName());
//                bundle.putString("base64",musicInfoList.get(position).getBase64());
                System.out.println(bundle);

                if (apIsActive) {
                    Message msg = AudioPlayer.musicSetHandler.obtainMessage();
                    msg.setData(bundle);
                    msg.what = 300;
                    AudioPlayer.musicSetHandler.sendMessage(msg);

                    Message msg2 = MainActivity.handler.obtainMessage();
                    msg2.setData(bundle);
                    msg2.what = 300;
                    MainActivity.handler.sendMessage(msg2);

                    Intent ap = new Intent(context, AudioPlayer.class);
                    context.startActivity(ap);
                } else {
                    Message msg = MainActivity.handler.obtainMessage();
                    msg.setData(bundle);
                    msg.what = 311;
                    MainActivity.handler.sendMessage(msg);
                }

//                Intent ap = new Intent(context, AudioPlayer.class);
//                context.startActivity(ap);
            }
        });
    }

    public void MusicSearchByIDForLike(String id, List<UserInfo> list, int position) {
//        String serverUrl = "http://172.17.115.69:3000/";
        String serverUrl = MainActivity.getApiMusicIP();

        //fullUrl???????????????url
        String fullUrl = serverUrl + "song/url?id=" + id;
        System.out.println("?????????url=" + fullUrl);

        //1.??????OkHttpClient??????
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.??????Request?????????????????????url????????????????????????,?????????????????????
        Request request = new Request.Builder().url(fullUrl).method("GET", null).build();
        //3.????????????call??????,????????????Request????????????
        Call call = okHttpClient.newCall(request);
        //4.???????????????????????????????????????
        call.enqueue(new Callback() {
            //???????????????????????????
            @Override
            public void onFailure(Call call, IOException e) {
                //???handle????????????
//                Message msg = AudioPlayer.musicSetHandler.obtainMessage();
//                msg.what = 301;
//                AudioPlayer.musicSetHandler.sendMessage(msg);
            }

            //???????????????????????????
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Gson gson = new Gson();
                JSONObject responseJSON = gson.fromJson(body, JSONObject.class);
                JSONArray result = responseJSON.getJSONArray("data");
//                System.out.println(result);
                String url = null;
                //??????????????????
                for (int i = 0; i < result.size(); i++) {
                    JSONObject song = result.getJSONObject(i);
                    url = song.getString("url");
                    musicInfoList.get(position).setMusicUrl(url);
                    System.out.println(musicInfoList.get(position).getMusicUrl());
                }

                UserInfo userInfo = new UserInfo();
                userInfo.setUserName(list.get(0).getUserName());
                userInfo.setUserPwd(list.get(0).getUserPwd());
                userInfo.setUserId(list.get(0).getUserId());
                userInfo.setIlikename(list.get(0).getIlikename());
                userInfo.setIlikesinger(list.get(0).getIlikesinger());
                userInfo.setIlikeurl(list.get(0).getIlikeurl());
                userInfo.setILikeCoverUrl(list.get(0).getILikeCoverUrl());
//                    ??????????????????
                list.get(0).delete();
                System.out.println(musicInfoList.get(position).getMusicName() + "---" + musicInfoList.get(position).getMusicSinger());

//                    ??????
                userInfo.setIlikename(userInfo.getIlikename() + "---" + musicInfoList.get(position).getMusicName());
//                    ??????
                userInfo.setIlikesinger(userInfo.getIlikesinger() + "---" + musicInfoList.get(position).getMusicSinger());
//                    ??????
                userInfo.setILikeCoverUrl(userInfo.getILikeCoverUrl() + "---" + musicInfoList.get(position).getPageImg());
                System.out.println("list " + musicInfoList.get(position).getPageImg());

//                    url
//                userInfo.setIlikesinger(userInfo.getIlikeurl() + "---" + musicInfoList.get(position).getMusicUrl());
                System.out.println("position " + position);
                if (musicInfoList.get(position).getMusicPath() != null) {
                    System.out.println("like = " + musicInfoList.get(position).getMusicPath());
                    userInfo.setIlikeurl(userInfo.getIlikeurl() + "---" + musicInfoList.get(position).getMusicPath());
                } else {
                    System.out.println("like = " + musicInfoList.get(position).getMusicUrl());
                    userInfo.setIlikeurl(userInfo.getIlikeurl() + "---" + musicInfoList.get(position).getMusicUrl());
                }

                MusicInfo musicInfo = new MusicInfo();
                musicInfo.setMusicName(musicInfoList.get(position).getMusicName());
                musicInfo.setMusicSinger(musicInfoList.get(position).getMusicSinger());
                musicInfo.setMusicPath(musicInfoList.get(position).getMusicUrl());
                musicInfo.setPageImg(musicInfoList.get(position).getPageImg());
                App.curUserMusicList.add(musicInfo);

//                      userInfo.setIlikes(userInfo.getIlikesinger()+"---"+musicInfoList.get(position).getMusicSinger());
                System.out.println("length:" + userInfo.getIlikesinger().split("---").length + "user:" + userInfo.getUserName());
//                        System.out.println("test3"+userInfo.getIlikename());
//                    ??????????????????
                userInfo.save();
//                Toast.makeText(context, "????????????~", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void MusicSearchByIDForDownload(String id, int position) {
//        String serverUrl = "http://172.17.115.69:3000/";
        String serverUrl = MainActivity.getApiMusicIP();

        //fullUrl???????????????url
        String fullUrl = serverUrl + "song/url?id=" + id;
        System.out.println("?????????url=" + fullUrl);

        //1.??????OkHttpClient??????
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.??????Request?????????????????????url????????????????????????,?????????????????????
        Request request = new Request.Builder().url(fullUrl).method("GET", null).build();
        //3.????????????call??????,????????????Request????????????
        Call call = okHttpClient.newCall(request);
        //4.???????????????????????????????????????
        call.enqueue(new Callback() {
            //???????????????????????????
            @Override
            public void onFailure(Call call, IOException e) {
                //???handle????????????
//                Message msg = AudioPlayer.musicSetHandler.obtainMessage();
//                msg.what = 301;
//                AudioPlayer.musicSetHandler.sendMessage(msg);
            }

            //???????????????????????????
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Gson gson = new Gson();
                JSONObject responseJSON = gson.fromJson(body, JSONObject.class);
                JSONArray result = responseJSON.getJSONArray("data");
//                System.out.println(result);
                String url = null;
                //??????????????????
                for (int i = 0; i < result.size(); i++) {
                    JSONObject song = result.getJSONObject(i);
                    url = song.getString("url");
                    musicInfoList.get(position).setMusicUrl(url);
                    System.out.println(musicInfoList.get(position).getMusicUrl());
                }

                /*@
                 * ???????????? url????????? filepateh??? ??????????????????  filename: ????????????????????????????????????????????????????????????
                 *
                 */
                String dirName = Environment.getExternalStorageDirectory().getPath() + "/Music/ikunMusic";
                System.out.println(dirName);
                String songName = musicInfoList.get(position).getMusicName();
                String singName = musicInfoList.get(position).getMusicSinger();
                downFile(url, dirName, songName + "-" + singName + ".mp3");
            }
        });
    }

    //bitmap??????
    public static Bitmap getBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);//???????????????????????????
    }

    public static byte[] getBytes(Bitmap bitmap) {
        //??????????????????????????????
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);//????????????
        return baos.toByteArray();//????????????????????????
    }

    /**
     * ????????????
     * mediaUri MP3????????????
     */
    private static Bitmap loadingCover(String mediaUri) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mediaUri);
        byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
        Bitmap bitmap = null;
        if (picture != null) {
            bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            Matrix matrix = new Matrix();
            matrix.setScale(0.5f, 0.5f);
            bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    @Override
    public int getItemViewType(int position) {
        // ?????????ItemView?????????????????????????????????RecyclerView???????????????ItemView??????????????????????????????
        return position;
    }

    //??????????????????
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

    private void downFile(String url, String filepath, String filename) {
        DownloadUtil.get().download(url, filepath, filename,
                new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(File file) {
                        //????????????
                        Log.i("LZ---", "onDownloadSuccess:....... ..................??????????????????..............................");
                        //??????????????????????????????
                    }

                    @Override
                    public void onDownloading(int progress) {
                        //?????????
                        Log.i("LZ---", "onDownloading:..............................???????????????................................");
                        System.out.println(progress);
                    }

                    @Override
                    public void onDownloadFailed(Exception e) {
                        //????????????????????????????????????
                        Log.e("lz---", "onDownloadFailed:..........................??????????????????...............................");
                        //?????????????????????????????????

                    }
                });
    }
}
