package com.example.voiceassistent;

import androidx.core.util.Consumer;

import com.example.voiceassistent.parsejsoup.ParsingHtmlService;
import com.example.voiceassistent.web.forecast.ForecastToString;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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

    public static void getAnswer(String inputText,final Consumer<String> callback) {
        final String[] answers = {""};
        inputText = inputText.toLowerCase(Locale.ROOT);
        Pattern cityPattern = Pattern.compile("погода в городе (\\p{L}+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = cityPattern.matcher(inputText);
        if (matcher.find()){
            String cityName = matcher.group(1);
            cityName=cityName.substring(0,1).toUpperCase(Locale.ROOT)+cityName.substring(1);
            ForecastToString.getForecast(cityName, new Consumer<String>() {
                @Override
                public void accept(String s) {
                    answers[0] = s;
                    callback.accept(String.join(", ", answers));
                }
            });
           /* if(answers[0].equals("")){
                answers[0] = "Не знаю я, какая там погода у вас в городе "+cityName;
            }*/
        }else if (inputText.indexOf("праздник")!=-1){
            /*new AsyncTask<String, Integer, Void>() {
                @Override
                protected Void doInBackground(String... strings) {
                    return null;
                }
            }.execute()*/
            String finalInputText = inputText;
            Observable.fromCallable(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    //finalInputText.indexOf("праздник")+9 при таком вычислении даты
                    // могут возникнуть ошибки, лучше поменять
                    answers[0] = ParsingHtmlService.getHoliday(getDate
                            (finalInputText.substring(finalInputText.indexOf("праздник")+9)));
                    return answers;
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((result) -> {
                        callback.accept(String.join(", ", answers));
            });

        }else if (dictionary.containsKey(inputText)) {
            if(dictionary.get(inputText).equals(dateQuestion)){
                switch (inputText){
                    case "какой сегодня день?": answers[0] =  new SimpleDateFormat("d MMM").format(new Date()); break;
                    case "который час?": answers[0] =  new SimpleDateFormat("H:m").format(new Date()); break;
                    case "какой сегодня день недели?": answers[0] =  new SimpleDateFormat("EEEE").format(new Date()); break;
                    case "сколько дней до нового года?": answers[0] =  new SimpleDateFormat("D").format(new Date(new GregorianCalendar(new Date().getYear()+1,0,0).getTime().getTime()-new Date().getTime())); break;
                    default: answers[0] =  "Не знаю"; break;
                }
            }else{
                answers[0] =  dictionary.get(inputText);
            }
            callback.accept(String.join(", ", answers));
        }else if(answers[0].equals("")){
            answers[0] =  "В моём понимании эта фраза лишена смысла, поэтому я не буду отвечать.";
            callback.accept(String.join(", ", answers));
        }
    }
    static String getDate(String dateQuestion){
        String answer=" ";
        if(dateQuestion.indexOf("сегодня")!=-1){
            answer = new SimpleDateFormat("dd MMMM yyyy").format(new Date());
        } else if (dateQuestion.indexOf("завтра")!=-1) {
            //86400000 - это сутки в миллисекундах
            answer = new SimpleDateFormat("dd MMMM yyyy")
                    .format(new Date(new Date().getTime()+86400000));
        } else if (dateQuestion.indexOf("вчера")!=-1) {
            answer = new SimpleDateFormat("dd MMMM yyyy")
                    .format(new Date(new Date().getTime()-86400000));
        } else if (Pattern.matches("[0-9][0-9]\\.[0-9][0-9]\\.[0-9][0-9][0-9][0-9]",
                dateQuestion)) {
            int lastSignMonth=dateQuestion.lastIndexOf('.')-1;
            String month=dateQuestion.substring(lastSignMonth,lastSignMonth+1);
            if(!dateQuestion.substring(lastSignMonth-1,lastSignMonth).equals("0")){
                month=dateQuestion.substring(lastSignMonth-1,lastSignMonth)+month;
            }
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Integer.parseInt(month)-1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer
                    .parseInt(dateQuestion.substring(lastSignMonth-4,lastSignMonth-2)));
            calendar.set(Calendar.YEAR,Integer
                    .parseInt(dateQuestion.substring(lastSignMonth+2,lastSignMonth+6)));
            answer = new SimpleDateFormat("dd MMMM yyyy").format(calendar.getTime());
        }
        if(answer.indexOf("0")==0){
            answer=answer.substring(1);
        }
        return answer;
    }
}
