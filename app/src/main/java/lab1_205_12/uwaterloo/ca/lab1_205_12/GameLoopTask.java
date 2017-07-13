package lab1_205_12.uwaterloo.ca.lab1_205_12;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private List<GameBlock> deleteBlocks;
    private List<int[]> freeBlocks;
    public int blockLength = (boundaryMax - boundaryMin) / 3;

    public GameDirection currentGameDirection = NO_MOVEMENT;

    public GameLoopTask(Activity myActivity, RelativeLayout myRL, Context myContext) {
        this.myActivity = myActivity;
        this.myRL = myRL;
        this.myContext = myContext;


        this.gameBlocks = new ArrayList<>();
        this.deleteBlocks = new ArrayList<>();
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

    private int[] getTargetCoordinates(GameBlock currentBlock, GameDirection newDirection) {
        int currentX = currentBlock.getCurrentX();
        int currentY = currentBlock.getCurrentY();

        List<GameBlock> blocksToLeft = new ArrayList<>();
        List<GameBlock> blocksToRight = new ArrayList<>();
        List<GameBlock> blocksAbove = new ArrayList<>();
        List<GameBlock> blocksBelow = new ArrayList<>();

        for (GameBlock gameBlock : gameBlocks) {
            int blockX = gameBlock.getCurrentX();
            int blockY = gameBlock.getCurrentY();

            boolean sameBlock = blockX == currentX && blockY == currentY;

            if(!sameBlock) {
                if (blockY == currentY) {
                    if (blockX < currentX) {
                        blocksToLeft.add(gameBlock);
                    } else {
                        blocksToRight.add(gameBlock);
                    }
                } else if (blockX == currentX) {
                    if (blockY < currentY) {
                        blocksAbove.add(gameBlock);
                    } else {
                        blocksBelow.add(gameBlock);
                    }
                }
            }
        }

        switch (newDirection) {
            case LEFT:
                Collections.sort(blocksToLeft, new Comparator<GameBlock>() {
                    @Override
                    public int compare(GameBlock block1, GameBlock block2) {
                        Integer distance1 = currentX - block1.getCurrentX();
                        Integer distance2 = currentX - block2.getCurrentX();
                        return distance1.compareTo(distance2);
                    }
                });
                GameBlock leftBlock = blocksToLeft.get(0);
                if (leftBlock.getNumber() == currentBlock.getNumber()) {
                    blocksToLeft.remove(leftBlock);
                    gameBlocks.remove(leftBlock);
                    currentBlock.increaseNumber();
                }
                return new int[] {boundaryMin + blocksToLeft.size()*blockLength, currentY};
            case RIGHT:
                Collections.sort(blocksToRight, new Comparator<GameBlock>() {
                    @Override
                    public int compare(GameBlock block1, GameBlock block2) {
                        Integer distance1 = block1.getCurrentX() - currentX;
                        Integer distance2 = block2.getCurrentX() - currentX;
                        return distance1.compareTo(distance2);
                    }
                });
                GameBlock rightBlock = blocksToRight.get(0);
                if (rightBlock.getNumber() == currentBlock.getNumber()) {
                    blocksToRight.remove(rightBlock);
                    gameBlocks.remove(rightBlock);
                    currentBlock.increaseNumber();
                }
                return new int[] {boundaryMax - blocksToRight.size()*blockLength, currentY};
            case UP:
                Collections.sort(blocksAbove, new Comparator<GameBlock>() {
                    @Override
                    public int compare(GameBlock block1, GameBlock block2) {
                        Integer distance1 = currentY - block1.getCurrentY();
                        Integer distance2 = currentY - block2.getCurrentY();
                        return distance1.compareTo(distance2);
                    }
                });
                GameBlock aboveBlock = blocksAbove.get(0);
                if (aboveBlock.getNumber() == currentBlock.getNumber()) {
                    blocksAbove.remove(aboveBlock);
                    gameBlocks.remove(aboveBlock);
                    currentBlock.increaseNumber();
                }
                return new int[] {currentX, boundaryMin + blocksAbove.size()*blockLength};
            case DOWN:
                Collections.sort(blocksBelow, new Comparator<GameBlock>() {
                    @Override
                    public int compare(GameBlock block1, GameBlock block2) {
                        Integer distance1 = block1.getCurrentY() - currentY;
                        Integer distance2 = block2.getCurrentY() - currentY;
                        return distance1.compareTo(distance2);
                    }
                });
                GameBlock belowBlock = blocksBelow.get(0);
                if (belowBlock.getNumber() == currentBlock.getNumber()) {
                    blocksAbove.remove(belowBlock);
                    gameBlocks.remove(belowBlock);
                    currentBlock.increaseNumber();
                }
                return new int[] {currentX, boundaryMax - blocksBelow.size()*blockLength};
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
        if (!willBlocksMove(newDirection)) {
            return;
        }
        if (!isMoving) {
            this.currentGameDirection = newDirection;
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
