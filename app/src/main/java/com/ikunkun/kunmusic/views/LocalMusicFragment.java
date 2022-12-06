package com.ikunkun.kunmusic.views;

import android.Manifest;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ikunkun.kunmusic.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ikunkun.kunmusic.App;
import com.ikunkun.kunmusic.MainActivity;
import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.adapt.RecyclerListAdapt;
import com.ikunkun.kunmusic.comn.MusicInfo;
import com.ikunkun.kunmusic.tools.MusicUtils;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;

import org.litepal.LitePal;

import java.util.List;

public class LocalMusicFragment extends Fragment {

    List<MusicInfo> list;
    private androidx.recyclerview.widget.RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_local, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        list = LitePal.findAll(MusicInfo.class);
        recyclerView = view.findViewById(R.id.local_recy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerListAdapt recyclerListAdapt = new RecyclerListAdapt(list,recyclerView);
        recyclerView.setAdapter(recyclerListAdapt);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_BACK) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentByTag("localFragment");
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });
//        刷新按钮
        FloatingActionButton fab = getActivity().findViewById(R.id.local_Refresh);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        new MaterialDialog.Builder(getActivity())
//                                .title("正在刷新")
//                                .show();
//                    }
//                }).run();
                LitePal.deleteAll(MusicInfo.class);
//                LoadingDialog ld = new LoadingDialog(this);
//                ld.setLoadingText("加载中")
//                        .setSuccessText("加载成功")//显示加载成功时的文字
//                        //.setFailedText("加载失败")
//                        .setInterceptBack(intercept_back_event)
//                        .setLoadSpeed(speed)
//                        .setRepeatCount(repeatTime)
//                        .setDrawColor()
//                        .show();

                Toast.makeText(getContext(), "正在扫描....", Toast.LENGTH_SHORT).show();
                final int[] flag = {0};
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        permissionsRequest();
                        flag[0] =1;
                    }
                });
                thread.run();
                list = LitePal.findAll(MusicInfo.class);
                while(true) {
                    System.out.println("wait.....");
                    if(flag[0]==1) {
                        RecyclerListAdapt recyclerListAdapt = new RecyclerListAdapt(list,recyclerView);
                        recyclerView.setAdapter(recyclerListAdapt);
                        Toast.makeText(getContext(), "扫描完成", Toast.LENGTH_SHORT).show();
                        System.out.println("扫描完成");

                        break;
                    }
                }
            }
        });
    }
    private void permissionsRequest() {

        PermissionX.init(this).permissions(
                        //写入文件
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {
                    @Override
                    public void onExplainReason(ExplainScope scope, List<String> deniedList, boolean beforeRequest) {
                        scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "我已明白");
                    }
                })
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白");
                    }
                })
                .setDialogTintColor(R.color.white, R.color.praise_item)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            //通过后的业务逻辑
                            list = MusicUtils.getMusicData(requireActivity());
                            //把本地音乐全部添加到总列表
                            System.out.println("当前本地音乐数量"+LitePal.findAll(MusicInfo.class).size());
                            SharedPreferences sp = getActivity().getSharedPreferences("localMusicListStatus", MODE_PRIVATE);
                            boolean loIsAdd = sp.getBoolean("loMzActive", false);
                            System.out.println("loIsAdd " + loIsAdd);
                            if (!loIsAdd) {
                                System.out.println(list.size());
                                App.curUserMusicList.addAll(list);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putBoolean("loMzActive", true);
                                editor.apply();
                            }
                        } else {
//                            show("您拒绝了如下权限：" + deniedList);
                        }
                    }
                });
    }
}
