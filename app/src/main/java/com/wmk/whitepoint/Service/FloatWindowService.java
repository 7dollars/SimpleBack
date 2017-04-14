package com.wmk.whitepoint.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wmk.whitepoint.MyPointManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wmk on 2017/2/26.
 */

public class FloatWindowService extends Service {
    private Handler handler = new Handler();
    private Timer timer;
    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        handler.post(new Runnable() {
            @Override
            public void run() {
                MyPointManager.removeSmallWindow(getApplicationContext());
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(timer==null)
        {
            timer=new Timer();
            timer.schedule(new RefreshTask(),0,500);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            if ( !MyPointManager.isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("重建窗口","重建窗口");
                        MyPointManager.createSmallWindow(getApplicationContext());
                    }
                });
            }
        }
    }
}
