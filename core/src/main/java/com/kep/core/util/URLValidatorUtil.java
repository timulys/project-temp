package com.kep.core.util;

import java.util.Optional;
import java.util.regex.Pattern;

public class URLValidatorUtil {

  private static final String URL_REGEX =
      "^(https?|ftp):\\/\\/(?:[\\w.-]+\\.[a-zA-Z]{2,}|\\d{1,3}(?:\\.\\d{1,3}){3})(:\\d+)?(\\/.*)?$";
  private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

  public static boolean isValidURL(String url) {
    boolean matches = URL_PATTERN.matcher(url).matches();
    if(!matches) throw new IllegalArgumentException("Invalid URL");
    return true;
  }
}
