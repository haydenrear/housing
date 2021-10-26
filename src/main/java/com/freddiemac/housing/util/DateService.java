package com.freddiemac.housing.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.TimeZone;

@Service
@Log4j2
public class DateService {

    /**
     * returns date from format "yyyy-mm-dd" or December 25th 1980 if thrown.
     * @return
     */
    public static Date parseDate(String dateFormatyyyydashmmdashdd){
        var ld = LocalDate.parse(dateFormatyyyydashmmdashdd);
        var ldt = ld.atStartOfDay();
        return Date.from(ldt.toInstant(ZoneOffset.UTC));
    }

    public static Date parseDate(DateTimeFormatter formatter, String date)
    {
        TemporalAccessor parse = formatter.parse(date);
        return Date.from(Instant.from(parse).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date defaultDate() {
        return Date.from(Instant.ofEpochMilli(346612784000L));
    }
}
