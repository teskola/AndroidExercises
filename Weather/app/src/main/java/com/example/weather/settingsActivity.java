package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class settingsActivity extends AppCompatActivity {

    private ToggleButton unitsButton;
    private String unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (getIntent().getStringExtra("unit") != null) {
            unit = getIntent().getStringExtra("unit");
        }
        unitsButton = findViewById(R.id.unitsButton);
        unitsButton.setChecked(unit.equals("metric"));
        unitsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    unitsButton.setText(R.string.metric);
                    unit = "metric";
                } else {
                    unitsButton.setText(R.string.imperial);
                    unit = "imperial";
                }
            }
        });

    }

    public void backToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("unit", unit);
        startActivity(intent);
    }
}