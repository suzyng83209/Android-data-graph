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
public class Lab1_205_12 extends AppCompatActivity {

    private static final String GESTURE_TITLE = "Current Gesture:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        GestureFSM gestureX = new GestureFSM((float)0.5, (float)1.5, (float)-0.5, (float)-0.5, (float)-1.5, (float)2);
        GestureFSM gestureY = new GestureFSM((float)1, (float)11, (float)10, (float)-1, (float)9, (float)10);

        TextView gestureLabel = new TextView(getApplicationContext());

        gestureLabel.setTextSize(100);
        gestureLabel.setText(GESTURE_TITLE);

        myLayout.addView(gestureLabel);

        createSensorDataLogger(
                sensorManager,
                DATA_POINTS,
                VECTOR_SIZE,
                Sensor.TYPE_ACCELEROMETER,
                gestureX,
                gestureY,
                gestureLabel);
    }
}
