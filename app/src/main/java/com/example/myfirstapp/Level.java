package com.example.myfirstapp;

import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Level {

    private Ball ball;
    private BlockMatrix blockField;
    private BallLauncher ballLauncher;
    private String currentTouchEvent;
    private Pair<Integer, Integer> screenSize;

    public Level(GameScreenActivity parentActivity, Integer level) throws IOException {
        DisplayMetrics displayMetrics = parentActivity.getResources().getDisplayMetrics();
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        screenSize = new Pair<>(displayWidth, displayHeight);
        // load level String



        // TODO: move an app start?
        // read file from asset manager
        Scanner scanner = new Scanner(parentActivity.getAssets().open("info.txt"));

        // hardcoded level zahl
        List<String> lvls = new ArrayList<String>();

        for (int i = 0; i < 3; i++) {
            String levelString = scanner.useDelimiter("\n").next();
            lvls.add(levelString);
        }

        scanner.close();


        ball = new Ball(screenSize);
        ballLauncher = new BallLauncher(screenSize, this);
        blockField = new BlockMatrix(parentActivity, screenSize, lvls.get(level), ball);
    }


    public void updateData(int touchX, int touchY, String currentTouchEvent) {

        touchX = touchX;
        touchY = touchY;

        this.currentTouchEvent = currentTouchEvent;
        if (currentTouchEvent != "UP" && currentTouchEvent != null) {
            ball.setXY(touchX, touchY);
        }
        ballLauncher.hitDetection(ball);
        ballLauncher.touchDetection(touchX, touchY, currentTouchEvent, ball);
        blockField.setBallPosition(touchX, touchY);

    }

    public void renderFrame(Canvas canvas) {
        ball.draw(canvas);
        ballLauncher.draw(canvas);
        blockField.draw(canvas);
    }


}
