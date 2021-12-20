package com.example.myfirstapp;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private Ball ball;
    private BlockMatrix blockField;
    GameScreenActivity parentActivity;
    private int currentTouchX;
    private int currentTouchY;
    private String currentTouchEvent;
    // Level
    private Level currentLevel;
    private Integer currentLevelNo;

    public GameView(Context context, Integer currentLevelNo) {
        super(context);
        this.currentLevelNo = currentLevelNo;
        // display Metric test
        parentActivity = (GameScreenActivity) context;
        getHolder().addCallback(this);




        // updates Canvas
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {



            currentLevel = new Level(parentActivity, currentLevelNo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        thread.setRunning(true);
        thread.start();
    }

    public void update() {
        currentLevel.updateData(currentTouchX, currentTouchY, currentTouchEvent);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            currentLevel.renderFrame(canvas);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        currentTouchX = (int) event.getX();
        currentTouchY = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentTouchEvent = "DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                currentTouchEvent = "MOVE";
                break;
            case MotionEvent.ACTION_UP:
                currentTouchEvent = "UP";
                break;
        }
        return true;
    }
}
