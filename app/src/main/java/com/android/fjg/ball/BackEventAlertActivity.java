package com.android.fjg.ball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BackEventAlertActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_event_alert);
        findViewById(R.id.sure).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure:
                AssistiveHandler.getInstance().openAccessibilityServiceSettings(this);
            case R.id.cancel:
                finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, SpriteService.class);
        intent.setAction(SpriteService.CLEARFLOATVIEW);
        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(this, SpriteService.class);
        intent.setAction(SpriteService.STARTFLOATVIEW);
        startService(intent);
    }
}
