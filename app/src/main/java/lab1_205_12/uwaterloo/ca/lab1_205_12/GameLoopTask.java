package lab1_205_12.uwaterloo.ca.lab1_205_12;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;

import java.util.TimerTask;

import static lab1_205_12.uwaterloo.ca.lab1_205_12.GameDirection.NO_MOVEMENT;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.Lab1_205_12.boundaryMax;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.Lab1_205_12.boundaryMin;

/**
 * Created by Chuqian Li on 2017-06-30.
 */

public class GameLoopTask extends TimerTask {

    private Activity myActivity;
    private RelativeLayout myRL;
    private Context myContext;
    private GameBlock gameBlock;

    public GameDirection currentGameDirection = NO_MOVEMENT;

    public GameLoopTask(Activity myActivity, RelativeLayout myRL, Context myContext) {
        this.myActivity = myActivity;
        this.myRL = myRL;
        this.myContext = myContext;
        this.gameBlock = createBlock();
    }

    private GameBlock createBlock(){
        GameBlock newBlock = new GameBlock(this.myContext, boundaryMin, boundaryMin);
        myRL.addView(newBlock);
        return newBlock;
    }

    private GameBlock createBlock(int coordX, int coordY){
        GameBlock newBlock = new GameBlock(this.myContext, coordX, coordY);
        myRL.addView(newBlock);
        return newBlock;
    }

    public void setDirection(GameDirection newDirection) {
        this.currentGameDirection = newDirection;
        this.gameBlock.setBlockDirection(currentGameDirection);
    }

    @Override
    public void run() {
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameBlock.move();
            }
        });
    }
}
