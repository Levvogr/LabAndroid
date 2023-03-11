package com.example.voiceassistent.parsejsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ParsingHtmlService {
    private static final String URL = "http://mirkosmosa.ru/holiday/{year}";
    /**
     *
     * @param date в формате dd MMMM yyyy, но в dd без незначащих нулей, например вместо 01 надо 1
     * @return
     */
    public static String getHoliday(String date) throws IOException {
        Document document = Jsoup.connect(URL.replace("{year}",
                date.substring(date.lastIndexOf(' ')+1))).get();
        Element body =document.body();
        Elements dates=body.getElementsByClass("month_cel_date");
        Elements holidays=body.getElementsByClass("month_cel");
        String answer="";
        for (int i = 0; i < dates.size(); i++) {
            if(dates.get(i).getElementsByTag("span").get(0).text().equals(date)){
                Element containerHolidaysInDay = holidays.get(i).getElementsByTag("ul").get(0);
                if(containerHolidaysInDay.childNodeSize()>0){
                    for (Element holiday:containerHolidaysInDay.getElementsByTag("a")) {
                        answer+=holiday.text()+"\n";
                    }
                    answer=answer.substring(0,answer.length()-1);
                    return answer;
                } else {
                    answer="В этот день нет праздников";
                    return answer;
                }
            }

        }
        return "В этот день нет праздников";
    }
}
