package de.fhl.swtlibrary.util;

import java.util.Arrays;
import java.util.regex.Pattern;

public final class Validation {

  // General patterns
  public static final Pattern NO_NUMBERS_PATTERN = Pattern.compile("^[^0-9]*$");
  public static final Pattern ONLY_NUMBERS_PATTERN = Pattern.compile("^[0-9]*$");

  // More precise patterns
  public static final Pattern IS_HOUSE_NUMBER_PATTERN = Pattern.compile("^\\d+[a-zA-Z]*$"); // https://stackoverflow.com/a/7784576

  public static Tuple<Boolean, Integer> isValidInt(final String intStr) {
    if (intStr == null || intStr.isEmpty()) return new Tuple<>(false, null);
    int res;
    try {
      res = Integer.parseInt(intStr);
    } catch (NumberFormatException e) {
      return new Tuple<>(false, null);
    }
    return new Tuple<>(true, res);
  }

  public static boolean isNonEmptyString(final String str) {
    return str != null && !str.isEmpty();
  }

  public static boolean isNonEmptyStringWithMinMaxLength(String str, int min, int max) {
    return isNonEmptyString(str)
      && str.length() >= min
      && str.length() <= max;
  }

  public static boolean isNonEmptyStringWithExactLength(String str, int exactLength) {
    return isNonEmptyStringWithMinMaxLength(str, exactLength, exactLength);
  }

  public static boolean isNonEmptyStringWithMinMaxLengthAndPatterns(String str, int min, int max, Pattern... patterns) {
    return isNonEmptyString(str)
      && str.length() >= min
      && str.length() <= max
      && Arrays.stream(patterns).allMatch((p) -> p.matcher(str).matches());
  }

  public static boolean isNonEmptyStringWithExactLengthAndPatterns(String str, int exactLength, Pattern... patterns) {
    return isNonEmptyStringWithMinMaxLengthAndPatterns(str, exactLength, exactLength, patterns);
  }

}
