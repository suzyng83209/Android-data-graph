package lab1_205_12.uwaterloo.ca.lab1_205_12;

import android.hardware.*;
import android.widget.*;

/**
 * An abstract class encapsulating the properties common between sensor event listeners
 * that track scalar or vector sensor data and display these onto TextViews.
 */
public abstract class AbstractSensorEventListener implements SensorEventListener
{
    protected TextView currentReading = null; // an optional TextView onto which to display the latest reading
    protected TextView recordHigh = null; // an optional TextView onto which to display the record historical high reading
    protected final int SENSOR_TYPE; // the type of sensor to retrieve data from

    /**
     * Creates a sensor event listener to listen to sensor data and display these onto TextViews.
     *
     * @param currentReading An optional TextView to display the current reading onto.
     *                       Can be set to null.
     * @param recordHigh An optional TextView to display the record high reading.
     *                   Can be set to null.
     * @param sensorType The type of sensor to retrieve data from.
     */
    public AbstractSensorEventListener(TextView currentReading, TextView recordHigh, int sensorType)
    {
        this.currentReading = currentReading;
        this.recordHigh = recordHigh;
        SENSOR_TYPE = sensorType;
    }

    /**
     * Resets the record high reading to zero.
     */
    public abstract void resetRecordHighReading();

    public void onAccuracyChanged(Sensor s, int i){}

    /**
     * The class's implementation of sensor data reading and display.
     *
     * @param se The sensor event containing the retrieved sensor data.
     */
    public abstract void onSensorChanged(SensorEvent se);
}
