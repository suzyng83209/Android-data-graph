package lab1_205_12.uwaterloo.ca.lab1_205_12;

import android.content.Context;
import android.gesture.GestureUtils;
import android.hardware.*;
import android.view.ViewGroup;
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
     * @return The newly created and registered sensor data logger.
     */
    public static SensorDataHandler createSensorDataHandler(SensorManager sensorManager,
                                                            int dataPoints,
                                                            int components,
                                                            int sensorType,
                                                            GestureFSM gestureX,
                                                            GestureFSM gestureY,
                                                            TextView gestureLabel,
                                                            GameLoopTask gameLoopTask)
    {
        Sensor sensor = sensorManager.getDefaultSensor(sensorType);
        SensorDataHandler sensorDataHandler = new SensorDataHandler(
                dataPoints,
                components,
                sensorType,
                gestureX,
                gestureY,
                gestureLabel,
                gameLoopTask);
        sensorManager.registerListener(sensorDataHandler, sensor, SensorManager.SENSOR_DELAY_GAME);
        return sensorDataHandler;
    }
}
