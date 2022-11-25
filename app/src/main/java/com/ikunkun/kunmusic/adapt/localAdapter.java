package com.ikunkun.kunmusic.adapt;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.comn.MusicInfo;

import java.util.List;

/**
 * 适配本地音乐界面
 */
public class localAdapter extends AppCompatActivity implements View.OnClickListener{
    List<MusicInfo> list;
    private androidx.recyclerview.widget.RecyclerView recyclerView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list =  (List<MusicInfo>) getIntent().getSerializableExtra("list");
        System.out.println("test3"+list.size());
        setContentView(R.layout.activity_search);
        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerListAdapt recyclerListAdapt = new RecyclerListAdapt(list);
        recyclerView.setAdapter(recyclerListAdapt);
    }
    @Override
    public void onClick(View view) {

    }
}
