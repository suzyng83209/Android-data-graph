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
    private static final String LOG_CTA = "Log Accelerometer Data to CSV";
    private static final String GESTURE_TITLE = "Current Gesture:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LineGraphView graph;
        int DATA_POINTS = 100;
        int VECTOR_SIZE = 3;

        setContentView(R.layout.activity_lab1_205_12);

        // set up layout

        LinearLayout myLayout = (LinearLayout) findViewById(R.id.linearLayout);
        myLayout.setOrientation(LinearLayout.VERTICAL);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        createSensorEventListener(sensorManager, Sensor.TYPE_LINEAR_ACCELERATION);

        // screen dimensions retrieval from https://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // calculate line graph dimensions
        float axisWidthPercentage = 0.2f; // the width of the space to the left of the vertical axis as a percentage of the graph's width
        int lineGraphWidth = (int) (metrics.widthPixels * (1 - axisWidthPercentage));
        int lineGraphHeight = (int) (metrics.heightPixels * ((metrics.widthPixels < metrics.heightPixels) ? 0.7f : 0.5f));

        // create graph and add to layout
        graph = new LineGraphView(getApplicationContext(), DATA_POINTS, Arrays.asList("x", "y", "z"), lineGraphWidth, lineGraphHeight, axisWidthPercentage);
        graph.setVisibility(View.VISIBLE);

        GestureFSM gestureX = new GestureFSM((float)0.5, (float)1.5, (float)-0.5, (float)-0.5, (float)-1.5, (float)2);
        GestureFSM gestureY = new GestureFSM((float)1, (float)11, (float)10, (float)-1, (float)9, (float)10);

        TextView gestureLabel = new TextView(getApplicationContext());


        gestureLabel.setTextSize(100);
        gestureLabel.setText(GESTURE_TITLE);

        // button for logging graph

        Button logButton = new Button(getApplicationContext());
        logButton.setText(LOG_CTA);

        // set up accelerometer data logging button and output file label
        myLayout.addView(graph);
        myLayout.addView(gestureLabel);
        myLayout.addView(logButton);

        SensorDataHandler sensorDataHandler = createSensorDataLogger(sensorManager,
                DATA_POINTS,
                VECTOR_SIZE,
                Sensor.TYPE_ACCELEROMETER,
                getExternalFilesDir(LOG_FOLDER).getAbsolutePath(),
                LOG_FILE_NAME,
                LOG_FILE_PATH_LABEL_FORMAT,
                gestureX,
                gestureY,
                gestureLabel,
                graph);
        logButton.setOnClickListener(sensorDataHandler);
    }
}
