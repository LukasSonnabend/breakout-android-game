package com.example.myfirstapp;

import android.graphics.Canvas;
import android.util.Pair;

public class BlockMatrix {

    private Integer fieldRows = 7;
    private Integer maxBlocksPerRow = 10;
    private Integer blockCount = 0;
    private Block[][] field = new Block[fieldRows][maxBlocksPerRow];
    GameScreenActivity parentActivity;
    Ball gameBall;

    private String level = "1111111111" +
            "1111111111" +
            "0000000000" +
            "110000LLL0" +
            "1110000000" +
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

        if (this.blockCount == 0)
            parentActivity.openMenuActivity();
    }

    public void registerHitWithBall(Boolean hitFromBottom, String hitDirection) {
        this.gameBall.collision(hitFromBottom, hitDirection);
    }


    // switch um block type zu setzen


}

