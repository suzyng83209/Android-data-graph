package lab1_205_12.uwaterloo.ca.lab1_205_12;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

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
    private boolean isMoving = true;
    private boolean wasMoving = false;

    public List<GameBlock> gameBlocks;
    public List<int[]> freeBlocks;

    public GameDirection currentGameDirection = NO_MOVEMENT;

    public GameLoopTask(Activity myActivity, RelativeLayout myRL, Context myContext) {
        this.myActivity = myActivity;
        this.myRL = myRL;
        this.myContext = myContext;
        this.gameBlocks = new ArrayList<>();
        this.freeBlocks = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int blockLength = (boundaryMax - boundaryMin) / 4;
                freeBlocks.add(new int[]{boundaryMin + blockLength*i, boundaryMin + blockLength*j});
            }
        }
    }

    private void createBlock(){
        this.spawnBlock = false;

        int coordinatePicker = new Random().nextInt(freeBlocks.size());
        int[] coordinates = freeBlocks.get(coordinatePicker);
        GameBlock newBlock = new GameBlock(this.myContext, coordinates);

        this.myRL.addView(newBlock);
        this.gameBlocks.add(newBlock);
        this.findFreeBlocks(coordinates);
    }

    private void createBlock(int coordX, int coordY){
        GameBlock newBlock = new GameBlock(this.myContext, coordX, coordY);
        this.myRL.addView(newBlock);
        this.gameBlocks.add(newBlock);
        this.findFreeBlocks(new int[]{coordX, coordY});
    }

    public int findFreeBlocks(int[] usedCoordinates) {
        for (int i = 0; i < freeBlocks.size(); i++) {
            if (freeBlocks.get(i) == usedCoordinates) {
                freeBlocks.remove(i);
                return 0;
            }
        }
        return 1;
    }

    private int[] getTargetCoordinates(int[] currentCoordinates) {
        int currentX = currentCoordinates[0];
        int currentY = currentCoordinates[1];

        switch (this.currentGameDirection) {
            case LEFT:
                return new int[]{boundaryMin, currentY};
            case RIGHT:
                return new int[]{boundaryMax, currentY};
            case UP:
                return new int[]{currentX, boundaryMin};
            case DOWN:
                return new int[]{currentX, boundaryMax};
            default:
                return new int[]{currentX, currentY};
        }
    }

    public void setDirection(GameDirection newDirection) {
        this.currentGameDirection = newDirection;
        for (GameBlock gameBlock: gameBlocks) {
            int[] targetCoordinates = getTargetCoordinates(gameBlock.getCurrentCoordinates());
            gameBlock.setBlockDirection(newDirection);
            gameBlock.setTargetCoordinates(targetCoordinates);
        }
    }

    @Override
    public void run() {
        myActivity.runOnUiThread(() -> {
            if (this.gameBlocks.size() < 16 && this.spawnBlock) {
                this.createBlock();
            }
            wasMoving = isMoving;
            isMoving = true;
            for (GameBlock gameBlock : gameBlocks) {
                gameBlock.move();
                if (isMoving) {
                    isMoving = gameBlock.isMoving();
                }
            }
            this.spawnBlock = !isMoving && wasMoving;
        });
    }
}
