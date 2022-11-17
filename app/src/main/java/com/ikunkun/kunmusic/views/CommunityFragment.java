package com.ikunkun.kunmusic.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ikunkun.kunmusic.LoginActivity;
import com.ikunkun.kunmusic.PostingActivity;
import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.comn.CommunityMessageInfo;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CommunityFragment extends Fragment implements View.OnClickListener{
//    Connector.getDatabase();
//    List<CommunityMessageInfo> CommunityMessageInfo = DataSupport.findAll(CommunityMessageInfo.class);
    private android.widget.ListView ListView;
    Random r = new Random();
    List<CommunityMessageInfo> messageList  = LitePal.findAll(CommunityMessageInfo.class);
    private static int[] image={R.drawable.default_ava,R.drawable.default_ava2,R.drawable.default_ava3,R.drawable.default_ava4};
//    private static String[] userName={"dynammor","用户1","用户2","用户3"};
//    private static String[] massage={"I'am KunKun's fans,I use KunMusic","用户1","用户2","用户3"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community,container,false);

        return view;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Collections.reverse (messageList);
        ListView=getActivity().findViewById(R.id.listview);
        MyBaseAdapter mAdapter= new MyBaseAdapter();
        ListView.setAdapter(mAdapter);
        FloatingActionButton fab =getActivity().findViewById(R.id.newitem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostingActivity.class) ;
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return messageList.size();
        }

        @Override
        public Object getItem(int i) {
            return messageList.get(i).getMessage();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            @SuppressLint("ViewHolder")
            View view=View.inflate(getActivity(),R.layout.community_list,null);
            TextView nameview=view.findViewById(R.id.dongtai_nickname);
            TextView content=view.findViewById(R.id.dongtai_content);
            TextView dongtai_tv_time=view.findViewById(R.id.dongtai_tv_time);
            ImageView dongtai_portrait=view.findViewById(R.id.dongtai_portrait);
            dongtai_tv_time.setText(messageList.get(i).getMessageTime());
            nameview.setText(messageList.get(i).getUserName());
            content.setText(messageList.get(i).getMessage());
            int temp = r.nextInt(3);
            dongtai_portrait.setImageResource(image[temp]);
            return view;
        }
    }
}
