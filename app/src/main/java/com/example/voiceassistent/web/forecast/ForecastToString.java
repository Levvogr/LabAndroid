package com.example.voiceassistent.web.forecast;

import android.util.Log;


import androidx.core.util.Consumer;

import com.example.voiceassistent.web.numbertoword.NumberToString;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastToString {
    public static void getForecast(String city,final Consumer<String> callback){
        ForecastApi api = ForecastService.getApi();
        Call<Forecast> call = api.getCurrentWeather(city);
        call.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                Forecast result = response.body();
                if (result!=null) {
                    NumberToString.getWord(Math.abs(result.current.temperature), new Consumer<String>() {
                        @Override
                        public void accept(String number) {
                            if(number!=null){
                                if(result.current.temperature<0){
                                    callback.accept("сейчас где-то минус " + number +
                                            " "+SyntaxCorrector.correctTemperature(result.current.temperature)+
                                            " " + " и " + result.current.weather_descriptions.get(0));
                                }else{
                                    callback.accept("сейчас где-то " + number +
                                            " "+SyntaxCorrector.correctTemperature(result.current.temperature)+
                                            " " + " и " + result.current.weather_descriptions.get(0));
                                }
                            }else{
                                callback.accept("сейчас где-то " + result.current.temperature +
                                        " "+SyntaxCorrector.correctTemperature(result.current.temperature)+
                                        " " + " и " + result.current.weather_descriptions.get(0));
                            }

                        }
                    });
                    /*String answer = "сейчас где-то " + result.current.temperature +
                            " "+SyntaxCorrector.correctTemperature(result.current.temperature)+
                            " " + " и " + result.current.weather_descriptions.get(0);
                    callback.accept(answer);*/
                }else{
                    callback.accept("Не могу узнать погоду");
                }
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                Log.w("WEATHER",t.getMessage());
            }
        });
    }
}
