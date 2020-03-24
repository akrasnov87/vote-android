package ru.mobnius.vote.utils;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import ru.mobnius.vote.data.manager.Version;

import static org.junit.Assert.*;

public class DateUtilTest {

    @Test
    public void convertDateToUserString() {
        String userDateString = DateUtil.convertDateToUserString(Version.BIRTH_DAY);
        assertEquals("16.12.2019 00:00:00", userDateString);

        userDateString = DateUtil.convertDateToUserString(Version.BIRTH_DAY, DateUtil.USER_FORMAT);
        assertEquals("16.12.2019 00:00:00", userDateString);
    }

    @Test
    public void convertTimeToDate() {
        String time = "1254863245912";
        Date dt = DateUtil.convertTimeToDate(time);
        assertEquals(dt.getTime(), Long.parseLong(time));
    }

    @Test
    public void convertStringToDate() throws ParseException {
        Date dt = DateUtil.convertStringToDate("2009-05-12T12:30:50.958");
        assertEquals(dt.getTime(), Long.parseLong("1242117050958"));
    }

    @Test
    public void convertDateToString() {
        Date dt = new Date(Long.parseLong("1242117050958"));
        String str = DateUtil.convertDateToString(dt);
        assertEquals("2009-05-12T12:30:50.958", str);
    }

    @Test
    public void geenerateTid() {
        assertTrue(DateUtil.geenerateTid() > 0);
    }
}