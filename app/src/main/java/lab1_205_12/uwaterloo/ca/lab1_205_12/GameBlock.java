package lab1_205_12.uwaterloo.ca.lab1_205_12;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;

import static lab1_205_12.uwaterloo.ca.lab1_205_12.Lab1_205_12.boundaryMax;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.Lab1_205_12.boundaryMin;

/**
 * Created by Chuqian Li on 2017-07-05.
 */

public class GameBlock extends FrameLayout {

    private final float IMAGE_SCALE = 0.67f;
    private int myCoordX;
    private int myCoordY;
    private int targetX;
    private int targetY;
    private GameDirection myDirection = GameDirection.NO_MOVEMENT;
    private boolean isMoving = false;
    private int initialVelocity = 45;
    private int velocity = initialVelocity;
    private int acceleration = 30;

    private TextView numberValue;
    private int number;

    public GameBlock(Context myContext, int coordX, int coordY) {
        super(myContext);
        this.setBackgroundResource(R.drawable.gameblock);
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);
        this.setX(coordX);
        this.setY(coordY);

        this.myCoordX = coordX;
        this.myCoordY = coordY;
    }

    public GameBlock(Context myContext, int[] coordinates) {
        super(myContext);
        this.setBackgroundResource(R.drawable.gameblock);
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);
        this.myCoordX = coordinates[0];
        this.myCoordY = coordinates[1];
        this.setX(myCoordX);
        this.setY(myCoordY);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        number = (int) Math.pow(2, new Random().nextInt(10));
        numberValue = new TextView(myContext);
        numberValue.setText(String.format(Locale.CANADA, "%d", number));
        numberValue.setTextColor(Color.BLACK);
        numberValue.setTextSize(48);
        numberValue.setLayoutParams(params);
        this.addView(numberValue);
    }

    public void setBlockDirection(GameDirection newDirection) {
        this.myDirection = newDirection;
        int[] targetCoordinates = getTargetCoordinates();
        this.targetX = targetCoordinates[0];
        this.targetY = targetCoordinates[1];
        this.velocity = initialVelocity;
    }

    private int[] getTargetCoordinates() {
        switch (this.myDirection) {
            case LEFT:
                return new int[]{boundaryMin, myCoordY};
            case RIGHT:
                return new int[]{boundaryMax, myCoordY};
            case UP:
                return new int[]{myCoordX, boundaryMin};
            case DOWN:
                return new int[]{myCoordX, boundaryMax};
            default:
                return new int[]{myCoordX, myCoordY};
        }
    }

    public int[] getCurrentCoordinates() {
        return new int[]{this.myCoordX, this.myCoordY};
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void move() {

        switch(this.myDirection) {
            case LEFT:
                if (myCoordX > targetX) {
                    myCoordX -= velocity;
                    isMoving = true;
                }
                else {
                    myCoordX = targetX;
                    isMoving = false;
                }
                break;

            case RIGHT:
                if (myCoordX < targetX) {
                    myCoordX += velocity;
                    isMoving = true;
                }
                else {
                    myCoordX = targetX;
                    isMoving = false;
                }
                break;

            case UP:
                if (myCoordY > targetY) {
                    myCoordY -= velocity;
                    isMoving = true;
                }
                else {
                    myCoordY = targetY;
                    isMoving = false;
                }
                break;

            case DOWN:
                if (myCoordY < targetY) {
                    myCoordY += velocity;
                    isMoving = true;
                }
                else {
                    myCoordY = targetY;
                    isMoving = false;
                }
                break;

            default:
                // TODO
                break;
        }

        velocity += acceleration;

        this.setX(myCoordX);
        this.setY(myCoordY);
    }
}
