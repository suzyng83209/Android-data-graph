package lab1_205_12.uwaterloo.ca.lab2_205_12;

import android.hardware.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.io.*;

import ca.uwaterloo.sensortoy.LineGraphView;

import static lab1_205_12.uwaterloo.ca.lab2_205_12.VectorType.TYPE_A;
import static lab1_205_12.uwaterloo.ca.lab2_205_12.VectorType.TYPE_B;
import static lab1_205_12.uwaterloo.ca.lab2_205_12.VectorType.TYPE_X;

/**
 * Keeps a log of the most recent readings from a specified sensor.
 */
public class SensorDataHandler implements SensorEventListener, View.OnClickListener
{
    // log data dimensions
    private final int DATA_POINTS;
    private final int COMPONENTS;

    private final int SENSOR_TYPE;

    // log data storage location string constants
    private final String FILE_PATH;
    private final String FILE_NAME;

    private int logCount = 0; // provides a count index for each reading file name; may replace with a date stamp
    private boolean logging = false; // set to true when onClick() has initiated a file save operation

    // optional TextView providing formatted display of previous file path used
    private final String LABEL_FORMAT;

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
    private String LEFT = "LEFT";
    private String RIGHT = "RIGHT";
    private String UP = "UP";
    private String DOWN = "DOWN";

    /**
     * Creates a SensorDataHandler that allows the logging and saving of data to
     * an Excel accessible CSV file.
     *
     * @param dataPoints The number of data points to log.
     * @param components The number of components per data point.
     * @param sensorType The type of sensor to query data from.
     * @param filePath The full file path of the folder to store the data into.
     * @param fileName The name of the file to store the data into.
     * @param labelFormat The format with which to display the file path of the next reading.
     *                    This can be set to null if unneeded.
     */
    public SensorDataHandler(int dataPoints,
                             int components,
                             int sensorType,
                             String filePath,
                             String fileName,
                             String labelFormat,
                             GestureFSM gestureX,
                             GestureFSM gestureY,
                             TextView gestureLabel,
                             LineGraphView graph)
    {
        DATA_POINTS = dataPoints;
        COMPONENTS = components;
        SENSOR_TYPE = sensorType;

        FILE_PATH = filePath;
        FILE_NAME= fileName;

        LABEL_FORMAT = labelFormat;

        history = new float[DATA_POINTS][COMPONENTS];
        timeStamps = new float[DATA_POINTS];

        this.graph = graph;
        filteredReadings = new float[COMPONENTS];

        this.gestureLabel = gestureLabel;
        this.gestureX = gestureX;
        this.gestureY = gestureY;
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
    public void onSensorChanged(SensorEvent se)
    {
        if(se.sensor.getType() == SENSOR_TYPE)
        {
            // necessary defensive copy as SensorEvent seems to store only one pointer; directly storing the
            // values pointer into the array will cause every element to point to the same data point
            for(int i = 0; i < COMPONENTS; i++) {
                filteredReadings[i] += (se.values[i] - filteredReadings[i]) / 9;
                history[currentIndex][i] = filteredReadings[i];
            }

            //adding graph points and filtering
            if (graph != null) graph.addPoint(filteredReadings);

            gestureX.analyze(filteredReadings[0]);
            gestureY.analyze(filteredReadings[1]);

            if (gestureX.getCurrentState() == VectorState.DETERMINED ||
                    gestureY.getCurrentState() == VectorState.DETERMINED) {
                VectorType xDir = gestureX.getType();
                VectorType yDir = gestureY.getType();

                if (xDir == TYPE_B && yDir == TYPE_X) {
                    gestureLabel.setText(LEFT);
                } else if (xDir == TYPE_A && yDir == TYPE_X) {
                    gestureLabel.setText(RIGHT);
                } else if (xDir == TYPE_X && yDir == TYPE_A) {
                    gestureLabel.setText(UP);
                } else if (xDir == TYPE_X && yDir == TYPE_B) {
                    gestureLabel.setText(DOWN);
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

    /**
     * Saves the current data to an Excel readable CSV file.
     *
     * @param v See View.OnClickListener documentation.
     */
    @Override
    public void onClick(View v)
    {
        // "!logging" to ensure only one thread initiates file IO at a time
        if(!logging && history != null && timeStamps != null && FILE_PATH != null && FILE_NAME != null)
        {
            logging = true;

            // construct the file path
            String filePath = FILE_PATH + "/" + FILE_NAME + "_" + logCount + ".csv";
            File file = new File(filePath);

            logCount++;

            // save log data to CSV file

            PrintWriter outputStream = null;

            try
            {
                outputStream = new PrintWriter(new FileWriter(file));

                int maxIndex = DATA_POINTS - 1;

                for(int i = 0, wrappingIndex = startIndex; i < DATA_POINTS; i++, wrappingIndex = wrap(wrappingIndex, maxIndex))
                {
                    outputStream.print(i + ", " + wrappingIndex + ", ");
                    for(int j = 0; j < COMPONENTS; j++) outputStream.print(history[wrappingIndex][j] + ", ");
                    outputStream.println(timeStamps[wrappingIndex]);
                }

                Log.d("SensorDataHandler", "Logged last " + DATA_POINTS + " readings to " + filePath + ".");
            }
            catch(IOException e)
            {
                Log.e("SensorDataHandler", "Error: There was an exception while creating the file.");
                return;
            }
            finally
            {
                if(outputStream != null) outputStream.close();
            }

            logging = false;
        }
    }
}
