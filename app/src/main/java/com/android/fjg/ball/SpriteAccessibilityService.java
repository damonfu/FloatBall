package com.android.fjg.ball;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by fujianguo on 16-12-21.
 */

public class SpriteAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.e("Sprite", "onInterrupt");
        AssistiveHandler.getInstance().setSpriteService(this);
    }

    @Override
    public void onInterrupt() {
        Log.e("Sprite", "onInterrupt");
        AssistiveHandler.getInstance().setSpriteService(null);
    }
}
