package com.udemy.multitenancy.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

public class Util {

    public Logger logger = Logger.getLogger("Util");

    public static Date getStartDate(Date date) {

        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);

        return (Date) date.clone();

    }



    public static Date getEndDate(Date date) {

        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);


        return (Date) date.clone();
    }







}
