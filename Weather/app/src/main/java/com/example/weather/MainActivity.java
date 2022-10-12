package com.example.weather;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "db665b34ad76791b17f190401a72755f";
    private String lat = "61.5";
    private String lon = "23.79";
    private String unit;
    boolean useDefaultUnits = true;
    private RequestQueue mQueue;
    private TextView temperatureTextView, feelsLikeTextView, windSpeedTextView, locationTextView;
    private ImageView windDirectionIcon, weatherIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getStringExtra("unit") != null) {
            useDefaultUnits = false;
            unit = getIntent().getStringExtra("unit");
        }
        if (useDefaultUnits) unit = getString(R.string.default_unit);
        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        feelsLikeTextView = findViewById(R.id.feelsLikeTextView);
        windSpeedTextView = findViewById(R.id.windSpeedTextView);
        locationTextView = findViewById(R.id.locationTextView);
        windDirectionIcon = findViewById(R.id.windDirectionIcon);
        weatherIcon = findViewById(R.id.weatherIcon);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchWeatherData();
    }

    private void fetchWeatherData() {
        Toast.makeText(getApplicationContext(), getString(R.string.fetch), Toast.LENGTH_SHORT).show();
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY + "&units=" + unit;
        JsonObjectRequest jsonObject = new JsonObjectRequest
                (Request.Method.GET, url, null,
                        this::parseJsonAndUpdateUI,
                        error -> Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_LONG).show());
        mQueue.add(jsonObject);
    }

    private void setWeatherIcon (int weatherIconId, long sun_rise, long sun_set) {

        // https://stackoverflow.com/questions/732034/getting-unixtime-in-java
        long unixTime = System.currentTimeMillis() / 1000L;
        boolean sunIsSet = unixTime <= sun_rise || unixTime >= sun_set;
        // https://openweathermap.org/weather-conditions
        if (weatherIconId < 300) {
            weatherIcon.setImageResource(R.drawable.rain_thunder);
            weatherIcon.setVisibility(VISIBLE);
        }
        else if (weatherIconId < 600) {
            weatherIcon.setImageResource(R.drawable.rain);
            weatherIcon.setVisibility(VISIBLE);
        }
        else if ((weatherIconId > 610) && (weatherIconId < 614)) {
            weatherIcon.setImageResource(R.drawable.sleet);
            weatherIcon.setVisibility(VISIBLE);
        }
        else if (weatherIconId < 700) {
            weatherIcon.setImageResource(R.drawable.snow);
            weatherIcon.setVisibility(VISIBLE);
        }
        else if (weatherIconId < 800) {
            weatherIcon.setImageResource(R.drawable.fog);
            weatherIcon.setVisibility(VISIBLE);
        }
        else if (weatherIconId < 801) {
            if (sunIsSet) {
                weatherIcon.setImageResource(R.drawable.night_half_moon_clear);
                weatherIcon.setVisibility(VISIBLE);
            } else {
                weatherIcon.setImageResource(R.drawable.day_clear);
                weatherIcon.setVisibility(VISIBLE);
            }
        }
        else if (weatherIconId < 803) {
            if (sunIsSet) {
                weatherIcon.setImageResource(R.drawable.night_half_moon_partial_cloud);
                weatherIcon.setVisibility(VISIBLE);
            } else {
                weatherIcon.setImageResource(R.drawable.day_partial_cloud);
                weatherIcon.setVisibility(VISIBLE);
            }
        }
        else if (weatherIconId < 804) {
            weatherIcon.setImageResource(R.drawable.cloudy);
            weatherIcon.setVisibility(VISIBLE);
        }
        else if (weatherIconId < 805) {
            weatherIcon.setImageResource(R.drawable.overcast);
            weatherIcon.setVisibility(VISIBLE);
        }
    }

    private void parseJsonAndUpdateUI (JSONObject weatherObject) {
        try {
            String location = weatherObject.getString("name");
            double temperature = weatherObject.getJSONObject("main").getDouble("temp");
            double feels_like = weatherObject.getJSONObject("main").getDouble("feels_like");
            double wind_speed = weatherObject.getJSONObject("wind").getDouble("speed");
            float wind_direction = (float) weatherObject.getJSONObject("wind").getInt("deg");
            int weatherIconId = weatherObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            long sun_rise = weatherObject.getJSONObject("sys").getLong("sunrise");
            long sun_set = weatherObject.getJSONObject("sys").getLong("sunset");
            String temp_unit;
            String speed_unit;
            if (unit.equals("imperial")) {
                temp_unit ="F";
                speed_unit ="mph";
            } else {
                temp_unit ="C";
                speed_unit ="m/s";
            }

            locationTextView.setText(location);
            locationTextView.setVisibility(VISIBLE);
            feelsLikeTextView.setText(getString(R.string.temperature, Math.round(feels_like),temp_unit));
            feelsLikeTextView.setVisibility(VISIBLE);
            temperatureTextView.setText(getString(R.string.temperature,Math.round(temperature), temp_unit));
            temperatureTextView.setVisibility(VISIBLE);
            windSpeedTextView.setText(getString(R.string.windSpeed, Math.round(wind_speed), speed_unit));
            windSpeedTextView.setVisibility(VISIBLE);
            // https://confluence.ecmwf.int/pages/viewpage.action?pageId=133262398
            windDirectionIcon.setRotation(360 - wind_direction);
            windDirectionIcon.setVisibility(VISIBLE);
            setWeatherIcon(weatherIconId, sun_rise, sun_set);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void openSettingsIntent(View view)  {
        Intent intent = new Intent(this, settingsActivity.class);
        intent.putExtra("unit", unit);
        startActivity(intent);
    }
}