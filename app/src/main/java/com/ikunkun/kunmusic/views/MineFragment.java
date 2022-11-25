package com.ikunkun.kunmusic.views;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.ikunkun.kunmusic.LoginActivity;
import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.adapt.localAdapter;
import com.ikunkun.kunmusic.comn.MusicInfo;
import com.ikunkun.kunmusic.tools.MusicUtils;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;


public class MineFragment extends Fragment implements View.OnClickListener{
    @Nullable
//    本地音乐列表
    public List<MusicInfo> list=new ArrayList<MusicInfo>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayout mine_music = getActivity().findViewById(R.id.mine_music);
        TextView mine_musicnum=getActivity().findViewById(R.id.mine_musicnum);
//        申请权限，并扫描手机音乐
        permissionsRequest();
//        设置本地音乐数量
        mine_musicnum.setText("  🏀 "+list.size()+"首");
//        转调至本地音乐列表
        mine_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_to_local = new Intent(getActivity(), localAdapter.class);
                intent_to_local.putExtra("list", (Serializable) list);
                startActivity(intent_to_local);
            }
        });
//        账号已登录
        if(LoginActivity.tempuser.getUserName()!=null) {
            TextView mine_name=getActivity().findViewById(R.id.mine_name);
            mine_name.setText(LoginActivity.tempuser.getUserName());
            LinearLayout Ilike = getActivity().findViewById(R.id.mine_ilike);
            Ilike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "点击事件", Toast.LENGTH_SHORT).show();
                }
            });
//            未登录账号
        }else{
            TextView mine_name=getActivity().findViewById(R.id.mine_name);
            TextView mine_id=getActivity().findViewById(R.id.mine_id);
            TextView mine_ilikenum=getActivity().findViewById(R.id.mine_ilikenum);
            mine_ilikenum.setText("\uD83C\uDFC0 请登录");
            mine_id.setText("Kunmusic's 游客");
            mine_name.setText("点击登录");
            mine_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                }
            });
            LinearLayout Ilike = getActivity().findViewById(R.id.mine_ilike);
            Ilike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "请登录后查看", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
    public void onClick(View view) {
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
                            System.out.println("test:"+list.size());
                        } else {
//                            show("您拒绝了如下权限：" + deniedList);
                        }
                    }
                });
    }
}
