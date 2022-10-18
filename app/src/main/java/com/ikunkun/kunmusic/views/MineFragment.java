package com.ikunkun.kunmusic.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.ikunkun.kunmusic.LoginActivity;
import com.ikunkun.kunmusic.MainActivity;
import com.ikunkun.kunmusic.R;

import org.jetbrains.annotations.NotNull;

public class MineFragment extends Fragment implements View.OnClickListener{
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button login=(Button)getActivity().findViewById(R.id.testlogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LoginActivity.class) ;
                startActivity(intent);
            }
        });

    }


    @Override
    public void onClick(View view) {
         view.getId();

    }
}
