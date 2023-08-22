package com.taskhero.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

public abstract class Utils {
  private static final int EXPIRATION_TIME = 10;

  private Utils() {}

  public static String generateUrl(HttpServletRequest request) {
    return ("http://" + request.getServerName() + ":" + request.getLocalPort() + "/api/v1/users/");
  }

  public static Date calculateTokenExpirationDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(new Date().getTime());
    calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
    return new Date(calendar.getTime().getTime());
  }
}
