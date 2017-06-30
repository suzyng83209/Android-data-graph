package lab1_205_12.uwaterloo.ca.lab2_205_12;

import android.hardware.*;

/**
 * An implementation of AbstractSensorEventListener for retrieving and
 * displaying vector sensor data. Encapsulates the reading of all
 * types of sensors that return vector data; this reduces code duplication.
 */
public class VectorSensorEventListener extends AbstractSensorEventListener
{
    private float[] lastSensorReading = new float[3]; // the latest sensor reading
    private float[] recordHighReading = new float[3]; // the record historical high component readings

    // vector array component index constants; use to access components of vector reading
    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;

    /**
     * Creates a sensor event listener specifically for displaying vector
     * sensor data onto a TextView.
     * @param sensorType The type of sensor to retrieve data from.
     */
    public VectorSensorEventListener(int sensorType)
    {
        super(sensorType);
    }
    /**
     * Updates and displays the current and record historical high vector readings.
     *
     * @param se The sensor event containing the retrieved sensor data.
     */
    @Override
    public void onSensorChanged(SensorEvent se)
    {
        if(se.sensor.getType() == SENSOR_TYPE)
        {
            float[] v = lastSensorReading;
            float[] r = recordHighReading;

            // update record historical high components; tracks the maximum magnitudes
            // of each component individually
            for(int i = 0; i < 3; i++)
            {
                v[i] = se.values[i];
                if(Math.abs(v[i]) > Math.abs(r[i])) r[i] = v[i];
            }

            // update displayed current and record high readings
            if(currentReading != null) currentReading.setText(String.format("(%.2f, %.2f, %.2f)", v[X], v[Y], v[Z]));
            if(recordHigh != null) recordHigh.setText(String.format("(%.2f, %.2f, %.2f)", r[X], r[Y], r[Z]));
        }
    }
}
