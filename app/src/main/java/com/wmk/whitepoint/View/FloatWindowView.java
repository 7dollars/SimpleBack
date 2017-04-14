package com.wmk.whitepoint.View;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.os.AsyncTaskCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import com.wmk.whitepoint.AdminRecever;
import com.wmk.whitepoint.Service.MyAccessibility;
import com.wmk.whitepoint.MyPointManager;
import com.wmk.whitepoint.R;
import com.wmk.whitepoint.Activity.getPMActivity;
import com.wmk.whitepoint.screencapture.FileUtil;

/**
 * Created by wmk on 2017/2/26.
 */

public class FloatWindowView extends LinearLayout {
    public static Intent mResultData = null;
    private BigFloatWindowView bigWindow;
    private Context mContext;

    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;


    private ImageReader mImageReader;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private GestureDetector mGestureDetector;


    private int mScreenWidth;
    private int mScreenHeight;
    private int mScreenDensity;
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
    Vibrator vibrator;
    private boolean MoveFlag=false;
    private Runnable LongPressRunnable,alphaRunnable;
    private Handler handler;
    float screenWidth,firstGroup,secondGroup,thirdGroup;//划分区域的两条线
    private DevicePolicyManager policyManager;
    private ComponentName componentName;
    private TextView txtview;
    private LinearLayout frag1,frag2,frag3;
    private static int before=0;

    LinearLayout float2;
    public FloatWindowView(Context context) {

        super(context);
        mContext=context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Resources resources=context.getResources();

        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        createImageReader();


        screenWidth = windowManager.getDefaultDisplay().getWidth();
        firstGroup=screenWidth/4;
        secondGroup=2*firstGroup;
        thirdGroup=3*firstGroup;

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.floatwindow1, this);
        float2 = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.floatwindow2, null);
        View view = findViewById(R.id.lin);
        txtview=(TextView) findViewById(R.id.textView);
        frag1=(LinearLayout) float2.findViewById(R.id.frag1);
        frag2=(LinearLayout) float2.findViewById(R.id.frag2);
        frag3=(LinearLayout) float2.findViewById(R.id.frag3);

        Drawable drawable=resources.getDrawable(R.mipmap.ic_menu_grey600_36dp);


        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        handler=new Handler();

        Log.e("重建窗口","float初始化");
        LongPressRunnable=new Runnable() {
            @Override
            public void run() {
                MoveFlag=true;
                vibrator.vibrate(70);
            }
        };
        alphaRunnable=new Runnable() {
            @Override
            public void run() {
                for(int i=255;i>70;i-=15) {
                    txtview.getBackground().setAlpha(i);
                    try {
                        Thread.currentThread().sleep(50);
                    }
                    catch(Exception ex) {
                        Log.e(ex.toString(),"");
                    }
                }
            }

        };
        handler.postDelayed(alphaRunnable,3000);
       // percentView.setText(MyWindowManager.getUsedPercentValue(context));

        Intent intent1=new Intent();
        intent1.setClass(mContext,getPMActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                MoveFlag=false;
                handler.postDelayed(LongPressRunnable,500);
                handler.removeCallbacks(alphaRunnable);
                txtview.getBackground().setAlpha(255);
              //  LastTime=System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
               if(MoveFlag) {
                    xInScreen = event.getRawX();
                    yInScreen = event.getRawY() - getStatusBarHeight();
                    // 手指移动的时候更新小悬浮窗的位置
                    updateViewPosition();
                }
                else if(Math.abs(event.getRawY() - getStatusBarHeight()-yInScreen)>10||Math.abs(event.getRawX()-xInScreen)>10)
                {
                    handler.removeCallbacks(LongPressRunnable);
                    if(bigWindow==null) {
                        bigWindow = MyPointManager.createBigWindow(mContext);
                        Log.e("创建大窗口","创建大窗口");
                    }
                    //创建大窗口
                    else
                    {
                        switch(inWhitchGroup(event.getRawX()))
                        {
                            case 0:
                            {
                                bigWindow.setUsing(before,0);
                                before=0;
                                break;
                            }
                            case 1:
                            {
                                bigWindow.setUsing(before,1);
                                before=1;
                                break;
                            }
                            case 2:
                            {
                                bigWindow.setUsing(before,2);
                                before=2;
                                break;
                            }
                            case 3:
                            {
                                bigWindow.setUsing(before,3);
                                before=3;
                                break;
                            }
                            default:
                                break;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
                handler.postDelayed(alphaRunnable,3000);
                bigWindow.func=0;
                if (xDownInScreen == event.getRawX() && yDownInScreen == event.getRawY() - getStatusBarHeight()) {
                    handler.removeCallbacks(LongPressRunnable);
                    Log.e("button","back");
                    synchronized (this) {
                        MyAccessibility.signal = true;
                    }
                }
                if(bigWindow!=null)
                {
                    MyPointManager.removeBigWindow(mContext);
                    bigWindow=null;
                    Log.e("创建大窗口","移除");
                    execTask(inWhitchGroup(event.getRawX()));
                }
                break;
            default:
                break;
        }
        return true;
    }
    public int inWhitchGroup(float x)
    {
        if(x<firstGroup)
            return 0;
        else if(x>=firstGroup&&x<secondGroup)
            return 1;
        else if(x>=secondGroup&&x<thirdGroup)
            return 2;
        else
            return 3;

    }
    public void execTask(int GroupCode)
    {
        Log.e(String.valueOf(GroupCode),"移除");
        switch(GroupCode)
        {
            case 0:
            {

                Toast.makeText(mContext,"正在保存截图...",Toast.LENGTH_SHORT).show();
                startScreenShot();
                break;
            }
            case 1:
            {
                Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);

                mHomeIntent.addCategory(Intent.CATEGORY_HOME);
                mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                mContext.startActivity(mHomeIntent);
                break;
            }
            case 2:
            {
                lockScreen();
                break;
            }
            case 3:
            {
                break;
            }
            default:
                break;
        }
    }
    public void lockScreen()
    {
        policyManager = (DevicePolicyManager) mContext.getSystemService(mContext.DEVICE_POLICY_SERVICE);

        //AdminReceiver 继承自 DeviceAdminReceiver
        componentName = new ComponentName(mContext,AdminRecever.class);

        boolean active = policyManager.isAdminActive(componentName);
        if(!active){//若无权限
          Toast.makeText(mContext,"请开启锁屏权限",Toast.LENGTH_SHORT).show();
        }
        if (active) {
            policyManager.lockNow();//直接锁屏
        }
    }

    /**
     * 获取SDCard的目录路径功能
     * @return
     */

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
    }

    /**
     * 打开大悬浮窗，同时关闭小悬浮窗。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    private void startScreenShot() {

       // mFloatView.setVisibility(View.GONE);

        Log.e("1","1");
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            public void run() {
                //start virtual
                startVirtual();
            }
        }, 5);

        handler1.postDelayed(new Runnable() {
            public void run() {
                //capture the screen
                startCapture();

            }
        }, 30);
    }


    private void createImageReader() {

        Log.e("2","1");
        mImageReader = ImageReader.newInstance(mScreenWidth, mScreenHeight, PixelFormat.RGBA_8888, 1);

    }

    public void startVirtual() {
        if (mMediaProjection != null) {
            virtualDisplay();
        } else {
            setUpMediaProjection();
            virtualDisplay();
        }
        Log.e("3","1");
    }

    public void setUpMediaProjection() {
        Log.e("4","1");
        if (mResultData == null) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } else {
            mMediaProjection = getMediaProjectionManager().getMediaProjection(Activity.RESULT_OK, mResultData);
        }
    }

    private MediaProjectionManager getMediaProjectionManager() {

        return (MediaProjectionManager) mContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    private void virtualDisplay() {
        Log.e("5","1");
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                mScreenWidth, mScreenHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
    }

    private void startCapture() {
        Log.e("6","1");
        Image image = mImageReader.acquireLatestImage();

        if (image == null) {
            startScreenShot();
        } else {
            SaveTask mSaveTask = new SaveTask();
            AsyncTaskCompat.executeParallel(mSaveTask, image);
        }
    }

    public class SaveTask extends AsyncTask<Image, Void, Bitmap> {
        private String filename=null;
        @Override
        protected Bitmap doInBackground(Image... params) {

            if (params == null || params.length < 1 || params[0] == null) {

                return null;
            }
            Image image = params[0];

            Log.e("7","1");
            int width = image.getWidth();
            int height = image.getHeight();
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            //每个像素的间距
            int pixelStride = planes[0].getPixelStride();
            //总的间距
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            image.close();

            File fileImage = null;
            if (bitmap != null) {
                try {
                    filename=FileUtil.getScreenShotsName(mContext);
                    fileImage = new File(filename);
                    if (!fileImage.exists()) {
                        fileImage.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(fileImage);
                    if (out != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri = Uri.fromFile(fileImage);
                        media.setData(contentUri);
                        mContext.sendBroadcast(media);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    fileImage = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    fileImage = null;
                }
            }

            publishProgress();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tearDownMediaProjection();
                }
            });
            if (fileImage != null) {
                return bitmap;
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            //String filename=values[0].toString();
            Toast.makeText(mContext,"截图保存在"+filename.toString(),Toast.LENGTH_SHORT).show();
        }


    }


    private void tearDownMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

    private void stopVirtual() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
    }
}
