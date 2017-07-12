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
    private boolean justSpawned = false;

    private List<GameBlock> gameBlocks;
    private int[][] gameBoard;
    public int blockLength = (boundaryMax - boundaryMin) / 3;

    public GameDirection currentGameDirection = NO_MOVEMENT;

    public GameLoopTask(Activity myActivity, RelativeLayout myRL, Context myContext) {
        this.myActivity = myActivity;
        this.myRL = myRL;
        this.myContext = myContext;
        this.gameBlocks = new ArrayList<>();
        this.gameBoard = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                gameBoard[i][j] = 0;
            }
        }
        createBlock();
    }

    private void createBlock(){
        this.spawnBlock = false;

        Random random = new Random();

        int rowNum = random.nextInt(3);
        int columnNum = random.nextInt(3);

        while (gameBoard[rowNum][columnNum] != 0) {
            rowNum = random.nextInt(3);
            columnNum = random.nextInt(3);
        }

        int coordX = boundaryMin + rowNum*blockLength;
        int coordY = boundaryMax + columnNum*blockLength;

        GameBlock newBlock = new GameBlock(this.myContext, coordX, coordY);
        gameBoard[rowNum][columnNum] = newBlock.getNumber();

        this.gameBlocks.add(newBlock);
        this.myRL.addView(newBlock);
        this.justSpawned = true;
    }

    private int getGridNum(int coordinate) {
        boundaryMax - bou
    }

    private void createBlock(int coordX, int coordY){
        GameBlock newBlock = new GameBlock(this.myContext, coordX, coordY);
        this.myRL.addView(newBlock);
        this.gameBlocks.add(newBlock);
    }

    private int[] getTargetCoordinates(GameBlock currentBlock, GameDirection newDirection) {
        int currentX = currentBlock.getCurrentX();
        int currentY = currentBlock.getCurrentY();

        int blocksToLeft = 0;
        int blocksToRight = 0;
        int blocksAbove = 0;
        int blocksBelow = 0;
        for (GameBlock gameBlock : gameBlocks) {
            int blockX = gameBlock.getCurrentX();
            int blockY = gameBlock.getCurrentY();

            boolean sameBlock = blockX == currentX && blockY == currentY;

            if(!sameBlock) {
                if (blockY == currentY) {
                    if (blockX < currentX) {
                        blocksToLeft++;
                    } else {
                        blocksToRight++;
                    }
                } else if (blockX == currentX) {
                    if (blockY < currentY) {
                        blocksAbove++;
                    } else {
                        blocksBelow++;
                    }
                }
            }
        }

//        Log.d("blocks", currentBlock.getNumber()
//                + " " + Integer.toString(blocksToLeft)
//                + " " + Integer.toString(blocksToRight)
//                + " " + Integer.toString(blocksAbove)
//                + " " + Integer.toString(blocksBelow));

        switch (newDirection) {
            case LEFT:
                return new int[] {boundaryMin + blocksToLeft*blockLength, currentY};
            case RIGHT:
                return new int[] {boundaryMax - blocksToRight*blockLength, currentY};
            case UP:
                return new int[] {currentX, boundaryMin + blocksAbove*blockLength};
            case DOWN:
                return new int[] {currentX, boundaryMax - blocksBelow*blockLength};
            default:
                return new int[] {currentX, currentY};
        }
    }

    private boolean willBlocksMove(GameDirection newDirection) {
        for (GameBlock gameBlock : gameBlocks) {
            int[] targetCoordinates = getTargetCoordinates(gameBlock, newDirection);
            Log.d("coordinates", Integer.toString(targetCoordinates[0])
                    + " " + Integer.toString(targetCoordinates[1])
                    + " <= " + Integer.toString(gameBlock.getCurrentX())
                    + " " + Integer.toString(gameBlock.getCurrentY()));

            if (targetCoordinates[0] != gameBlock.getCurrentX() ||
                    targetCoordinates[1] != gameBlock.getCurrentY()) {
                return true;
            }
        }
        return false;
    }

    public void setDirection(GameDirection newDirection) {

        this.justSpawned = currentGameDirection != NO_MOVEMENT && newDirection == NO_MOVEMENT;

        this.currentGameDirection = newDirection;

        if (!isMoving && !justSpawned) {
            for (GameBlock gameBlock : gameBlocks) {
                int[] targetCoordinates = getTargetCoordinates(gameBlock, newDirection);
                gameBlock.setBlockDirection(newDirection);
                gameBlock.setTargetCoordinates(targetCoordinates);
            }
        }
    }

    @Override
    public void run() {
        myActivity.runOnUiThread(() -> {
            wasMoving = isMoving;
            isMoving = false;

            for (GameBlock gameBlock : gameBlocks) {
                gameBlock.move();
                if (gameBlock.isMoving()) {
                    isMoving = true;
                }
            }

            this.spawnBlock = !isMoving && wasMoving;

            if (this.gameBlocks.size() < 16 && this.spawnBlock) {
                this.createBlock();
                Log.d("game blocks", Integer.toString(gameBlocks.size()));
            }
        });
    }
}
