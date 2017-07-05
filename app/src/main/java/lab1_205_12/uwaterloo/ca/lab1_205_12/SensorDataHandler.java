package lab1_205_12.uwaterloo.ca.lab1_205_12;

import android.hardware.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.io.*;

import ca.uwaterloo.sensortoy.LineGraphView;

import static lab1_205_12.uwaterloo.ca.lab1_205_12.VectorType.TYPE_A;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.VectorType.TYPE_B;
import static lab1_205_12.uwaterloo.ca.lab1_205_12.VectorType.TYPE_X;

/**
 * Keeps a log of the most recent readings from a specified sensor.
 */
public class SensorDataHandler implements SensorEventListener {
    // log data dimensions
    private final int DATA_POINTS;
    private final int COMPONENTS;

    private final int SENSOR_TYPE;

    // log data arrays; implement in place "scrolling"
    private float[][] history; // a scrolling list of DATA_POINTS data points of COMPONENTS size
    private float[] timeStamps; // a scrolling list of the time stamps at which each reading was taken

    // log data array scrolling variables
    private int startIndex = 0; // the current index at which the scrolling list starts
    private int currentIndex = startIndex; // the current index at which to store the scrolling data
    private boolean indexWrapping = false; // defines if list scrolling has started

    // adding graph components
    private LineGraphView graph = null;
    private float[] filteredReadings;

    // gesture components
    private GestureFSM gestureX;
    private GestureFSM gestureY;
    private TextView gestureLabel;
    private GameLoopTask gameLoopTask;

    /**
     * Creates a SensorDataHandler that allows the logging and saving of data to
     * an Excel accessible CSV file.
     *
     * @param dataPoints The number of data points to log.
     * @param components The number of components per data point.
     * @param sensorType The type of sensor to query data from.
     */
    public SensorDataHandler(int dataPoints,
                             int components,
                             int sensorType,
                             GestureFSM gestureX,
                             GestureFSM gestureY,
                             TextView gestureLabel,
                             GameLoopTask gameLoopTask)
    {
        DATA_POINTS = dataPoints;
        COMPONENTS = components;
        SENSOR_TYPE = sensorType;

        history = new float[DATA_POINTS][COMPONENTS];
        timeStamps = new float[DATA_POINTS];

        filteredReadings = new float[COMPONENTS];

        this.gestureLabel = gestureLabel;
        this.gestureX = gestureX;
        this.gestureY = gestureY;
        this.gameLoopTask = gameLoopTask;
    }

    public void onAccuracyChanged(Sensor s, int i){}

    /**
     * Adds a sensor data point to the line graph.
     *
     * Confirm thread safety as per whether sensor data might be updated
     * while the history array is being accessed for file IO.
     *
     * @param se The SensorEvent from which the data point is to be obtained.
     */
    @Override
    public void onSensorChanged(SensorEvent se) {
        if(se.sensor.getType() == SENSOR_TYPE) {
            String LEFT = "LEFT";
            String RIGHT = "RIGHT";
            String UP = "UP";
            String DOWN = "DOWN";

            // necessary defensive copy as SensorEvent seems to store only one pointer; directly storing the
            // values pointer into the array will cause every element to point to the same data point
            for(int i = 0; i < COMPONENTS; i++) {
                filteredReadings[i] += (se.values[i] - filteredReadings[i]) / 9;
                history[currentIndex][i] = filteredReadings[i];
            }

            if (!gestureX.getInitialSetUp() || !gestureY.getInitialSetUp()) {
                gestureX.setThresholds(filteredReadings[0]);
                gestureY.setThresholds(filteredReadings[1]);
            } else {
                gestureX.analyze(filteredReadings[0]);
                gestureY.analyze(filteredReadings[1]);
            }

            if (gestureX.getCurrentState() == VectorState.DETERMINED ||
                    gestureY.getCurrentState() == VectorState.DETERMINED) {
                VectorType xDir = gestureX.getType();
                VectorType yDir = gestureY.getType();

                if (xDir == TYPE_B && yDir == TYPE_X) {
                    gestureLabel.setText(LEFT);
                    gameLoopTask.setDirection(GameDirection.LEFT);
                } else if (xDir == TYPE_A && yDir == TYPE_X) {
                    gestureLabel.setText(RIGHT);
                    gameLoopTask.setDirection(GameDirection.RIGHT);
                } else if (xDir == TYPE_X && yDir == TYPE_A) {
                    gestureLabel.setText(UP);
                    gameLoopTask.setDirection(GameDirection.UP);
                } else if (xDir == TYPE_X && yDir == TYPE_B) {
                    gestureLabel.setText(DOWN);
                    gameLoopTask.setDirection(GameDirection.DOWN);
                } else {
                    gameLoopTask.setDirection(GameDirection.NO_MOVEMENT);
                }
            }

            timeStamps[currentIndex] = se.timestamp;

            int maxIndex = DATA_POINTS - 1;

            // activate list scrolling (index wraps around) once list is filled
            if(!indexWrapping && currentIndex >= maxIndex) indexWrapping = true;

            // wrap indices to facilitate list scrolling
            currentIndex = wrap(currentIndex, maxIndex);
            if(indexWrapping) startIndex = wrap(startIndex, maxIndex);
        }
    }

    /**
     * Increments a number, resetting it to zero if the number is equal to some
     * maximum index.
     *
     * @param currentIndex The number to be incremented with wrapping.
     * @param maxIndex The maximum value the number can be incremented to before wrapping.
     *
     * @return The incremented number with wrapping.
     */
    private int wrap(int currentIndex, int maxIndex)
    {
        return (currentIndex >= maxIndex) ? 0 : (currentIndex + 1);
    }
}
