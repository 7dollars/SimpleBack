package com.wmk.whitepoint.isOpen;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.wmk.whitepoint.AdminRecever;
import com.wmk.whitepoint.Service.MyAccessibility;

/**
 * Created by wmk on 2017/3/6.
 */

public class Judge {

    public static boolean isAccessibilitySettingsOn(Context context) {
        String TAG="辅助";
        int accessibilityEnabled = 0;
        // TestService为对应的服务
        final String service = context.getPackageName() + "/" + MyAccessibility.class.getCanonicalName();
        Log.i(TAG, "service:" + service);
        // com.z.buildingaccessibilityservices/android.accessibilityservice.AccessibilityService
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            // com.z.buildingaccessibilityservices/com.z.buildingaccessibilityservices.TestService
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }
    public static boolean isActive(Context context) {
        DevicePolicyManager policyManager;
        ComponentName componentName;
        policyManager = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);

        //AdminReceiver 继承自 DeviceAdminReceiver
        componentName = new ComponentName(context, AdminRecever.class);

        boolean active = policyManager.isAdminActive(componentName);
        if (!active) {//若无权限
            return false;
        }
        return true;

    }
}
