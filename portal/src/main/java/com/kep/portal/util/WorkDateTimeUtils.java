package com.kep.portal.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkDateTimeUtils {

	
    final static Logger log = LoggerFactory.getLogger(WorkDateTimeUtils.class);
    final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ZonedDateTime stringToDateTime(String dateTime){
        return LocalDateTime.parse(dateTime, formatter).atZone(ZoneId.systemDefault());
    }

    public static String dateTimeToString(ZonedDateTime zonedDateTime){
        return zonedDateTime.format(formatter);
    }

    public static Map<String,String> times(String time){
        if (time == null)
            return Collections.emptyMap();

        List<String> items = Arrays.asList(time.split(" "));
        Map<String, String> item = new HashMap<>();
        item.put("sec",items.get(0));
        item.put("minutes",items.get(1));
        item.put("hours",items.get(2));
        item.put("days",items.get(3));
        item.put("month",items.get(4));
        item.put("dayOfWeek",items.get(5));
        item.put("year",items.get(6));
        return item;
    }

    public static boolean isDayOfWeek(String dayOfWeek) {
        try {
            String cron = "* * 0-23 ? * ".concat(dayOfWeek);
            CronExpression isDayOfWeek = new CronExpression(cron);
            return isDayOfWeek.isSatisfiedBy(new Date());
        } catch (ParseException e){
            log.error("DAY OF WEEK:{} , ERROR:{}",dayOfWeek , e.getMessage());
            return false;
        }
    }

    public static String dayOfWeek(List<String> dayOfWeek){
        if (dayOfWeek == null || dayOfWeek.isEmpty())
            return "";
        return String.join(",",dayOfWeek);
    }

    public static List<String> dayOfWeek(String dayOfWeek){
        if (dayOfWeek == null || dayOfWeek.isEmpty())
            return Collections.emptyList();
            return Stream.of(dayOfWeek.split(","))
                .collect(Collectors.toList());
    }

    public static String hours(String hour , String min){

        return hour.concat(":").concat(min);
    }

    /**
    * yyyy-MM-dd HH:mm
    * @param hours
    * @param minutes
    * @return
    */
    public static Date dateTime(String hours , String minutes){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ymd = simpleDateFormat.format(new Date());
        String dateTime = ymd.concat(" ").concat(hours).concat(":").concat(minutes);
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateTime);
        } catch (ParseException e){
            log.error("HOURS: {} , MINUTES:{} , DATETIME:{} , ERROR:{}" , hours, minutes , dateTime , e.getMessage());
            return null;
        }

    }

    public static Map<String,String> hours(String hours){
        List<String> items = Arrays.asList(hours.split(":"));
        Map<String, String> item = new HashMap<>();
        item.put("hours",items.get(0));
        item.put("minutes",items.get(1));
        return item;
    }
}
