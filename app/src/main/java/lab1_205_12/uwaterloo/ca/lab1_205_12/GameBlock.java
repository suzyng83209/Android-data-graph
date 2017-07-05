package lab1_205_12.uwaterloo.ca.lab1_205_12;

import android.content.Context;
import android.widget.ImageView;

import static lab1_205_12.uwaterloo.ca.lab1_205_12.Lab1_205_12.boundaryMax;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.Lab1_205_12.boundaryMin;

/**
 * Created by Chuqian Li on 2017-07-05.
 */

public class GameBlock extends ImageView {

    private final float IMAGE_SCALE = 0.75f;
    private int myCoordX;
    private int myCoordY;
    private int targetX;
    private int targetY;
    private GameDirection myDirection = GameDirection.NO_MOVEMENT;

    private boolean moving = false;
    private int velocity = 3;
    private int acceleration = 30;

    public GameBlock(Context myContext, int coordX, int coordY) {
        super(myContext);
        this.setImageResource(R.drawable.gameblock);
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);
        this.setX(coordX);
        this.setY(coordY);

        this.myCoordX = coordX;
        this.myCoordY = coordY;
        this.targetX = coordX;
        this.targetY = coordY;
    }

    public void setBlockDirection(GameDirection newDirection) {
        myDirection = newDirection;
        int[] targetCoordinates = getTargetCoordinates();
        this.targetX = targetCoordinates[0];
        this.targetY = targetCoordinates[1];
    }

    public int[] getTargetCoordinates() {
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

    public void move() {
//        moving = true;
        switch(this.myDirection) {
            case LEFT:
                if (myCoordX > targetX) myCoordX -= velocity;
                break;

            case RIGHT:
                if (myCoordX < targetX) myCoordX += velocity;
                break;

            case UP:
                if (myCoordY > targetY) myCoordY -= velocity;
                break;

            case DOWN:
                if (myCoordY < targetY) myCoordY += velocity;
                break;

            default:
                // TODO
                break;
        }

        velocity += acceleration;

//        switch(this.myDirection) {
//            case LEFT:
//            case RIGHT:
//                myCoordX = targetX;
//                break;
//
//            case UP:
//            case DOWN:
//                myCoordY = targetY;
//                break;
//
//            default:
//                // TODO
//                break;
//        }

        this.setX(myCoordX);
        this.setY(myCoordY);
    }
}
