package com.meepleengine.meepleserver.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    // TODO: add freeze time for testing
    private static ZoneId zoneId = ZoneId.of("UTC");

    public static LocalDateTime now() {
        return LocalDateTime.now(zoneId);
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return date != null ? Instant.ofEpochMilli(date.getTime()).atZone(zoneId).toLocalDateTime() : null;
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
