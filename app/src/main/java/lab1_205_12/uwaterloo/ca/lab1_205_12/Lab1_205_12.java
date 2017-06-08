package lab1_205_12.uwaterloo.ca.lab1_205_12;

import android.content.Context;
import android.hardware.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;

import java.util.Arrays;

import ca.uwaterloo.sensortoy.LineGraphView;

import static lab1_205_12.uwaterloo.ca.lab1_205_12.SensorDisplayUtilities.*;

/**
 * A utility app for displaying sensor readings with record highs,
 * plotting data and saving readings into CSV files.
 */
public class Lab1_205_12 extends AppCompatActivity
{
    // log data file name string constants
    private static final String LOG_FOLDER = "Lab_1_Files";
    private static final String LOG_FILE_NAME = "Lab_1_Accelerometer_Data";
    private static final String LOG_FILE_PATH_LABEL_FORMAT = "Previous log stored at %s";

    // data logging constants
    private final int DATA_POINTS = 100; // the number of samples to log and store
    private final int VECTOR_SIZE = 3; // the number of components per data point

    // stores the instance of the line graph plotter
    private LineGraphView graph = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1_205_12);

        // store application context
        Context context = getApplicationContext();

        // set up layout

        LinearLayout myLayout = (LinearLayout) findViewById(R.id.linearLayout);
        myLayout.setOrientation(LinearLayout.VERTICAL);

        // add title
        TextView sensorReadingsTitle = addTitleLabel(context, myLayout, "**Sensor Readings Readout**:");

        // set up scalar sensor readouts

        TextView lightSensorTitle = addTitleLabel(context, myLayout, "Light Sensor Reading:");
        TextView lightSensorReading = addTextView(context, myLayout);

        TextView lightSensorRecordHighTitle = addTitleLabel(context, myLayout, "Light Sensor Record-High Reading:");
        TextView lightSensorRecordHigh = addTextView(context, myLayout);

        // set up vector sensor readouts

        TextView accelerometerTitle = addTitleLabel(context,myLayout, "Accelerometer Reading:");
        TextView accelerometerReading = addTextView(context, myLayout);

        TextView accelerometerMaxComponentsTitle = addTitleLabel(context, myLayout, "Accelerometer Record-High Components:");
        TextView accelerometerMaxComponents = addTextView(context, myLayout);

        TextView magneticFieldTitle = addTitleLabel(context, myLayout, "Magnetic Field Reading: ");
        TextView magneticFieldReading = addTextView(context, myLayout);

        TextView magneticFieldMaxComponentsTitle = addTitleLabel(context, myLayout, "Magnetic Field Record-High Components:");
        TextView magneticFieldMaxComponents = addTextView(context, myLayout);

        TextView rotationVectorTitle = addTitleLabel(context, myLayout, "Rotation Vector Reading:");
        TextView rotationVectorReading = addTextView(context, myLayout);

        TextView rotationVectorMaxComponentsTitle = addTitleLabel(context, myLayout, "Rotation Vector Record-High Components:");
        TextView rotationVectorMaxComponents = addTextView(context, myLayout);

        // set up sensor event listeners

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        final AbstractSensorEventListener lightSensorEventListener = createSensorEventListener(
                sensorManager, lightSensorReading, lightSensorRecordHigh, Sensor.TYPE_LIGHT, SCALAR);
        final AbstractSensorEventListener accelerometerEventListener = createSensorEventListener(
                sensorManager, accelerometerReading, accelerometerMaxComponents, Sensor.TYPE_LINEAR_ACCELERATION, VECTOR);
        final AbstractSensorEventListener magneticFieldEventListener = createSensorEventListener(
                sensorManager, magneticFieldReading, magneticFieldMaxComponents, Sensor.TYPE_MAGNETIC_FIELD, VECTOR);
        final AbstractSensorEventListener rotationVectorEventListener = createSensorEventListener(
                sensorManager, rotationVectorReading, rotationVectorMaxComponents, Sensor.TYPE_ROTATION_VECTOR, VECTOR);

        // set up sensor record-high readings reset button

        Button resetButton = addButton(context, myLayout, "Clear Record-High Readings");
        resetButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                lightSensorEventListener.resetRecordHighReading();
                accelerometerEventListener.resetRecordHighReading();
                magneticFieldEventListener.resetRecordHighReading();
                rotationVectorEventListener.resetRecordHighReading();
            }
        });

        // set up accelerometer vector line graph

        // add title
        TextView graphTitle = addTitleLabel(context, myLayout, "**Accelerometer Readings Line Graph**:");

        // screen dimensions retrieval from https://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // calculate line graph dimensions
        float axisWidthPercentage = 0.2f; // the width of the space to the left of the vertical axis as a percentage of the graph's width
        int lineGraphWidth = (int) (metrics.widthPixels * (1 - axisWidthPercentage));
        int lineGraphHeight = (int) (metrics.heightPixels * ((metrics.widthPixels < metrics.heightPixels) ? 0.7f : 0.5f));

        // create graph and add to layout
        graph = new LineGraphView(getApplicationContext(), DATA_POINTS, Arrays.asList("x", "y", "z"), lineGraphWidth, lineGraphHeight, axisWidthPercentage);
        myLayout.addView(graph);
        graph.setVisibility(View.VISIBLE);

        GestureFSM gestureX = new GestureFSM((float)0.5, (float)2, (float)-0.5, (float)-0.5, (float)-2, (float)3);
        GestureFSM gestureY = new GestureFSM((float)1.5, (float)12, (float)9, (float)-1.5, (float)8, (float)11);
        TextView gestureLabel = addTitleLabel(context, myLayout, "");

        // set up accelerometer data logging button and output file label

        Button logButton = addButton(context, myLayout, "Log Accelerometer Data to CSV");

        TextView filePathLabel = addTitleLabel(context, myLayout, String.format(LOG_FILE_PATH_LABEL_FORMAT, "N/A"));

        SensorDataHandler sensorDataHandler = createSensorDataLogger(sensorManager,
                DATA_POINTS,
                VECTOR_SIZE,
                Sensor.TYPE_ACCELEROMETER,
                getExternalFilesDir(LOG_FOLDER).getAbsolutePath(),
                LOG_FILE_NAME,
                filePathLabel,
                LOG_FILE_PATH_LABEL_FORMAT,
                gestureX,
                gestureY,
                gestureLabel,
                graph);
        logButton.setOnClickListener(sensorDataHandler);
    }
}
