package com.example.voiceassistent.message;

import java.util.Date;
import java.util.GregorianCalendar;

public class Message {
    public String text;
    public Date date;
    public Boolean isSend;

    public Message(String text, Boolean isSend) {
        this.text = text;
        this.isSend = isSend;
        this.date = new Date();
    }
    public Message(MessageEntity entity) {
        text= entity.text;
        GregorianCalendar entityDate=new GregorianCalendar();
        entityDate.set(GregorianCalendar.HOUR_OF_DAY,
                Integer.parseInt(entity.date.substring(0,entity.date.indexOf(':'))));
        entityDate.set(GregorianCalendar.MINUTE,
                Integer.parseInt(entity.date.substring(entity.date.indexOf(':')+1)));
        date=entityDate.getTime();
        if(entity.isSend==1){
            isSend=true;
        }else{
            isSend=false;
        }
    }
}
