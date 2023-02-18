package com.example.voiceassistent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

public class AI {
    private static final String dateQuestion="date";
    private static final HashMap<String, String> dictionary = new HashMap<>() {{
        put("привет", "Привет");
        put("приветик", "Привет");
        put("здравствуй", "Привет");
        put("здравствуйте", "Привет");
        put("как дела?", "Не плохо");
        put("чем занимаешься?", "Отвечаю на вопросы");
        put("а чем занимаешься?", "Отвечаю на вопросы");
        put("какой сегодня день?", "date");
        put("который час?", "date");
        put("какой сегодня день недели?", "date");
        put("сколько дней до нового года?", "date");
    }};

    public static String getAnswer(String inputText) {
        inputText = inputText.toLowerCase(Locale.ROOT);
        if (dictionary.containsKey(inputText)) {
            if(dictionary.get(inputText).equals(dateQuestion)){
                switch (inputText){
                    case "какой сегодня день?": return new SimpleDateFormat("d MMM").format(new Date());
                    case "который час?": return new SimpleDateFormat("H:m").format(new Date());
                    case "какой сегодня день недели?": return new SimpleDateFormat("EEEE").format(new Date());
                    case "сколько дней до нового года?": return new SimpleDateFormat("D").format(new Date(new GregorianCalendar(new Date().getYear()+1,0,0).getTime().getTime()-new Date().getTime()));
                    default: return "Не знаю";
                }
            }
            return dictionary.get(inputText);
        }
        return "В моём понимании эта фраза лишена смысла, поэтому я не буду отвечать.";
    }
}
