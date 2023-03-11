package com.example.voiceassistent.web.numbertoword;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NumberToWord implements Serializable {


    @SerializedName("str")
    @Expose
    public String number;

}