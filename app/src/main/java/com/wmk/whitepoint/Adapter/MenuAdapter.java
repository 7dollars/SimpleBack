package com.wmk.whitepoint.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wmk.whitepoint.Service.FloatWindowService;
import com.wmk.whitepoint.MyPointManager;
import com.wmk.whitepoint.R;
import com.wmk.whitepoint.isOpen.Judge;

/**
 * Created by wmk on 2017/3/6.
 */

public class MenuAdapter extends BaseAdapter {
    private Context mContext;
    public MenuAdapter(Context context) {
        super();
        mContext=context;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.e("tuichu","123");
        ViewHolder viewHolder=null;
        if(view==null)
        {

            viewHolder=new ViewHolder();
            view=View.inflate(mContext, R.layout.meun,null);
            viewHolder.title=(TextView)view.findViewById(R.id.textView4);
            viewHolder.info=(TextView)view.findViewById(R.id.textView6);
            viewHolder.isOpen=(CheckBox)view.findViewById(R.id.checkBox);
            viewHolder.isOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox ckbox=(CheckBox)view;
                    if(ckbox.isChecked())
                    {
                        Intent intent = new Intent(mContext, FloatWindowService.class);
                        mContext.startService(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(mContext, FloatWindowService.class);
                        mContext.stopService(intent);
                    }
                }
            });

           /* viewHolder.isOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b) {
                        Handler handler=new Handler();
                        if(!MyPointManager.isWindowShowing())
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    MyPointManager.createSmallWindow(mContext);
                                }
                            });
                    }
                    else {
                        Handler handler=new Handler();
                        if(MyPointManager.isWindowShowing())
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    MyPointManager.removeSmallWindow(mContext);
                                }
                            });
                    }
                }
            });*/
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)view.getTag();
        }
        switch (i)
        {
            case 0:
            {
                viewHolder.title.setText("开启辅助权限");
                if(Judge.isAccessibilitySettingsOn(mContext)) {
                    viewHolder.info.setText("已开启");
                }
                else
                {
                    viewHolder.info.setText("未开启");
                }
                viewHolder.isOpen.setVisibility(View.INVISIBLE);
                viewHolder.info.setVisibility(View.VISIBLE);
                break;
            }
            case 1:
            {
                viewHolder.title.setText("开启锁屏权限");
                if(Judge.isActive(mContext)) {
                    viewHolder.info.setText("已开启");
                }
                else
                {
                    viewHolder.info.setText("未开启");
                }
                viewHolder.isOpen.setVisibility(View.INVISIBLE);
                viewHolder.info.setVisibility(View.VISIBLE);
                break;
            }
            case 2:
            {
                if(MyPointManager.isWindowShowing())
                {
                    viewHolder.isOpen.setChecked(true);
                }
                else
                {
                    viewHolder.isOpen.setChecked(false);
                }
                viewHolder.title.setText("开启/关闭圆点辅助");
                viewHolder.info.setText("未开启");
                viewHolder.info.setVisibility(View.INVISIBLE);
                viewHolder.isOpen.setVisibility(View.VISIBLE);
                break;
            }
            case 3:
            {
                viewHolder.title.setText("使用说明");
                viewHolder.info.setText("");
                viewHolder.isOpen.setVisibility(View.INVISIBLE);
                viewHolder.info.setVisibility(View.VISIBLE);
                break;
            }
        }
        return view;
    }
}
