package com.example.muweather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface WeatherService {
    @GET("weather")
    Call<WeatherResponse> getWeather(
            @Query("q") String location,
            @Query("appid") String apiKey,
            @Query("units") String units
    );
}