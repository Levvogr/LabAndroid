package com.example.voiceassistent.message;

import java.text.SimpleDateFormat;

public class MessageEntity {
    public String text;
    public String  date;
    public int isSend;
    public MessageEntity(String text, String  date, int isSend){
        this.text=text;
        this.date=date;
        this.isSend=isSend;
    }

    public MessageEntity(Message message){
        text= message.text;
        date=new SimpleDateFormat("H:m").format(message.date.getTime());
        if(message.isSend){
            isSend=1;
        }else{
            isSend=0;
        }
    }

}
