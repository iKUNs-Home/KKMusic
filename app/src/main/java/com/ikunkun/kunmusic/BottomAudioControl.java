package com.ikunkun.kunmusic;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.ikunkun.kunmusic.service.MusicService;


public class BottomAudioControl extends AppCompatActivity implements View.OnClickListener{
    private ImageButton mPlay;
    private MusicService.MusicControl musicControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_control_bottom);
        mPlay = findViewById(R.id.pcb_play);
        mPlay.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pcb_play:
                System.out.println("66666666666666");
                musicControl.play();
                if (musicControl.isPlaying()) {
                    mPlay.setBackgroundResource(R.drawable.play);
                } else {
                    mPlay.setBackgroundResource(R.drawable.pause);
                }
        }
    }
}