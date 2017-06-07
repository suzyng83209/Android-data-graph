package lab1_205_12.uwaterloo.ca.lab1_205_12;

import android.hardware.*;
import android.widget.*;

/**
 * An implementation of AbstractSensorEventListener for retrieving and
 * displaying scalar sensor data. Encapsulates the reading of all
 * types of sensors that return scalar data; this reduces code duplication.
 */
public class ScalarSensorEventListener extends AbstractSensorEventListener
{
    private float lastSensorReading = 0; // the latest sensor reading
    private float recordHighReading = 0; // the record historical high

    /**
     * Creates a sensor event listener specifically for displaying scalar
     * sensor data onto a TextView.
     *
     * @param currentReading An optional TextView to display the current reading onto.
     *                       Can be set to null.
     * @param recordHigh An optional TextView to display the record high reading.
     *                   Can be set to null.
     * @param sensorType The type of sensor to retrieve data from.
     */
    public ScalarSensorEventListener(TextView currentReading, TextView recordHigh, int sensorType)
    {
        super(currentReading, recordHigh, sensorType);
    }

    /**
     * Sets the record historical high reading to zero.
     */
    @Override
    public void resetRecordHighReading()
    {
        recordHighReading = 0;
    }

    /**
     * Updates the assigned TextView with the latest scalar sensor reading.
     *
     * @param se The sensor event containing the retrieved sensor data.
     */
    @Override
    public void onSensorChanged(SensorEvent se)
    {
        if(se.sensor.getType() == SENSOR_TYPE)
        {
            // update current and record high readings
            lastSensorReading = se.values[0];
            if(Math.abs(lastSensorReading) > Math.abs(recordHighReading)) recordHighReading = lastSensorReading;

            // update displayed current and record high readings
            if(currentReading != null) currentReading.setText(String.format("%.2f", lastSensorReading));
            if(recordHigh != null) recordHigh.setText(String.format("%.2f", recordHighReading));
        }
    }
}
