package com.example.voiceassistent.web.numbertoword;

import android.util.Log;

import androidx.core.util.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NumberToString {
    public static void getWord(Integer number,final Consumer<String> callback){
        NumberToWordApi api = NumberToWordService.getApi();
        Call<NumberToWord> call = api.getNumberToWord(number.toString());
        call.enqueue(new Callback<NumberToWord>() {

            @Override
            public void onResponse(Call<NumberToWord> call, Response<NumberToWord> response) {
                NumberToWord result=response.body();
                if (result!=null) {
                    callback.accept(result.number.substring(0,result.number.indexOf("рублей")-1));
                }else{
                    callback.accept("Не могу перевести число в слово");
                }
            }

            @Override
            public void onFailure(Call<NumberToWord> call, Throwable t) {
                Log.w("WEATHER",t.getMessage());
            }
        });
    }
}
