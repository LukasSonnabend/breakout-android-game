package com.example.myfirstapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Pair;

import java.util.concurrent.locks.Lock;

public class LockedBlock extends Block{

    private boolean locked = true;
    public LockedBlock(Integer x, Integer y, Integer height, Integer width, Boolean isEmpty, BlockMatrix parent) {
        super(x, y, height,width, isEmpty, parent);

    }

    public void draw(Canvas canvas) {
        if (show) {
            paint.setColor(Color.parseColor("#33cccc"));
            paint.setTextSize(height-10);
            canvas.drawRect(calcRect(origin, width, height), paint);
            if (locked)
                canvas.drawText("\uD83D\uDD12", origin.first+10, origin.second+height-20, paint);
        }
    }

    @Override
    public void registerHitWithParent() {
        if (!locked){
            this.show = false;
            parent.decrementBlockCount();
        }

    }

    @Override
    public void ballCollision(Boolean hitFromBottom, String hitDirection) {
        this.locked = false;
        parent.registerHitWithBall(hitFromBottom, hitDirection);
    }

}
