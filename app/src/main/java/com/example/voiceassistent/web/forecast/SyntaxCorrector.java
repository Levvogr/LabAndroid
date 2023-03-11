package com.example.voiceassistent.web.forecast;

public class SyntaxCorrector {
    static String correctTemperature(final int temperature){
        int temp=Math.abs(temperature)%100;
        if(temp<20&&temp>10){
            return "градусов";
        }
        temp%=10;
        if (temp==1) {
            return "градус";
        } else if (temp>=2&&temp<=4) {
            return "градуса";
        }
        return "градусов";
    }
}
