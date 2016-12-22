package com.android.fjg.ball;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;

public class SpriteService extends Service {
    public static final String STARTFLOATVIEW = "com.android.fjg.ball.startFloatView";
    public static final String CLEARFLOATVIEW = "com.android.fjg.ball.clearFloatView";

    private TouchDotView mTouchDotView;
    private TouchPanelView mTouchPanelView;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("Sprite", "SpriteService onCreate");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        boolean isOn = PreferenceUtils.getBoolean(PreferenceUtils.FLOATVIEW);
        if (isOn) {
            if (mTouchPanelView != null)
                mTouchPanelView.changeXYParams();
            toggleFloatView();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("Sprite", "SpriteService onStartCommand");
        boolean isOn = PreferenceUtils.getBoolean(PreferenceUtils.FLOATVIEW);
        String action = intent == null ? "" : intent.getAction();
        switch (action) {
            case STARTFLOATVIEW:
                if (isOn)
                    toggleFloatView();
                break;
            case CLEARFLOATVIEW:
                if (mTouchDotView != null) {
                    mTouchDotView.removeFromWindow();
                }
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void togglePanelView() {
        if (mTouchPanelView == null) {
            mTouchPanelView = new TouchPanelView(this);
            mTouchPanelView.setIPanelViewClick(new TouchPanelView.IPanelViewClick() {
                @Override
                public void clickItem(int item) {
                    Intent intent;
                    switch (item) {
                        case TouchPanelView.IPanelViewClick.OUTSIDE:
                            toggleFloatView();
                            break;
                        case TouchPanelView.IPanelViewClick.BACK:
                            if (AssistiveHandler.getInstance().isAccessibilitySettingsOn(QuickBallApplication.getInstance())) {
                                AssistiveHandler.getInstance().clickToBack();
                            } else {
                                intent = new Intent(SpriteService.this, BackEventAlertActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                toggleFloatView();
                            }
                            break;
                        case TouchPanelView.IPanelViewClick.HOME:
                            intent = new Intent(SpriteService.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            toggleFloatView();
                            break;
                        case TouchPanelView.IPanelViewClick.LAUCHER:
                            AssistiveHandler.getInstance().clickToHome();
                            toggleFloatView();
                            break;
                        case TouchPanelView.IPanelViewClick.MENU:
//                            intent =  new Intent(Settings.ACTION_SETTINGS);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
                            AssistiveHandler.getInstance().clickToSettings();
                            toggleFloatView();
                            break;
                        case TouchPanelView.IPanelViewClick.RECENT:
                            AssistiveHandler.getInstance().clickToRecents();
                            toggleFloatView();
                            break;
                    }
                }
            });
        }
        mTouchDotView.removeFromWindow();
        mTouchPanelView.addToWindow();
    }

    private void toggleFloatView() {
        if (mTouchPanelView != null) {
            mTouchPanelView.removeFromWindow();
        }
        if (mTouchDotView == null) {
            mTouchDotView = new TouchDotView(this);
            mTouchDotView.setFloatViewClickListener(new TouchDotView.IFloatViewClick() {
                @Override
                public void onFloatViewClick() {
                    togglePanelView();
                }
            });
        }
        mTouchDotView.addToWindow();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTouchDotView != null) {
            mTouchDotView.removeFromWindow();
        }
    }
}
