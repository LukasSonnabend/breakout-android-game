package com.example.myfirstapp;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.util.Pair;

public class BallLauncher {

    //top left Corner is Origin (x,y)
    private Pair<Integer, Integer> origin;
    private Integer width = 270;
    private Integer height = 15;
    private String fillColorString = "#CDFCFC";

    private Paint paint = new Paint();

    private Integer currentTouchX;
    private Integer currentTouchY;
    private Rect touchBox;
    private Level parent;

    private Integer xPosition;

    private Integer maxXCoord;


    //braucht für die Hitdetection eine "Touchbox"


    public BallLauncher(Pair<Integer, Integer> screenSize, Level parent) {
        xPosition = screenSize.first / 2 - width / 2;
        origin = new Pair<>(xPosition, screenSize.second - 400);
        this.parent = parent;
        maxXCoord = screenSize.first;
        touchBox = new Rect(origin.first, origin.second, origin.second + width, origin.second + 400);
    }

    public void draw(Canvas canvas) {
        origin = new Pair<>(xPosition, origin.second);
        touchBox = new Rect(origin.first, origin.second, origin.second + width, origin.second + 400);

        paint.setColor(Color.parseColor(fillColorString));

        if (currentTouchX.equals(-1) && currentTouchY.equals(-1)) {
            canvas.drawRect(calcRect(origin, width, height), paint);

        } else {
            //line from right startX, startY, stopX, stopY, paint
            paint.setStrokeWidth(10);

            PointF VectRightStart = new PointF(origin.first + width, origin.second);
            PointF VectRightEnd = new PointF(currentTouchX, currentTouchY + 30);

            PointF VectLeftStart = new PointF(origin.first, origin.second);
            PointF VectLeftEnd = new PointF(currentTouchX, currentTouchY);

            double angleRadians = bisectorAngleBetween2Lines(VectRightStart, VectRightEnd, VectLeftStart, VectLeftEnd);

            System.out.println(angleRadians);
            canvas.drawLine(currentTouchX,
                    currentTouchY + 30,
                    currentTouchX + 1000 * (float) Math.cos(Math.PI * angleRadians / 180),
                    currentTouchY + 30 + 1000 * (float) Math.sin(Math.PI * angleRadians / 180),
                    paint);

            //line from right
            canvas.drawLine(origin.first + width, origin.second, currentTouchX, currentTouchY + 30, paint);
            //line from left                                                //+ ball radius
            canvas.drawLine(origin.first, origin.second, currentTouchX, currentTouchY + 30, paint);
        }
    }

    //calc Hit with contains hits müssen nur gerechnet werden wenn block nicht von 4 blocks eingeschlossen ist
    public void touchDetection(int x, int y, String currentTouchEvent, Ball gameBall) {
        // R = ball radius#
        if (!gameBall.getShotTaken()) {
        int R = 30;

        int Xn = Math.max(touchBox.left, Math.min(x, touchBox.left + width));
        int Yn = Math.max(touchBox.top, Math.min(y, touchBox.bottom + height));

        int Dx = Xn - x;
        int Dy = Yn - y;
        if ((Dx * Dx + Dy * Dy) <= R * R) {
            fillColorString = "#39ff33";
            currentTouchX = x;
            currentTouchY = y;
            // erst schießen wenn released wird
            if (currentTouchEvent.equals("UP")) {
                Pair<Double, Double> speedPair = calcAcceleration();
                currentTouchY = -1;
                currentTouchX = -1;
                gameBall.setShotTaken(true);
                gameBall.setAcceleration(speedPair.first, speedPair.second);
            }

        } else {
            fillColorString = "#CDFCFC";
            currentTouchX = -1;
            currentTouchY = -1;
        }
        } else {
            movePaddle(x);

        }
    }

    /**
     * Calculate the bisector angle of two lines with two given points
     *
     * @param A1 First point first line
     * @param A2 Second point first line
     * @param B1 First point second line
     * @param B2 Second point second line
     * @return Angle  between two lines in degrees
     */
    private float bisectorAngleBetween2Lines(PointF A1, PointF A2, PointF B1, PointF B2) {

        float angle1 = (float) Math.atan2(A2.y - A1.y, A1.x - A2.x);
        float angle2 = (float) Math.atan2(B2.y - B1.y, B1.x - B2.x);
        float calculatedAngle = (float) Math.toDegrees(angle1 + angle2);
        if (calculatedAngle < 0) {
            calculatedAngle += 360;
        }
        if (calculatedAngle > angle1)
            calculatedAngle = 90 - calculatedAngle;


        System.out.println(calculatedAngle);

        return calculatedAngle;
    }

    private Pair<Double, Double> calcAcceleration() {
        Pair<Double, Double> vectRight = new Pair<Double, Double>((double) origin.first + width - currentTouchX, (double) origin.second - currentTouchY + 30);
        Pair<Double, Double> vectLeft = new Pair<Double, Double>((double) origin.first - currentTouchX, (double) origin.second - currentTouchY + 30);

        PointF VectRightStart = new PointF(origin.first + width, origin.second);
        PointF VectRightEnd = new PointF(currentTouchX, currentTouchY + 30);

        PointF VectLeftStart = new PointF(origin.first, origin.second);
        PointF VectLeftEnd = new PointF(currentTouchX, currentTouchY);

        double angleDegrees = bisectorAngleBetween2Lines(VectRightStart, VectRightEnd, VectLeftStart, VectLeftEnd);
        Integer multiplier = 40;
        Double xComponent = Math.cos(Math.PI * angleDegrees / 180) * multiplier;
        if (currentTouchX < origin.first)
            xComponent = Math.abs(xComponent);

        return new Pair<>(xComponent, -Math.sin(Math.PI * angleDegrees / 180) * multiplier);
    }

    private Rect calcRect(Pair<Integer, Integer> origin, Integer w, Integer h) {
        return new Rect(origin.first, origin.second, origin.first + width, origin.second + height);
    }


    private void movePaddle(int x) {
        int newPos = x -  width/2;
        if (newPos < 0 )
            newPos = 0;
        else if ((newPos + width) > maxXCoord )
            newPos = maxXCoord - width ;

        xPosition = newPos;
    }

    public void hitDetection(Ball gameBall) {
        if (!gameBall.hasFirstHit)
            return;
        // R = ball radius
        int R = gameBall.getRadius();
        // find nearest point of rectangle to circle
        int Xn = (int) Math.max(origin.first, Math.min(gameBall.x, origin.first + width));
        int Yn = (int)  Math.max(origin.second, Math.min(gameBall.y, origin.second + height));

        int Dx = (int) (Xn - gameBall.x);
        int Dy = (int) (Yn - gameBall.y);
        if ((Dx * Dx + Dy * Dy) <= R * R) {
            //if (gameBall.x > origin.first + width/4 && gameBall.x < (origin.first + width - (width/4)*3))
            /*{
                PointF VectLeftStart = new PointF(origin.first, origin.second);
                PointF VectLeftEnd = new PointF(origin.first+width, origin.second);
                // Ball Vector
                Pair<Double, Double> calcedAccel = getJumpAngle(gameBall, VectLeftStart, VectLeftEnd);
                gameBall.setAcceleration(-calcedAccel.first, Math.abs(calcedAccel.second));
            }*/
            if(gameBall.x < origin.first + width /4){
                // einfallswinekl zwischen linkem Dreieck und Ball
                //start unten am Paddle = x
                // ende ist 50px nach rechts 10px hoch
                PointF VectLeftStart = new PointF(origin.first, origin.second);
                PointF VectLeftEnd = new PointF(origin.first+50, origin.second-50);
                // Ball Vector
                Pair<Double, Double> calcedAccel = getJumpAngle(gameBall, VectLeftStart, VectLeftEnd);
                gameBall.setAcceleration(-calcedAccel.first, Math.abs(calcedAccel.second));
            }
            else if (gameBall.x > origin.first + width - width/4){
                // einfallswinekl zwischen linkem Dreieck und Ball
                //start unten am Paddle = x
                // ende ist 50px nach rechts 10px hoch
                // TODO: Der winkel ist vlt bissi spitz
                PointF VectRightStart = new PointF(origin.first+width, origin.second);
                PointF VectRightEnd = new PointF(origin.first+width-width/4, origin.second-50);
                // Ball Vector
                Pair<Double, Double> calcedAccel = getJumpAngle(gameBall, VectRightStart, VectRightEnd);
                gameBall.setAcceleration(calcedAccel.first, Math.abs(calcedAccel.second));
            } else {
                PointF VectLeftStart = new PointF(origin.first, origin.second);
                PointF VectLeftEnd = new PointF(origin.first + width, origin.second);
                // Ball Vector
                Pair<Double, Double> calcedAccel = getJumpAngle(gameBall, VectLeftStart, VectLeftEnd);
                gameBall.setAcceleration(-calcedAccel.first, Math.abs(calcedAccel.second));
            }




        }
    }
    // Berechnung von Absprungwinkel an Paddle enden
    private Pair<Double, Double> getJumpAngle(Ball gameBall, PointF vectLeftStart, PointF vectLeftEnd) {
        PointF BallStart = new PointF((float) gameBall.x, (float) gameBall.y);
        PointF BallMoveVect = new PointF((float) (gameBall.x + gameBall.accelerationX), (float) (gameBall.y + gameBall.accelerationY));
        double angleDegrees = angleBetween2Lines(vectLeftStart, vectLeftEnd, BallStart, BallMoveVect);

        Integer multiplier = 15;
        Double xComponent = Math.cos(Math.PI * angleDegrees / 180) * multiplier;
        if (currentTouchX < origin.first)
            xComponent = Math.abs(xComponent);

        return new Pair<>(xComponent, -Math.sin(Math.PI * angleDegrees / 180) * multiplier);
    }

    private float angleBetween2Lines(PointF A1, PointF A2, PointF B1, PointF B2) {

        float angle1 = (float) Math.atan2(A2.y - A1.y, A1.x - A2.x);
        float angle2 = (float) Math.atan2(B2.y - B1.y, B1.x - B2.x);
        float calculatedAngle = (float) Math.toDegrees(angle1 + angle2);
        if (calculatedAngle < 0) {
            calculatedAngle += 360;
        }
        return calculatedAngle;
    }

}

