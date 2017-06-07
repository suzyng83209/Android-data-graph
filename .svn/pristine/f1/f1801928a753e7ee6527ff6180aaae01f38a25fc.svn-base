package lab1_205_12.uwaterloo.ca.lab1_205_12;

import android.hardware.*;

import ca.uwaterloo.sensortoy.LineGraphView;

/**
 * Listens to sensor events for a particular sensor type and plots
 * the data onto a specified line graph.
 */
public class SensorDataPlotter implements SensorEventListener
{
    private LineGraphView graph = null; // the line graph plotter to feed data to
    private final int SENSOR_TYPE; // the type of sensor to retrieve data from

    private float[] lastSensorReading = null; // the last sensor reading taken

    /**
     * Creates a SensorDataPlotter to populate the specified LineGraphView
     * with data from the specified sensor type.
     *
     * @param graph The LineGraphView object to feed data to.
     * @param sensorType The type of sensor to query data from.
     */
    public SensorDataPlotter(LineGraphView graph, int sensorType)
    {
        this.graph = graph;
        SENSOR_TYPE = sensorType;
    }

    public void onAccuracyChanged(Sensor s, int i){}

    /**
     * Adds a sensor data point to the line graph.
     *
     * @param se The SensorEvent from which the data point is to be obtained.
     */
    @Override
    public void onSensorChanged(SensorEvent se)
    {
        if(se.sensor.getType() == SENSOR_TYPE)
        {
            lastSensorReading = se.values;
            if(graph != null) graph.addPoint(lastSensorReading);
        }
    }
}
