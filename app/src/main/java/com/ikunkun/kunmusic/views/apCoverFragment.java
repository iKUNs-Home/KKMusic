package com.ikunkun.kunmusic.views;

import static com.ikunkun.kunmusic.AudioPlayer.apBlurBK;
import static com.ikunkun.kunmusic.adapt.RecyclerListAdapt.base64ToBitmap;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ikunkun.kunmusic.AudioPlayer;
import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.tools.ImageFilter;

public class apCoverFragment extends Fragment implements View.OnClickListener {
    private static ObjectAnimator animator;
    public static ImageView apCover;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("这里是apCoverFragment2");
        return inflater.inflate(R.layout.fragment_ap_cover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        System.out.println("这里是apCoverFragment1");

        apCover = view.findViewById(R.id.apCover);

        Bundle bundle = getArguments();

        String mzName = bundle.getString("musicName");
        String mzSinger = bundle.getString("musicSinger");
        String mzCover = bundle.getString("musicCover");
        String mzBase = bundle.getString("musicBase");


        AudioPlayer.apName.setText(mzName);
        AudioPlayer.apSinger.setText(mzSinger);

        if (mzCover == null) {
            if (mzBase == null) {
                apCover.setImageResource(R.drawable.cover1);
                Bitmap resource = BitmapFactory.decodeResource(getResources(),R.drawable.cover1);
                Bitmap BlurBackground = ImageFilter.blurBitmap(getContext(), resource, 25f);
                AudioPlayer.apBlurBK.setImageBitmap(BlurBackground);
            } else {
                System.out.println("base:" + mzBase);
                Bitmap resource = base64ToBitmap(mzBase);
                Bitmap BlurBackground = ImageFilter.blurBitmap(getContext(), resource, 25f);
                AudioPlayer.apBlurBK.setImageBitmap(BlurBackground);
                apCover.setImageBitmap(resource);
            }
//            Drawable drawable=new BitmapDrawable(resource);
//            coverControl.setApCoverDrawable(drawable);
        } else {
            Glide.with(getContext()).asBitmap().load(mzCover).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Bitmap BlurBackground = ImageFilter.blurBitmap(getContext(), resource, 25f);
                    apBlurBK.setImageBitmap(BlurBackground);
                    apCover.setImageBitmap(resource);
                }
            });
        }

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

        public boolean isStartAnimator() {
            return animator.isStarted();
        }

        public boolean isAnimatorNull() {
            if (animator == null) {
                return true;
            } else {
                return false;
            }
        }
    }


    @Override
    public void onClick(View view) {

    }

    public static class coverControl {
        public void setApCover(Bitmap resource) {
            apCover.setImageBitmap(resource);
        }

        public void setApCoverResource(int resource) {
            apCover.setImageResource(resource);
        }
    }

}