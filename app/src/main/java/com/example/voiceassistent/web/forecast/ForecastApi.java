package com.example.voiceassistent.web.forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ForecastApi {
    @GET("/current?access_key=cdbf7bda5c364456bbe7ddcea9a06d3b")
    Call<Forecast> getCurrentWeather(@Query("query") String city);
}
