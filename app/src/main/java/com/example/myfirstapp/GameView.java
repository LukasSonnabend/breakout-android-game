package com.example.myfirstapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
        // draw bitmap on canvas
        super.draw(canvas);
        // draw png to canvas
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);

        DisplayMetrics displayMetrics = parentActivity.getResources().getDisplayMetrics();
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;

        canvas.drawBitmap(getResizedBitmap(bitmap,displayWidth, displayHeight ), 0,0,null);

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

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


}
