package com.wmk.whitepoint.Activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.wmk.whitepoint.Adapter.MenuAdapter;
import com.wmk.whitepoint.AdminRecever;
import com.wmk.whitepoint.R;
import com.wmk.whitepoint.isOpen.Judge;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_MEDIA_PROJECTION = 18;
    private DevicePolicyManager policyManager;
    private ComponentName componentName;
    RelativeLayout rl;
    MenuAdapter menuAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        menuAdapter=new MenuAdapter(MainActivity.this);
        ListView ls1=(ListView)findViewById(R.id.LIST1);
        rl=new RelativeLayout(MainActivity.this);

        ls1.setAdapter(menuAdapter);
        ls1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i)
                {
                    case 0:
                    {
                        if (!Judge.isAccessibilitySettingsOn(MainActivity.this)) {
                            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            startActivity(intent);
                        }
                        break;
                    }
                    case 1:
                    {
                        if (!Judge.isActive(MainActivity.this)) {
                            policyManager = (DevicePolicyManager) MainActivity.this.getSystemService(MainActivity.this.DEVICE_POLICY_SERVICE);
                            //AdminReceiver 继承自 DeviceAdminReceiver
                            componentName = new ComponentName(MainActivity.this,AdminRecever.class);
                            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                            //权限列表
                            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                            //描述(additional explanation)
                            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, componentName);
                            // intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        break;
                    }
                    case 3:
                    {
                        Intent intent=new Intent();
                        intent.setClass(MainActivity.this,HelpActivity.class);
                        startActivity(intent);
                        break;
                    }
                }

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        menuAdapter.notifyDataSetChanged();
    }

}
