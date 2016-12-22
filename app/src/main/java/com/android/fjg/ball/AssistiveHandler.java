package com.android.fjg.ball;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by fujianguo on 16-12-21.
 */

public class AssistiveHandler {
    private static final String TAG = "AssistiveHandler";
    private static AssistiveHandler sInstance;

    private AccessibilityService mSpriteService;

    private AssistiveHandler() {
    }

    public static AssistiveHandler getInstance() {
        if (sInstance == null) {
            sInstance = new AssistiveHandler();
        }
        return sInstance;
    }

    public void setSpriteService(AccessibilityService service) {
        mSpriteService = service;
    }

    public void clickToHome() {
        try {
            if (mSpriteService == null || !isAccessibilitySettingsOn(QuickBallApplication.getInstance())) {
                performHome(QuickBallApplication.getInstance());
            } else {
                performHome();
            }
        } catch (Exception e) {
            performHome(QuickBallApplication.getInstance());
        }
    }

    public void clickToBack() {
        try {
            if (mSpriteService == null || !isAccessibilitySettingsOn(QuickBallApplication.getInstance())) {
                Log.e(TAG, "click back do nothing...");
                openAccessibilityServiceSettings(QuickBallApplication.getInstance());
            } else {
                performBack();
            }
        } catch (Exception e) {
            Log.e(TAG, "====>", e);
        }
    }

    public void clickToRecents() {
        try {
            if (mSpriteService == null || !isAccessibilitySettingsOn(QuickBallApplication.getInstance())) {
                performRecents();
            } else {
                performRecents2();
            }
        } catch (Exception e) {
            performRecents();
        }
    }

    public void clickToSettings() {
        preformSetting();
    }

    /** 打开辅助服务的设置*/
    public void openAccessibilityServiceSettings(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            Toast.makeText(context, "找到返回键辅助服务，打开即可", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 回到桌面
     */
    private void performHome(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 通过反射调用最近任务栏
     */
    private void performRecents() {
        Class serviceManagerClass;
        try {
            serviceManagerClass = Class.forName("android.os.ServiceManager");
            Method getService = serviceManagerClass.getMethod("getService",
                    String.class);
            IBinder retbinder = (IBinder) getService.invoke(
                    serviceManagerClass, "statusbar");
            Class statusBarClass = Class.forName(retbinder
                    .getInterfaceDescriptor());
            Object statusBarObject = statusBarClass.getClasses()[0].getMethod(
                    "asInterface", IBinder.class).invoke(null,
                    new Object[] { retbinder });
            Method clearAll = statusBarClass.getMethod("toggleRecentApps");
            clearAll.setAccessible(true);
            clearAll.invoke(statusBarObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 返回主界面事件*/
    private void performHome() {
        if(mSpriteService == null) {
            return;
        }
        mSpriteService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    /** 返回事件*/
    private void performBack() {
        if(mSpriteService == null) {
            return;
        }
        mSpriteService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    /** 最近任务事件*/
    private void performRecents2() {
        if(mSpriteService == null) {
            return;
        }
        mSpriteService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
    }

    private void preformSetting() {
        if(mSpriteService == null) {
            return;
        }
        mSpriteService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_QUICK_SETTINGS);
    }

    /**
     * 辅助服务是否存在
     * @param context
     * @return
     */
    public boolean isAssistiveServiceEnabled(Context context) {
        AccessibilityManager am = (AccessibilityManager) context
                .getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> runningServices = am
                .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (AccessibilityServiceInfo service : runningServices) {
            if (service.getId().startsWith(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否on
     * @param context
     * @return
     */
    public boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName() + "/" + SpriteAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    context.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
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
}
