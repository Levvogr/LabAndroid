package com.example.voiceassistent.web.numbertoword;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NumberToWordApi {
    @GET("/json/convert/num2str")
    Call<NumberToWord> getNumberToWord(@Query("num") String number);
}
