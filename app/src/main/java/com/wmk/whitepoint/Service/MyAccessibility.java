package com.wmk.whitepoint.Service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Vibrator;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wmk on 2017/2/26.
 */

public class MyAccessibility extends AccessibilityService {
    public static boolean signal=false;
    private Timer timer;
    //Vibrator vibrator;

    public MyAccessibility() {
        super();
        Log.d("aaa","aaa");
        if(timer==null)
        {
            timer=new Timer();
            timer.schedule(new RefreshTask(),0,100);
        }
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
    /*    int eventtype=accessibilityEvent.getEventType();
        if(eventtype== AccessibilityEvent.TYPE_VIEW_CLICKED)
        {
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
        else if(eventtype== 0x66)
        {
            Log.e("aaaaaaassfa","asfgasgas");
        }*/
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo info = getServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 500;
        setServiceInfo(info);
        super.onServiceConnected();
    }
    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            if(signal==true)
            {
                //vibrator.vibrate(30);
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                synchronized (this)
                {
                    signal=false;
                }
            }
        }
    }
}
