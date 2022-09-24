package com.example.painoindeksi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @SuppressLint("DefaultLocale")
    public void calculateBMI(View view) {
        TextView bmiTextView = findViewById(R.id.bmiTextView);
        EditText editPituus = findViewById(R.id.editPituus);
        EditText editPaino = findViewById(R.id.editPaino);
        float pituus = Float.parseFloat(editPituus.getText().toString());
        float paino = Float.parseFloat(editPaino.getText().toString());
        float bmi = paino / ((pituus / 100) * (pituus / 100));
        bmiTextView.setText(String.format("%.4g%n", bmi));
    }
}