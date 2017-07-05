package lab1_205_12.uwaterloo.ca.lab1_205_12;

import android.graphics.Color;
import android.hardware.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.*;

import java.util.Timer;

import static lab1_205_12.uwaterloo.ca.lab1_205_12.SensorDisplayUtilities.*;

/**
 * A utility app for displaying sensor readings with record highs,
 * plotting data and saving readings into CSV files.
 */
public class Lab1_205_12 extends AppCompatActivity {

    private static final String GESTURE_TITLE = "Gesture:";
    private static final int FPS = 144;
    private static final int GAME_PERIOD = 1000 / FPS;

    public static int boundaryMin = -46;
    public static int boundaryMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int DATA_POINTS = 100;
        int VECTOR_SIZE = 3;

        setContentView(R.layout.activity_lab1_205_12);

        // set up layout
        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        // screen dimensions retrieval from https://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int gameboardDimensions = metrics.widthPixels;
        boundaryMax = 3*gameboardDimensions/4 + boundaryMin;
        ImageView background = new ImageView(getApplicationContext());
        RelativeLayout.LayoutParams backgroundParams =
                new RelativeLayout.LayoutParams(gameboardDimensions, gameboardDimensions);
        background.setLayoutParams(backgroundParams);
        background.setImageResource(R.drawable.gameboard);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        createSensorEventListener(sensorManager, Sensor.TYPE_LINEAR_ACCELERATION);

        GestureFSM gestureX = new GestureFSM((float)0.5, (float)1.5, (float)-0.5, (float)-0.5, (float)-1.5, (float)2);
        GestureFSM gestureY = new GestureFSM((float)1, (float)11, (float)10, (float)-1, (float)9, (float)10);

        TextView gestureLabel = new TextView(getApplicationContext());
        gestureLabel.setTextSize(48);
        gestureLabel.setTextColor(Color.BLACK);
        gestureLabel.setText(GESTURE_TITLE);
        gestureLabel.setY(boundaryMax);

        myLayout.addView(background);
//        myLayout.addView(gestureLabel);

        // timer stuff
        Timer myAnimator = new Timer();
        GameLoopTask myAnimation = new GameLoopTask(this, myLayout, (getApplicationContext()));

        myAnimator.schedule(myAnimation, GAME_PERIOD, GAME_PERIOD);

        createSensorDataHandler(
                sensorManager,
                DATA_POINTS,
                VECTOR_SIZE,
                Sensor.TYPE_ACCELEROMETER,
                gestureX,
                gestureY,
                gestureLabel,
                myAnimation);
    }
}
