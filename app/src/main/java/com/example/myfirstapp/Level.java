package com.example.myfirstapp;

import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Pair;

public class Level {

    private Ball ball;
    private BlockMatrix blockField;
    private BallLauncher ballLauncher;
    private int touchX;
    private int touchY;
    private String currentTouchEvent;
    private Pair<Integer, Integer> screenSize;

    public Level(GameScreenActivity parentActivity) {
        DisplayMetrics displayMetrics = parentActivity.getResources().getDisplayMetrics();
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        screenSize = new Pair<>(displayWidth, displayHeight);

        ball = new Ball(screenSize);
        ballLauncher = new BallLauncher(screenSize, this);
        blockField = new BlockMatrix(parentActivity, screenSize, ball);
    }


    public void updateData(int touchX, int touchY, String currentTouchEvent) {

        touchX = touchX;
        touchY = touchY;

        this.currentTouchEvent = currentTouchEvent;
        if (currentTouchEvent != "UP" && currentTouchEvent != null) {
            ball.setXY(touchX, touchY);
        }
        ballLauncher.hitDetection(touchX, touchY, currentTouchEvent, ball);
        blockField.setBallPosition(touchX, touchY);

    }

    public void renderFrame(Canvas canvas) {
        ball.draw(canvas);
        ballLauncher.draw(canvas);
        blockField.draw(canvas);
    }


}
