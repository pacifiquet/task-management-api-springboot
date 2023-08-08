package com.pacifique.todoapp.config.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class OldTimeAdaptor {

    public static LocalDateTime localDateTime(Date date) {
        return (date == null)
            ? null
            : LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return localDateTime == null
            ? null
            : Date.from(
                localDateTime.atZone(ZoneId.systemDefault()).toInstant()
            );
    }
}
