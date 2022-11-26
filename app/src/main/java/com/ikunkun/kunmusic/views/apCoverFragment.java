package com.ikunkun.kunmusic.views;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.ikunkun.kunmusic.R;

public class apCoverFragment extends Fragment implements View.OnClickListener {
    private static ObjectAnimator animator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ap_cover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView apCover = view.findViewById(R.id.apCover);
        animator = ObjectAnimator.ofFloat(apCover, "rotation", 0f, 360.0f);
        animator.setDuration(30000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
    }

    public static class controlAnimator {
        public void startAnimator() {
            animator.start();
        }

        public void resumeAnimator() {
            animator.resume();
        }

        public void pauseAnimator() {
            animator.pause();
        }

        public boolean isPausedAnimator() {
            return animator.isPaused();
        }
    }


    @Override
    public void onClick(View view) {

    }
}