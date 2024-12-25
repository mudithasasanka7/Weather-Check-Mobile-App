package com.example.muweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class MainActivity extends AppCompatActivity {

    private EditText locationInput;
    private Button searchButton;
    private LinearLayout weatherInfo;
    private TextView tempText, humidityText, conditionText;

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "c018cb5b0f2375eaf866e58c53cf52a0"; // Replace with your API key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationInput = findViewById(R.id.locationInput);
        searchButton = findViewById(R.id.searchButton);
        weatherInfo = findViewById(R.id.weatherInfo);
        tempText = findViewById(R.id.tempText);
        humidityText = findViewById(R.id.humidityText);
        conditionText = findViewById(R.id.conditionText);

        searchButton.setOnClickListener(v -> fetchWeather(locationInput.getText().toString()));
    }

    private void fetchWeather(String location) {
        if (location.isEmpty()) {
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService weatherService = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = weatherService.getWeather(location, API_KEY, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();
                    weatherInfo.setVisibility(View.VISIBLE);
                    tempText.setText("Temperature: " + weather.getMain().getTemp() + "°C");
                    humidityText.setText("Humidity: " + weather.getMain().getHumidity() + "%");
                    conditionText.setText("Condition: " + weather.getWeather().get(0).getDescription());
                } else {
                    Toast.makeText(MainActivity.this, "Location not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}