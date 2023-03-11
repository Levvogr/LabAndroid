package com.example.voiceassistent;

import static com.example.voiceassistent.AI.getDate;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.voiceassistent.parsejsoup.ParsingHtmlService;

import java.io.IOException;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void getHolidayIsCorrect() throws IOException {
        assertEquals(ParsingHtmlService.getHoliday("18 марта 2020"),
                "В этот день нет праздников");
        assertEquals(ParsingHtmlService.getHoliday("19 марта 2020"),
                "День моряка-подводника\n");
        assertEquals(ParsingHtmlService.getHoliday("20 марта 2020"),
                "Международный день без мяса\n" +
                "Международный день счастья\n" +
                "День французского языка\n" +
                "Международный день астрологии\n");

    }
    @Test
    public void getDateIsCorrect() throws IOException {
        assertEquals(getDate("сегодня"),
                "10 марта 2023");
        assertEquals(getDate("завтра"),
                "11 марта 2023");
        assertEquals(getDate("10.03.2023"),
                "10 марта 2023");
        assertEquals(getDate("01.03.2023"),
                "1 марта 2023");

    }
}