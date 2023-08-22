package com.taskhero.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class OldTimeApiAdaptor {

  private OldTimeApiAdaptor() {
    throw new IllegalStateException("Utility class");
  }

  public static LocalDateTime localDateTime(Date date) {
    return (date == null)
        ? null
        : LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }

  public static Date toDate(LocalDateTime localDateTime) {
    return localDateTime == null
        ? null
        : Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }
}
