package com.ikunkun.kunmusic.views;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.ikunkun.kunmusic.App;
import com.ikunkun.kunmusic.LoginActivity;
import com.ikunkun.kunmusic.MainActivity;
import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.adapt.IlikeAdapter;
import com.ikunkun.kunmusic.comn.CommunityMessageInfo;
import com.ikunkun.kunmusic.comn.MusicInfo;
import com.ikunkun.kunmusic.comn.UserInfo;
import com.ikunkun.kunmusic.tools.MusicUtils;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;

import org.litepal.LitePal;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;


public class MineFragment extends Fragment implements View.OnClickListener {
    static TextView mine_name;
    static FragmentActivity context;
    List<UserInfo> userinfolist;
    private static String[] mznames;
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            System.out.println(mine_name);
            init(bundle.getString("username"));
//            mine_name.setText(bundle.getString("username"));
        }
    };

    public static void init(String str) {
        mine_name = context.findViewById(R.id.mine_name);
        LinearLayout Ilike = context.findViewById(R.id.mine_ilike);
        mine_name.setText(str);
    }

    @Nullable
//    ??????????????????
    public List<MusicInfo> list = new ArrayList<MusicInfo>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        LinearLayout mine_music = getActivity().findViewById(R.id.mine_music);
        TextView mine_musicnum = getActivity().findViewById(R.id.mine_musicnum);
//        ????????????????????????????????????
//        if(LitePal.findAll(MusicInfo.class).size()==0) {
//            LitePal.deleteAll(MusicInfo.class);
//            permissionsRequest();
//        }
//        ????????????????????????
        list = LitePal.findAll(MusicInfo.class);
        mine_musicnum.setText("  ???? " + LitePal.findAll(MusicInfo.class).size() + "???");
        //???????????????????????????????????????
        SharedPreferences sp = context.getSharedPreferences("localMusicListStatus", MODE_PRIVATE);
        boolean loIsAdd = sp.getBoolean("loMzActive", false);
        System.out.println("loIsAdd " + loIsAdd);
        if (list.size() != 0 && !loIsAdd) {
            App.curUserMusicList.addAll(list);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("loMzActive", true);
            editor.apply();
        }

//        ???????????????????????????
        mine_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                LocalMusicFragment localMusicFragment = new LocalMusicFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) list);
                localMusicFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.mainFragment, localMusicFragment, "localFragment");
                fragmentTransaction.commit();

//                Intent intent_to_local = new Intent(getActivity(), localAdapter.class);
//                intent_to_local.putExtra("list", (Serializable) list);
//                startActivity(intent_to_local);
            }
        });
//        ???????????????
        if (LoginActivity.tempuser.getUserName() != null) {
//            List<UserInfo> UserInfoList = LitePal.findAll(UserInfo.class);
            init(LoginActivity.tempuser.getUserName());
            TextView mine_ilikenum = getActivity().findViewById(R.id.mine_ilikenum);
            userinfolist = LitePal.where(" userName = ?", LoginActivity.tempuser.getUserName()).find(UserInfo.class);
            mznames = userinfolist.get(0).getIlikename().split("---");
            mine_ilikenum.setText("  ???? " + (mznames.length - 1) + "???");
            LinearLayout Ilike = getActivity().findViewById(R.id.mine_ilike);
            Ilike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_to_Ilike = new Intent(getActivity(), IlikeAdapter.class);
//                    intent_to_Ilike.putExtra("userlist", (Serializable) UserInfoList);
                    startActivity(intent_to_Ilike);
                    Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                }
            });
//            ???????????????
        } else {
            TextView mine_name = getActivity().findViewById(R.id.mine_name);
            TextView mine_id = getActivity().findViewById(R.id.mine_id);
            TextView mine_ilikenum = getActivity().findViewById(R.id.mine_ilikenum);
            mine_ilikenum.setText("\uD83C\uDFC0 ?????????");
            mine_id.setText("Kunmusic's ??????");
            mine_name.setText("????????????");
            mine_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);

                }
            });
            LinearLayout Ilike = getActivity().findViewById(R.id.mine_ilike);
            Ilike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "??????????????????", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
    }

    private void permissionsRequest() {

        PermissionX.init(this).permissions(
                        //????????????
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {
                    @Override
                    public void onExplainReason(ExplainScope scope, List<String> deniedList, boolean beforeRequest) {
                        scope.showRequestReasonDialog(deniedList, "???????????????????????????????????????????????????", "????????????");
                    }
                })
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList, "??????????????????????????????????????????????????????", "????????????");
                    }
                })
                .setDialogTintColor(R.color.white, R.color.praise_item)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            //????????????????????????
                            list = MusicUtils.getMusicData(requireActivity());
                            System.out.println("????????????????????????"+LitePal.findAll(MusicInfo.class).size());
                        } else {
//                            show("???????????????????????????" + deniedList);
                        }
                    }
                });
    }
}
