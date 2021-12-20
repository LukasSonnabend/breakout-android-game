package com.example.myfirstapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GameScreenActivity extends AppCompatActivity {

    public TextView xPos;
    public TextView yPos;
    public TextView eventPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_game_screen);
        super.onCreate(savedInstanceState);
        //get User info
        Integer level = 2;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            level = extras.getInt("currentLevel");
            //The key argument here must match that used in the other activity
        }


        setContentView(new GameView(this, level));
    }

    public void openMenuActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}