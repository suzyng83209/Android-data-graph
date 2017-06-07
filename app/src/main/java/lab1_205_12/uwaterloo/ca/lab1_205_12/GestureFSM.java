package lab1_205_12.uwaterloo.ca.lab1_205_12;

import java.util.Date;

import static lab1_205_12.uwaterloo.ca.lab1_205_12.VectorState.FALL_B;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.VectorState.RISE_A;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.VectorState.WAIT;

/**
 * Created by Chuqian Li on 2017-06-06.
 */

public class GestureFSM {

    private VectorState currentState = WAIT;
    private float[][] data;
    private Axis axis;
    private double threshold;
    private int axisVal;
    private int numVals;

    public void GestureFSM(Axis axis, float[][] data) {
        this.axis = axis;
        switch(axis) {
            case X:
                this.axisVal = 0;
                this.numVals = 8;
                this.threshold = 2;
                break;
            case Y:
                this.axisVal = 1;
                this.numVals = 5;
                this.threshold = 0.2;
                break;
            case Z:
                this.axisVal = 2;
                this.numVals = 0;
                this.threshold = 0;
                break;
        }

        this.data = data;
        float dA = data[0][axisVal] - data[numVals][axisVal];

        switch(currentState) {
            case WAIT:
                if (dA > threshold) {
                    this.currentState = RISE_A;
                } else if (dA < -1*threshold) {
                    this.currentState = FALL_B;
                }
                break;

            case RISE_A:
                //TODO
                break;

            case FALL_A:
                //TODO
                break;

            case FALL_B:
                //TODO
                break;

            case RISE_B:
                //TODO
                break;

            case DETERMINED:
                //TODO save determined direction
                this.currentState = WAIT;
        }
    }
}
