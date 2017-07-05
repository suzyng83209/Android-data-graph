package lab1_205_12.uwaterloo.ca.lab1_205_12;

import android.graphics.Color;
import android.hardware.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.*;

import java.util.Timer;

import static lab1_205_12.uwaterloo.ca.lab1_205_12.SensorDisplayUtilities.*;

/**
 * A utility app for displaying sensor readings with record highs,
 * plotting data and saving readings into CSV files.
 */
public class Lab1_205_12 extends AppCompatActivity {

    private static final String GESTURE_TITLE = "Gesture:";
    private static final String BUTTON_TITLE = "RE-ORIENT";
    private static final int FPS = 60;
    private static final int GAME_PERIOD = 1000 / FPS;

    public static int boundaryMin = -30;
    public static int boundaryMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int DATA_POINTS = 100;
        int VECTOR_SIZE = 3;

        setContentView(R.layout.activity_lab1_205_12);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        createSensorEventListener(sensorManager, Sensor.TYPE_LINEAR_ACCELERATION);

        final GestureFSM gestureX = new GestureFSM(0.5f, 0.5f, -1.5f, -0.5f, -2.5f, 1f);
        final GestureFSM gestureY = new GestureFSM(0.5f, 0.5f, -1f, -0.5f, -1.5f, 1f);

        // screen dimensions retrieval from https://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int gameboardDimensions = Math.min(metrics.widthPixels, metrics.heightPixels);
        boundaryMax = 3*gameboardDimensions/4 + boundaryMin;
        RelativeLayout.LayoutParams backgroundParams =
                new RelativeLayout.LayoutParams(gameboardDimensions, gameboardDimensions);

        ImageView background = new ImageView(getApplicationContext());
        background.setLayoutParams(backgroundParams);
        background.setImageResource(R.drawable.gameboard);

        TextView gestureLabel = new TextView(getApplicationContext());
        gestureLabel.setTextColor(Color.WHITE);
        gestureLabel.setText(GESTURE_TITLE);
        gestureLabel.setTextSize(48);
        gestureLabel.setY(boundaryMax + 360);
        gestureLabel.setX((boundaryMax + boundaryMin) / 2);
        gestureLabel.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        Button reorientButton = new Button(getApplicationContext());
        reorientButton.setX((boundaryMax + boundaryMin) / 2 + 30);
        reorientButton.setY(boundaryMax + 240);
        reorientButton.setText(BUTTON_TITLE);
        reorientButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gestureX.resetThresholds();
                gestureY.resetThresholds();
            }
        });

        // set up layout
        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        myLayout.getLayoutParams().height = metrics.heightPixels;
        myLayout.getLayoutParams().width = metrics.widthPixels;
        myLayout.addView(background);
        myLayout.addView(reorientButton);
        myLayout.addView(gestureLabel);

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
