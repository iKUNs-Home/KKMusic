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
//        ????????????
        FloatingActionButton fab = getActivity().findViewById(R.id.local_Refresh);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        new MaterialDialog.Builder(getActivity())
//                                .title("????????????")
//                                .show();
//                    }
//                }).run();
                LitePal.deleteAll(MusicInfo.class);
//                LoadingDialog ld = new LoadingDialog(this);
//                ld.setLoadingText("?????????")
//                        .setSuccessText("????????????")//??????????????????????????????
//                        //.setFailedText("????????????")
//                        .setInterceptBack(intercept_back_event)
//                        .setLoadSpeed(speed)
//                        .setRepeatCount(repeatTime)
//                        .setDrawColor()
//                        .show();

                Toast.makeText(getContext(), "????????????....", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "????????????", Toast.LENGTH_SHORT).show();
                        System.out.println("????????????");

                        break;
                    }
                }
            }
        });
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
                            //???????????????????????????????????????
                            System.out.println("????????????????????????"+LitePal.findAll(MusicInfo.class).size());
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
//                            show("???????????????????????????" + deniedList);
                        }
                    }
                });
    }
}
