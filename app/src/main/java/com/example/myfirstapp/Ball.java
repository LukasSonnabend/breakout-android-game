package com.example.myfirstapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;

public class Ball {

    public double x = 0;
    public double y = 0;
    private Integer radius = 15;
    public double accelerationX = 0;
    public double accelerationY = 0;
    private Pair<Integer, Integer> screenSize;
    private Boolean shotTaken = false;
    public  Boolean hasFirstHit = false;
    private Paint paint = new Paint();

    public Ball(Pair<Integer, Integer> screenSize) {
        this.screenSize = screenSize;
    }

    public void draw(Canvas canvas) {
        paint.setColor(Color.parseColor("#CD5C5C"));
        if (accelerationX != 0 || accelerationY != 0)
            calcPosition();
        canvas.drawCircle((float) x, (float) y, radius, paint);
    }

    public void setAcceleration(double accX, double accY) {
        accelerationX = accX;
        accelerationY = accY;
    }

    public void collision(Boolean hitFromBottom, String hitDirection) {
        hasFirstHit = true;
        if (hitFromBottom && this.accelerationY > 0)
            this.accelerationY *= -1;
        else if (!hitFromBottom && this.accelerationY < 0)
            this.accelerationY *= -1;

        if (hitDirection.equals("left") && this.accelerationX > 0)
            this.accelerationX *= -1;
        else if (hitDirection.equals("right") && this.accelerationX < 0)
            this.accelerationX *= -1;
    }

    public void collision(String hitDirection, double expo) {
        // TODO: wenn hit auf Außenkante dann winkel spitzer

        this.accelerationY *= -1;
        if (hitDirection.equals("left") && this.accelerationX > 0) {
            this.accelerationY += 2;
            this.accelerationX *= -1;
        }
        else if (hitDirection.equals("right") && this.accelerationX < 0) {
            this.accelerationY += 2;
            this.accelerationX *= -1;
        }
    }
    public void calcPosition() {
        if (x <= 0) {
            x = 20;
            this.accelerationX = -accelerationX;
        } else if (x > screenSize.first) {
            //shot over
            hasFirstHit = true;
            this.accelerationX *= -1;
        }
        if (y <= 0) {
            y = 20;
            hasFirstHit = true;
            this.accelerationY = -accelerationY;
        } else if (y > screenSize.second) {
            //shot over
            this.accelerationY = 0;
            this.accelerationX = 0;
            shotTaken = false;
        }
        x += accelerationX;
        y = y - accelerationY;
    }

    public void setXY(int x, int y) {
        if (!shotTaken) {
            this.x = (float) x;
            this.y = (float) y;
        }
    }

    public void setShotTaken(Boolean status) {
        this.hasFirstHit = false;
        this.shotTaken = status;
    }

    public Boolean getShotTaken() {
        return this.shotTaken;
    }

    public Integer getRadius() { return this.radius;  }
}
