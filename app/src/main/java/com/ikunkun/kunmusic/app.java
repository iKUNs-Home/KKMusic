package com.ikunkun.kunmusic;
import android.app.Application;

import org.litepal.LitePal;

    public class app  extends Application {
        @Override
        public void onCreate() {
            super.onCreate();
            LitePal.initialize(this);
        }
    }

