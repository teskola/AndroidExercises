package com.example.painoindeksi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String mWeightUnit = "kg";
    float mLatestBmiResult = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mLatestBmiResult = savedInstanceState.getFloat("LATEST_BMI", 0.0f);
            mWeightUnit = savedInstanceState.getString("WEIGHT_UNIT", "kg");
        }

        if (getIntent().getStringExtra("WEIGHT_UNIT") != null) {
            mWeightUnit = getIntent().getStringExtra("WEIGHT_UNIT");
        }

        TextView painoTextView = findViewById(R.id.painoTextView);
        painoTextView.setText(getString(R.string.paino) + " " + mWeightUnit);
        TextView bmiTextView = findViewById(R.id.bmiTextView);
        bmiTextView.setText("" + mLatestBmiResult);

    }

    @Override
    public void onSaveInstanceState (Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("WEIGHT_UNIT", mWeightUnit);
        savedInstanceState.putFloat("LATEST_BMI", mLatestBmiResult);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @SuppressLint("DefaultLocale")
    public void calculateBMI(View view) {
        TextView bmiTextView = findViewById(R.id.bmiTextView);
        EditText editPituus = findViewById(R.id.editPituus);
        EditText editPaino = findViewById(R.id.editPaino);
        float pituus = Float.parseFloat(editPituus.getText().toString());
        float paino = Float.parseFloat(editPaino.getText().toString());
        float bmi = paino / ((pituus / 100) * (pituus / 100));
        mLatestBmiResult = bmi;
        bmiTextView.setText(String.format("%.4g%n", bmi));
    }

    public void openIntent(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}