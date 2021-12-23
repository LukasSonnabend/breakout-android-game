package com.example.myfirstapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;

public class BlockMatrix {
    private Pair<Integer, Integer> screenSize;
    private Integer fieldRows = 7;
    private Integer maxBlocksPerRow = 10;
    private Integer blockCount = 0;
    private Block[][] field;
    GameScreenActivity parentActivity;
    Ball gameBall;

    private String level;
    private Integer score;

    public BlockMatrix(GameScreenActivity act, Pair<Integer, Integer> screenSize, String levelString, Ball gameBall) {
        parentActivity = act;
        this.screenSize = screenSize;
        level = levelString;
        //loop set rows
        fieldRows = levelString.length() / maxBlocksPerRow;
        field = new Block[fieldRows][maxBlocksPerRow];
        score = 0;
        Integer xOrigin;
        Integer blockHeight = 40;
        Integer yOrigin = 10;
        this.gameBall = gameBall;


        Integer blockWidth = (int) Math.ceil((screenSize.first / 10));
        Integer blockGap = (int) Math.ceil(blockWidth * 0.1);

        blockWidth = blockWidth - blockGap;


        for (int i = 0; i < fieldRows; ++i) {
            xOrigin = 10;
            yOrigin = 10 + (blockHeight * (i + 1)) + (blockGap * i + 1);
            //loop set block in rows
            for (int j = 0; j < maxBlocksPerRow; ++j) {
                Block blk;
                switch (level.charAt((maxBlocksPerRow * i) + j)) {
                    case '1':
                        blk = new Block(xOrigin, yOrigin, blockHeight, blockWidth, false, this);
                        break;
                    case 'L':
                        blk = new LockedBlock(xOrigin, yOrigin, blockHeight, blockWidth, false, this);
                        break;
                    default:
                        blk = new Block(xOrigin, yOrigin, blockHeight, blockWidth, true, this);
                }

                field[i][j] = blk;
                if (level.charAt((maxBlocksPerRow * i) + j) != '0')
                    blockCount++;
                xOrigin += blockWidth + blockGap;
            }
        }
    }

    public void draw(Canvas canvas) {
        System.out.println("Drawing Matrix");
        Paint paint = new Paint();
        /*paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);
*/
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        canvas.drawText("Score: " + this.score, screenSize.first-300, 40, paint);


        for (int i = 0; i < fieldRows; ++i) {
            for (int j = 0; j < 10; ++j) {
                field[i][j].draw(canvas);
            }
        }
    }

    public void setBallPosition(int x, int y) {
        for (int i = 0; i < fieldRows; ++i) {
            for (int j = 0; j < 10; ++j) {
                field[i][j].hitDetection((int) gameBall.x, (int) gameBall.y);
            }
        }
    }

    public void decrementBlockCount() {
        this.blockCount--;
        this.score += 100;
        if (this.blockCount == 0)
            parentActivity.openMenuActivity();
    }

    public void registerHitWithBall(Boolean hitFromBottom, String hitDirection) {
        this.gameBall.collision(hitFromBottom, hitDirection);
    }

    public void setLevel(String levelString) {
        this.level = levelString;
    }


    // switch um block type zu setzen


}

