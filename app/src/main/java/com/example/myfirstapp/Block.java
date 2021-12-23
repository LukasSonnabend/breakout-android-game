package com.example.myfirstapp;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Pair;
import java.util.Vector;
import java.util.Random;


public class Block {

    //top left Corner is Origin (x,y)
    public Pair<Integer, Integer> origin;
    public Integer width = 20;
    public Integer height = 10;
    public String color;
    public Boolean show;
    public BlockMatrix parent;

    public Paint paint = new Paint();


    // Boolean ersetzen mit type of block/item etc
    public Block(Integer x, Integer y, Integer height, Integer width, Boolean isEmpty, BlockMatrix parent, String color) {
        origin = new Pair<>(x, y);
        this.color = color;
        this.height = height;
        this.width = width;
        this.show = !isEmpty;
        this.parent = parent;
    }

    public void draw(Canvas canvas) {
        if (show) {
            // Random rand = new Random();
            // int upperbound = 5;
            // Vector<Integer> myColours = new Vector<Integer>(4);
            // addColours(myColours);
            // paint.setColor(myColours.elementAt(rand.nextInt()));
            //paint.setColor(Color.parseColor("#CD5C5C"));
            paint.setColor(Color.parseColor(color));
            canvas.drawRect(calcRect(origin, width, height), paint);
        }
    }

    // public Vector<Integer> addColours(Vector<Integer> myColours) {
    //     myColours.add(Color.BLUE);
    //     myColours.add(Color.RED);
    //     myColours.add(Color.GRAY);
    //     myColours.add(Color.WHITE);
    //     myColours.add(Color.YELLOW);
    //     return myColours;
    // }


    // calc Hit with contains hits müssen nur gerechnet werden wenn block nicht von 4 blocks eingeschlossen ist
    public void hitDetection(int x, int y) {
        if (!show)
            return;
        // R = ball radius
        int R = 10;
        // find nearest point of rectangle to circle
        int Xn = Math.max(origin.first, Math.min(x, origin.first + width));
        int Yn = Math.max(origin.second, Math.min(y, origin.second + height));

        int Dx = Xn - x;
        int Dy = Yn - y;
        if ((Dx * Dx + Dy * Dy) <= R * R) {
            Boolean hitFromBottom = origin.second+this.height < (y+R) ;
            String hitDirection = "";

            if (x > origin.first && x < origin.first + width)
                hitDirection = "center";
            else if(x < origin.first)
                hitDirection = "left";
            else if (x > origin.first +  width)
                hitDirection = "right";

            registerHitWithParent();
            ballCollision(hitFromBottom, hitDirection);
        }
    }

    public void registerHitWithParent() {
        this.show = false;
        parent.decrementBlockCount();
    }

    public void ballCollision(Boolean hitFromBottom, String hitDirection) {
        parent.registerHitWithBall(hitFromBottom, hitDirection);
    }

    public Rect calcRect(Pair<Integer, Integer> origin, Integer w, Integer h) {
        return new Rect(origin.first, origin.second, origin.first + width, origin.second + height);
    }

}
