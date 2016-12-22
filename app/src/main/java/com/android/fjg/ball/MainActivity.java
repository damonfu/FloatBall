package com.android.fjg.ball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent = new Intent(this, SpriteService.class);
        intent.setAction(SpriteService.STARTFLOATVIEW);
        startService(intent);
    }
}
