package com.example.myfirstapp;

import android.graphics.Canvas;
import android.util.Pair;

import java.lang.reflect.Field;

public class BlockMatrix {

    private Integer fieldRows = 7;
    private Integer maxBlocksPerRow = 10;
    private Integer blockCount = 0;
    private Block[][] field = new Block[fieldRows][maxBlocksPerRow];
    GameScreenActivity parentActivity;
    Ball gameBall;

    private String level = "1111111111" +
            "1111111111" +
            "1100001111" +
            "1100011111" +
            "1110001111" +
            "1111000000" +
            "1111110000";


    public BlockMatrix(GameScreenActivity act, Pair<Integer, Integer> screenSize, Ball gameBall) {
        parentActivity = act;
        //loop set rows
        Integer xOrigin;
        Integer blockHeight = 40;
        Integer yOrigin = 10;
        this.gameBall = gameBall;


        Integer blockWidth = (int) Math.ceil((screenSize.first / 10));
        Integer blockGap = (int) Math.ceil(blockWidth * 0.2);

        blockWidth = blockWidth - blockGap;


        for (int i = 0; i < fieldRows; ++i) {
            xOrigin = 10;
            yOrigin = 10 + (blockHeight * (i + 1)) + (blockGap * i + 1);
            //loop set block in rows
            for (int j = 0; j < maxBlocksPerRow; ++j) {
                field[i][j] = new Block(xOrigin, yOrigin, blockHeight, blockWidth, level.charAt((maxBlocksPerRow * i) + j) == '1' ? false : true, this);
                if (level.charAt((maxBlocksPerRow * i) + j) == '1')
                    blockCount++;
                xOrigin += blockWidth + blockGap;
            }
        }
    }

    public void draw(Canvas canvas) {
        System.out.println("Drawing Matrix");
        for (int i = 0; i < 7; ++i) {
            for (int j = 0; j < 10; ++j) {
                field[i][j].draw(canvas);
            }
        }
    }

    public void setBallPosition(int x, int y) {
        for (int i = 0; i < 7; ++i) {
            for (int j = 0; j < 10; ++j) {
                field[i][j].hitDetection((int) gameBall.x, (int) gameBall.y);
            }
        }
    }

    public void decrementBlockCount() {
        this.blockCount--;
        this.gameBall.collision();
        if (this.blockCount == 0)
            parentActivity.openMenuActivity();
    }

    // switch um block type zu setzen


}

