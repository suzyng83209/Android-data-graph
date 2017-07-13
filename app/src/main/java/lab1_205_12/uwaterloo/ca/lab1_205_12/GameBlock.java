package lab1_205_12.uwaterloo.ca.lab1_205_12;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.FrameLayout;
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
    private int currentX;
    private int currentY;
    private int targetX;
    private int targetY;
    private GameDirection myDirection = GameDirection.NO_MOVEMENT;
    private boolean isMoving = false;
    private int initialVelocity = 30;
    private int velocity = initialVelocity;
    private int acceleration = 20;

    private TextView numberValue;
    private int number;

    public GameBlock(Context myContext, int coordX, int coordY) {
        super(myContext);
        this.setBackgroundResource(R.drawable.gameblock);
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);
        this.setX(coordX);
        this.setY(coordY);

        this.currentX = coordX;
        this.currentY = coordY;
    }

    public GameBlock(Context myContext, int[] coordinates) {
        super(myContext);
        this.setBackgroundResource(R.drawable.gameblock);
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);
        this.currentX = coordinates[0];
        this.currentY = coordinates[1];
        this.setX(currentX);
        this.setY(currentY);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        number = (int) Math.pow(2, new Random().nextInt(2) + 1);
        numberValue = new TextView(myContext);
        numberValue.setText(String.format(Locale.CANADA, "%d", number));
        numberValue.setTextColor(Color.BLACK);
        numberValue.setTextSize(48);
        numberValue.setLayoutParams(params);
        this.addView(numberValue);
    }

    public void setBlockDirection(GameDirection newDirection) {
        this.myDirection = newDirection;
        this.velocity = initialVelocity;
    }

    public void setTargetCoordinates(int[] targetCoordinates) {
        this.targetX = targetCoordinates[0];
        this.targetY = targetCoordinates[1];
    }

    public void setTargetCoordinates(int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
        this.targetY = currentY;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
        this.targetX = currentX;
    }

    public void increaseNumber() {
        this.number *= 2;
    }

    public int[] getCurrentCoordinates() {
        return new int[]{this.currentX, this.currentY};
    }

    public int getCurrentX() {
        return this.currentX;
    }

    public int getCurrentY() {
        return this.currentY;
    }

    public int getNumber() {
        return this.number;
    }

    public GameDirection getMyDirection() {
        return this.myDirection;
    }

    public boolean isMoving() {
        return currentX != targetX || currentY != targetY;
    }

    public void move() {

        switch(this.myDirection) {
            case LEFT:
                if (currentX > targetX) {
                    currentX -= velocity;
                }
                else {
                    currentX = targetX;
                }
                break;

            case RIGHT:
                if (currentX < targetX) {
                    currentX += velocity;
                }
                else {
                    currentX = targetX;
                }
                break;

            case UP:
                if (currentY > targetY) {
                    currentY -= velocity;
                }
                else {
                    currentY = targetY;
                }
                break;

            case DOWN:
                if (currentY < targetY) {
                    currentY += velocity;
                }
                else {
                    currentY = targetY;
                }
                break;

            default:
                // TODO
                break;
        }

        velocity += acceleration;

        this.setX(currentX);
        this.setY(currentY);
    }
}
