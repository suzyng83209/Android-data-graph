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

    private List<GameBlock> gameBlocks;
    private List<int[]> freeBlocks;
    public int blockLength = (boundaryMax - boundaryMin) / 3;

    public GameDirection currentGameDirection = NO_MOVEMENT;

    public GameLoopTask(Activity myActivity, RelativeLayout myRL, Context myContext) {
        this.myActivity = myActivity;
        this.myRL = myRL;
        this.myContext = myContext;
        this.gameBlocks = new ArrayList<>();
        this.freeBlocks = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int[] coordinates = new int[] {boundaryMin + blockLength*i, boundaryMin + blockLength*j};
                freeBlocks.add(coordinates);
            }
        }
        createBlock();
    }

    private void createBlock(){
        this.spawnBlock = false;

        int coordinatePicker = new Random().nextInt(16 - gameBlocks.size());
        int[] coordinates = freeBlocks.get(coordinatePicker);
        freeBlocks.remove(coordinatePicker);

        GameBlock newBlock = new GameBlock(this.myContext, coordinates);

        this.gameBlocks.add(newBlock);
        this.myRL.addView(newBlock);
    }

    private void createBlock(int coordX, int coordY){
        GameBlock newBlock = new GameBlock(this.myContext, coordX, coordY);
        this.myRL.addView(newBlock);
        this.gameBlocks.add(newBlock);
        freeBlocks.remove(new int[] {coordX, coordY});
    }

    private void setTargetCoordinates(GameBlock currentBlock) {
        int currentX = currentBlock.getCurrentX();
        int currentY = currentBlock.getCurrentY();

        int blocksToLeft = 0;
        int blocksToRight = 0;
        int blocksAbove = 0;
        int blocksBelow = 0;
        for (GameBlock gameBlock : gameBlocks) {
            if(gameBlock != currentBlock) {
                int blockX = gameBlock.getCurrentX();
                int blockY = gameBlock.getCurrentY();

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

        switch (this.currentGameDirection) {
            case LEFT:
                currentBlock.setTargetX(boundaryMin + blocksToLeft*blockLength);
                break;
            case RIGHT:
                currentBlock.setTargetX(boundaryMax - blocksToRight*blockLength);
                break;
            case UP:
                currentBlock.setTargetY(boundaryMin + blocksAbove*blockLength);
                break;
            case DOWN:
                currentBlock.setTargetY(boundaryMax - blocksBelow*blockLength);
                break;
            default:
                currentBlock.setTargetCoordinates(currentX, currentY);
        }
    }

    public void setDirection(GameDirection newDirection) {
        if (!isMoving) {
            this.currentGameDirection = newDirection;
            for (GameBlock gameBlock : gameBlocks) {
                gameBlock.setBlockDirection(newDirection);
                setTargetCoordinates(gameBlock);
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
