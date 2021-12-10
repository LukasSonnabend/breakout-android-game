package com.example.myfirstapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Pair;

public class Block {

    //top left Corner is Origin (x,y)
    private Pair<Integer, Integer> origin;
    private Integer width = 20;
    private Integer height = 10;
    private Boolean show;
    private BlockMatrix parent;

    private Paint paint = new Paint();

    // Boolean ersetzen mit type of block/item etc
    public Block(Integer x, Integer y, Integer height, Integer width, Boolean isEmpty, BlockMatrix parent) {
        origin = new Pair<>(x, y);
        this.height = height;
        this.width = width;
        this.show = !isEmpty;
        this.parent = parent;
    }

    public void draw(Canvas canvas) {
        if (show) {
            paint.setColor(Color.parseColor("#CD5C5C"));
            canvas.drawRect(calcRect(origin, width, height), paint);
        }
    }

    // calc Hit with contains hits müssen nur gerechnet werden wenn block nicht von 4 blocks eingeschlossen ist
    public void hitDetection(int x, int y) {
        if (!show)
            return;
        // R = ball radius
        int R = 30;
        // find nearest point of rectangel to circle
        int Xn = Math.max(origin.first, Math.min(x, origin.first + width));
        int Yn = Math.max(origin.second, Math.min(y, origin.second + height));

        int Dx = Xn - x;
        int Dy = Yn - y;
        if ((Dx * Dx + Dy * Dy) <= R * R) {
            this.show = false;
            parent.decrementBlockCount();
        }
    }

    private Rect calcRect(Pair<Integer, Integer> origin, Integer w, Integer h) {
        return new Rect(origin.first, origin.second, origin.first + width, origin.second + height);
    }

}
