package com.wmk.whitepoint.View;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wmk.whitepoint.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wmk on 2017/3/3.
 */

public class BigFloatWindowView extends LinearLayout {
    public static int func=0;
    /**
     * 记录小悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录小悬浮窗的高度
     */
    public static int viewHeight;

    /**
     * 记录系统状态栏的高度
     */
    private static int statusBarHeight;

    /**
     * 用于更新小悬浮窗的位置
     */
    private WindowManager windowManager;

    /**
     * 小悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;

    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;

    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private float xInView;

    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */

    private float yInView;

    private TextView txt1;
    private Drawable drawable;
    private Resources resources;
    private LinearLayout frag1,frag2,frag3,frag4;
    public BigFloatWindowView(Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.floatwindow2, this);
        View view = findViewById(R.id.lin2);

        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        resources=context.getResources();

        frag1=(LinearLayout)findViewById(R.id.frag1);
        frag2=(LinearLayout)findViewById(R.id.frag2);
        frag3=(LinearLayout)findViewById(R.id.frag3);
        frag4=(LinearLayout)findViewById(R.id.frag4);

        txt1=(TextView) findViewById(R.id.textView8);
        drawable=resources.getDrawable(R.mipmap.ic_stay_current_portrait_grey600_36dp);
        txt1.setBackground(drawable);

        txt1=(TextView) findViewById(R.id.textView9);
        drawable=resources.getDrawable(R.mipmap.ic_home_grey600_36dp);
        txt1.setBackground(drawable);

        txt1=(TextView) findViewById(R.id.textView10);
        drawable=resources.getDrawable(R.mipmap.ic_screen_lock_portrait_grey600_36dp);
        txt1.setBackground(drawable);

        txt1=(TextView) findViewById(R.id.textView11);
        drawable=resources.getDrawable(R.mipmap.ic_not_interested_grey600_36dp);
        txt1.setBackground(drawable);

    }
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }
    public void setUsing(int before,int now)
    {
        switch(before)
        {
            case 0:
            {
                frag1.setBackgroundColor(Color.argb(0,0XAA,0XAA,0XAA));
                break;
            }
            case 1:
            {
                frag2.setBackgroundColor(Color.argb(0,0XAA,0XAA,0XAA));
                break;
            }
            case 2:
            {
                frag3.setBackgroundColor(Color.argb(0,0XAA,0XAA,0XAA));
                break;
            }
            case 3:
            {
                frag4.setBackgroundColor(Color.argb(0,0XAA,0XAA,0XAA));
                break;
            }
        }
        switch(now)
        {
            case 0:
            {
                frag1.setBackgroundColor(Color.argb(255,0XAA,0XAA,0XAA));
                break;
            }
            case 1:
            {
                frag2.setBackgroundColor(Color.argb(255,0XAA,0XAA,0XAA));
                break;
            }
            case 2:
            {
                frag3.setBackgroundColor(Color.argb(255,0XAA,0XAA,0XAA));
                break;
            }
            case 3:
            {
                frag4.setBackgroundColor(Color.argb(255,0XAA,0XAA,0XAA));
                break;
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

}
