package com.example.flashlight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SwitchCompat switchCompat;
    ViewGroup switchContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        switchContainer = findViewById(R.id.swtichContainer);
        addSwitchButtons(cameraManager, findCamerasWithFlash(cameraManager), switchContainer);
    }

    public void addSwitchButtons(CameraManager cameraManager, ArrayList<String> cameras, ViewGroup viewGroup) {
        for (String id : cameras) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.camera_switch, viewGroup, false);
            view.setId(Integer.parseInt(id));
            viewGroup.addView(view);
            switchCompat = findViewById(Integer.parseInt(id));
            switchCompat.setText("Camera: " + id);
            switchCompat.setOnCheckedChangeListener((compoundButton, b) -> turnOnFlashLight(cameraManager, id, b));
        }
    }

    public ArrayList<String> findCamerasWithFlash(CameraManager cameraManager) {
        ArrayList<String> cameras = new ArrayList<>();
        try {
            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(id);
                if (cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                    cameras.add(id);
                }
            }
        }
        catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return cameras;
    }

    public void turnOnFlashLight(CameraManager cameraManager, String id, boolean b) {
        try
        {
            cameraManager.setTorchMode(id, b);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}