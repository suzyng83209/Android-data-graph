package lab1_205_12.uwaterloo.ca.lab2_205_12;

import android.hardware.*;
import android.widget.*;

import ca.uwaterloo.sensortoy.LineGraphView;

/**
 * Contains a set of utility methods for setting up the display of
 * sensor readings alongside shared constants.
 */
public class SensorDisplayUtilities
{
    /**
     * Creates a sensor event listener for tracking and displaying sensor readings.
     *
     * @param sensorManager The system sensor manager.
     * @param sensorType The type of sensor to retrieve and display data from.
     *
     * @return The newly created and registered sensor event listener.
     */
    public static AbstractSensorEventListener createSensorEventListener(SensorManager sensorManager,
                                                                        int sensorType)
    {
        Sensor sensor = sensorManager.getDefaultSensor(sensorType);
        AbstractSensorEventListener sensorEventListener = new VectorSensorEventListener(sensorType);
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
        return sensorEventListener;
    }

    /**
     * Creates a sensor data logger to store a scrolling record of past readings and provide
     * the utility to save this data.
     *
     * @param sensorManager The system sensor manager.
     * @param dataPoints The maximum number of data points to store prior to scrolling.
     * @param components The number of components per data point.
     * @param sensorType The type of sensor to log data from.
     * @param filePath The file path into which to store the data file.
     * @param fileName The base file name to assign to each saved reading; appends an incrementing
     *                 number to each file name of the format, "_%d".
     * @param labelFormat The format with which to display the file path info onto the supplied
     *                    TextView.
     * @return The newly created and registered sensor data logger.
     */
    public static SensorDataHandler createSensorDataLogger(SensorManager sensorManager,
                                                           int dataPoints,
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
        Sensor sensor = sensorManager.getDefaultSensor(sensorType);
        SensorDataHandler sensorDataHandler = new SensorDataHandler(
                dataPoints,
                components,
                sensorType,
                filePath,
                fileName,
                labelFormat,
                gestureX,
                gestureY,
                gestureLabel,
                graph);
        sensorManager.registerListener(sensorDataHandler, sensor, SensorManager.SENSOR_DELAY_GAME);
        return sensorDataHandler;
    }
}
