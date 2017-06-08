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
     * Boolean constant to be passed to createSensorEventListener().
     * Tells it to create a ScalarSensorEventListener.
     */
    public static final boolean SCALAR = true;
    /**
     * Boolean constant to be passed to createSensorEventListener().
     * Tells it to create a VectorSensorEventListener.
     */
    public static final boolean VECTOR = false;

    /**
     * Helper method to create a TextView and add it to an existing layout.
     *
     * @param viewGroup The layout to add the TextView to.
     *
     * @return The newly created TextView.
     */
    public static TextView addTextView(Context context, ViewGroup viewGroup)
    {
        TextView textView = new TextView(context);
        viewGroup.addView(textView);
        return textView;
    }

    /**
     * Creates a TextView title label with initialized text.
     *
     * @param viewGroup The layout to add the TextView to.
     * @param title The title text of the label.
     *
     * @return The newly created title label.
     */
    public static TextView addTitleLabel(Context context, ViewGroup viewGroup, String title)
    {
        TextView titleLabel = addTextView(context, viewGroup);
        titleLabel.setText(title);
        return titleLabel;
    }

    /**
     * Helper method to create a button and add it to an existing layout.
     *
     * @param viewGroup The layout to add the button to.
     * @param title The button title text.
     *
     * @return The newly created button.
     */
    public static Button addButton(Context context, ViewGroup viewGroup, String title)
    {
        Button button = new Button(context);
        button.setText(title);
        viewGroup.addView(button);
        return button;
    }

    /**
     * Creates a sensor event listener for tracking and displaying sensor readings.
     *
     * @param sensorManager The system sensor manager.
     * @param currentReading A TextView on which to display the current reading.
     * @param recordHigh An optional TextView on which to display the record high component
     *                   readings. Can be left as null.
     * @param sensorType The type of sensor to retrieve and display data from.
     * @param scalarOrVector A boolean defining whether to create a ScalarSensorEventListener
     *                       or a VectorSensorEventListener; this decides between reading and
     *                       displaying sensor readings as scalar or vector data. This
     *                       categorization decreases code duplication.
     *                       Use the SCALAR and VECTOR boolean constants included in this
     *                       class to use the scalarOrVector parameter.
     *
     * @return The newly created and registered sensor event listener.
     */
    public static AbstractSensorEventListener createSensorEventListener(SensorManager sensorManager,
                                                                        TextView currentReading,
                                                                        TextView recordHigh,
                                                                        int sensorType,
                                                                        boolean scalarOrVector)
    {
        Sensor sensor = sensorManager.getDefaultSensor(sensorType);
        AbstractSensorEventListener sensorEventListener = (scalarOrVector) ?
                new ScalarSensorEventListener(currentReading, recordHigh, sensorType) :
                new VectorSensorEventListener(currentReading, recordHigh, sensorType);
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
     * @param filePathLabel A TextView for displaying the file path of the last reading saved.
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
                                                           TextView filePathLabel,
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
                filePathLabel,
                labelFormat,
                gestureX,
                gestureY,
                gestureLabel,
                graph);
        sensorManager.registerListener(sensorDataHandler, sensor, SensorManager.SENSOR_DELAY_GAME);
        return sensorDataHandler;
    }
}
