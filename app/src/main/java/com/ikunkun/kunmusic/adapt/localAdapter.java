package com.ikunkun.kunmusic.adapt;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.comn.MusicInfo;

import org.litepal.LitePal;

import java.util.List;

/**
 * 适配本地音乐界面
 */
public class localAdapter extends AppCompatActivity implements View.OnClickListener{
    static List<MusicInfo> list;
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle =msg.getData();
            list= (List<MusicInfo>) bundle.getParcelableArrayList("locallist").get(0);
        }
    };
    private androidx.recyclerview.widget.RecyclerView recyclerView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerListAdapt recyclerListAdapt = new RecyclerListAdapt(list,recyclerView);
        recyclerView.setAdapter(recyclerListAdapt);
    }
    @Override
    public void onClick(View view) {

    }
}
