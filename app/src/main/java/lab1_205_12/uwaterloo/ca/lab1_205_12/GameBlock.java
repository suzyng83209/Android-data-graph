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
    private int initialVelocity = 45;
    private int velocity = initialVelocity;
    private int acceleration = 30;

    private TextView numberLabel;
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

        number = (int) Math.pow(2, new Random().nextInt(1) + 1);
        numberLabel = new TextView(myContext);
        numberLabel.setText(String.format(Locale.CANADA, "%d", number));
        numberLabel.setTextColor(Color.BLACK);
        numberLabel.setTextSize(48);
        numberLabel.setLayoutParams(params);
        this.addView(numberLabel);
    }

    public void setBlockDirection(GameDirection newDirection, GameBlock blockAhead) {
        this.myDirection = newDirection;
        this.velocity = initialVelocity;
        boolean canMerge = false;

        if (blockAhead != null) {
            canMerge = blockAhead.getNumber() == this.number;
        }

        switch(this.myDirection) {
            case LEFT:
                this.targetX = boundaryMin;
                this.targetY = currentY;
                break;
            case RIGHT:
                this.targetX = boundaryMax;
                this.targetY = currentY;
                break;
            case UP:
                this.targetX = currentX;
                this.targetY = boundaryMin;
                break;
            case DOWN:
                this.targetX = currentX;
                this.targetY = boundaryMin;
                break;
            default:
                this.targetX = currentX;
                this.targetY = currentY;
                break;
        }
    }

    public void setTargetCoordinates(int[] targetCoordinates) {
        this.targetX = targetCoordinates[0];
        this.targetY = targetCoordinates[1];
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

    public boolean isMoving() {
        return isMoving;
    }

    public void move() {

        switch(this.myDirection) {
            case LEFT:
                if (currentX > targetX) {
                    currentX -= velocity;
                    isMoving = true;
                }
                else {
                    currentX = targetX;
                    isMoving = false;
                }
                break;

            case RIGHT:
                if (currentX < targetX) {
                    currentX += velocity;
                    isMoving = true;
                }
                else {
                    currentX = targetX;
                    isMoving = false;
                }
                break;

            case UP:
                if (currentY > targetY) {
                    currentY -= velocity;
                    isMoving = true;
                }
                else {
                    currentY = targetY;
                    isMoving = false;
                }
                break;

            case DOWN:
                if (currentY < targetY) {
                    currentY += velocity;
                    isMoving = true;
                }
                else {
                    currentY = targetY;
                    isMoving = false;
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
