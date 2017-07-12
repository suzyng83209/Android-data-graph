package lab1_205_12.uwaterloo.ca.lab1_205_12;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import static lab1_205_12.uwaterloo.ca.lab1_205_12.GameDirection.DOWN;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.GameDirection.LEFT;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.GameDirection.NO_MOVEMENT;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.GameDirection.RIGHT;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.GameDirection.UP;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.Lab1_205_12.boundaryMax;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.Lab1_205_12.boundaryMin;

/**
 * Created by Chuqian Li on 2017-06-30.
 */

public class GameLoopTask extends TimerTask {

    private Activity myActivity;
    private RelativeLayout myRL;
    private Context myContext;

    private boolean spawnBlock = true;
    private boolean isMoving = false;
    private boolean wasMoving = false;

    public List<GameBlock> gameBlocks;
    public int blockLength = (boundaryMax - boundaryMin) / 3;

    public GameDirection currentGameDirection = NO_MOVEMENT;

    public GameLoopTask(Activity myActivity, RelativeLayout myRL, Context myContext) {
        this.myActivity = myActivity;
        this.myRL = myRL;
        this.myContext = myContext;
        this.gameBlocks = new ArrayList<>();
    }

    private void createBlock(){
        this.spawnBlock = false;
        Log.d("game blocks", Integer.toString(gameBlocks.size()));

        List<int[]> freeBlocks = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int[] freeBlock = {boundaryMin + blockLength*i, boundaryMin + blockLength*j};
                boolean isFree = true;
                for (GameBlock gameBlock : gameBlocks) {
                    if (isFree && gameBlock.getCurrentCoordinates() == freeBlock) {
                        isFree = false;
                    }
                }
                if (isFree) {
                    freeBlocks.add(freeBlock);
                }
            }
        }

        int coordinatePicker = new Random().nextInt(16 - gameBlocks.size());
        int[] coordinates = freeBlocks.get(coordinatePicker);
        GameBlock newBlock = new GameBlock(this.myContext, coordinates);

        this.myRL.addView(newBlock);
        this.gameBlocks.add(newBlock);
    }

    private void createBlock(int coordX, int coordY){
        GameBlock newBlock = new GameBlock(this.myContext, coordX, coordY);
        this.myRL.addView(newBlock);
        this.gameBlocks.add(newBlock);
    }

    private int[] getTargetCoordinates(int[] currentCoordinates) {
        int currentX = currentCoordinates[0];
        int currentY = currentCoordinates[1];

        int blocksToLeft = 0;
        int blocksToRight = 0;
        int blocksAbove = 0;
        int blocksBelow = 0;
        for (GameBlock gameBlock : gameBlocks) {
            int blockX = gameBlock.getCurrentX();
            int blockY = gameBlock.getCurrentY();
            if (blockY == currentY) {
                if (blockX < currentX) {
                    blocksToLeft++;
                } else if (blockX > currentX) {
                    blocksToRight++;
                }
            } else if (blockX == currentX) {
                if (blockY < currentY) {
                    blocksAbove++;
                } else if (blockY > currentY) {
                    blocksBelow++;
                }
            }
        }

        int leftBound = boundaryMin + blocksToLeft*blockLength;
        int rightBound = boundaryMax - blocksToRight*blockLength;
        int upperBound = boundaryMin + blocksAbove*blockLength;
        int lowerBound = boundaryMax - blocksBelow*blockLength;

        switch (this.currentGameDirection) {
            case LEFT:
                return new int[]{leftBound, currentY};
            case RIGHT:
                return new int[]{rightBound, currentY};
            case UP:
                return new int[]{currentX, upperBound};
            case DOWN:
                return new int[]{currentX, lowerBound};
            default:
                return new int[]{currentX, currentY};
        }
    }

    public void setDirection(GameDirection newDirection) {
        if (!isMoving) {
            this.currentGameDirection = newDirection;
            for (GameBlock gameBlock : gameBlocks) {
                int[] targetCoordinates = getTargetCoordinates(gameBlock.getCurrentCoordinates());
                gameBlock.setBlockDirection(newDirection);
                gameBlock.setTargetCoordinates(targetCoordinates);
            }
        }
    }

    @Override
    public void run() {
        myActivity.runOnUiThread(() -> {
            if (this.gameBlocks.size() < 16 && this.spawnBlock) {
                this.createBlock();
            }
            wasMoving = isMoving;
            isMoving = false;

            for (GameBlock gameBlock : gameBlocks) {
                gameBlock.move();
                if (gameBlock.isMoving()) {
                    isMoving = true;
                }
            }

            this.spawnBlock = !isMoving && wasMoving;
        });
    }
}
