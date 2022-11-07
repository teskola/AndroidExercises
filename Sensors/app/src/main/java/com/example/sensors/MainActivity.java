package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;


public class MainActivity extends AppCompatActivity implements SensorEventListener{
    public static final int DELTA = 20;
    private OrientationView orientationView;
    private final Handler handler = new Handler();
    float x_vel = 0f, x_acc = 0f, y_vel = 0f, y_acc = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        orientationView = findViewById(R.id.orientationView);

        startSensors();

        handler.post(new Runnable() {
            @Override
            public void run() {
                x_vel = x_vel + x_acc;
                y_vel = y_vel + y_acc;
                orientationView.setX(orientationView.getX() + x_vel + 0.5f * x_acc);
                orientationView.setY(orientationView.getY() + y_vel + 0.5f * y_acc);
                orientationView.invalidate();
                handler.postDelayed(this, DELTA);
            }
        });


    }
    public void startSensors() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
//        for (Sensor sensor : sensorList) {
//            Toast.makeText(this, sensor.getName(), Toast.LENGTH_SHORT).show();
//        }

        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        }

//        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        if (accelerometer != null) {
//            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
//        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float ySensor = sensorEvent.values[1];
        float xSensor = sensorEvent.values[2];
        x_acc = -0.01f * xSensor;
        y_acc = -0.01f * ySensor;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}