package lab1_205_12.uwaterloo.ca.lab1_205_12;

import static lab1_205_12.uwaterloo.ca.lab1_205_12.VectorState.DETERMINED;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.VectorState.FALL_A;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.VectorState.FALL_B;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.VectorState.RISE_A;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.VectorState.RISE_B;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.VectorState.WAIT;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.VectorType.TYPE_A;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.VectorType.TYPE_B;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.VectorType.TYPE_X;

/**
 * Created by Chuqian Li on 2017-06-06.
 */

public class GestureFSM {

    private VectorState currentState = WAIT;
    private VectorType type = TYPE_X;
    private boolean initialSetUp = false;
    private Float previousReading = null;
    private float threshold_A1;
    private float threshold_A2;
    private float threshold_A3;
    private float threshold_B1;
    private float threshold_B2;
    private float threshold_B3;
    private int count = 0;

    /**
     *
     * @param threshold_A1 - the first threshold for Type A
     * @param threshold_A2 - the second threshold for Type A
     * @param threshold_A3 - the third threshold for Type A
     * @param threshold_B1 - the first threshold for Type B
     * @param threshold_B2 - the second threshold for Type B
     * @param threshold_B3 - the third threshold for Type B
     */
    public GestureFSM(float threshold_A1,
                      float threshold_A2,
                      float threshold_A3,
                      float threshold_B1,
                      float threshold_B2,
                      float threshold_B3) {
        this.threshold_A1 = threshold_A1;
        this.threshold_A2 = threshold_A2;
        this.threshold_A3 = threshold_A3;
        this.threshold_B1 = threshold_B1;
        this.threshold_B2 = threshold_B2;
        this.threshold_B3 = threshold_B3;
    }

    public void setThresholds(float initialValues) {
        this.threshold_A2 += initialValues;
        this.threshold_A3 += initialValues;
        this.threshold_B2 += initialValues;
        this.threshold_B3 += initialValues;
        this.initialSetUp = true;
    }

    public boolean getInitialSetUp() {
        return this.initialSetUp;
    }

    public void resetThresholds() {
        this.initialSetUp = false;
    }

    /**
     *
     * @param currentReading - the current reading from the accelerometer (x, y, or z)
     */
    public void analyze(float currentReading) {
        if (previousReading == null) previousReading = currentReading;
        Float dA = currentReading - previousReading;

        if (count > 30) {
            this.currentState = WAIT;
            this.type = TYPE_X;
            count = 0;
        }

        switch(currentState) {
            case WAIT:
                if (dA > threshold_A1) {
                    this.currentState = RISE_A;
                } else if (dA < threshold_B1) {
                    this.currentState = FALL_B;
                }
                break;

            case RISE_A:
                if (dA < 0) {
                    if (previousReading > threshold_A2) {
                        this.currentState = FALL_A;
                    } else {
                        this.type = TYPE_X;
                        this.currentState = DETERMINED;
                    }
                }
                count++;
                break;

            case FALL_A:
                if (dA > 0) {
                    if (previousReading < threshold_A3) {
                        this.type = TYPE_A;
                        this.currentState = DETERMINED;
                    } else {
                        this.type = TYPE_X;
                        this.currentState = DETERMINED;
                    }
                }
                count++;
                break;

            case FALL_B:
                if (dA > 0) {
                    if (previousReading < threshold_B2) {
                        this.currentState = RISE_B;
                    } else {
                        this.type = TYPE_X;
                        this.currentState = DETERMINED;
                    }
                }
                count++;
                break;

            case RISE_B:
                if (dA < 0) {
                    if (previousReading > threshold_B3) {
                        this.type = TYPE_B;
                        this.currentState = DETERMINED;
                    } else {
                        this.type = TYPE_X;
                        this.currentState = DETERMINED;
                    }
                }
                break;

            case DETERMINED:
                count++;
                break;
        }

        this.previousReading = currentReading;
    }

    public VectorState getCurrentState() {
        return this.currentState;
    }

    public VectorType getType() {
        return this.type;
    }
}
