package com.example.voiceassistent.web.numbertoword;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NumberToWordService {
    public static NumberToWordApi getApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://htmlweb.ru")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(NumberToWordApi.class);
    }
}
