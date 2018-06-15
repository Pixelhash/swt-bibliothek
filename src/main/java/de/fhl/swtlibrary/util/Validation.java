package de.fhl.swtlibrary.util;

import java.util.Arrays;
import java.util.regex.Pattern;

public final class Validation {

  // General patterns
  public static final Pattern NO_NUMBERS_PATTERN = Pattern.compile("^[^0-9]*$");
  public static final Pattern ONLY_NUMBERS_PATTERN = Pattern.compile("^[0-9]*$");

  // More precise patterns
  public static final Pattern IS_HOUSE_NUMBER_PATTERN = Pattern.compile("^\\d+[a-zA-Z]*$"); // https://stackoverflow.com/a/7784576
  public static final Pattern IS_CORRECT_DATE = Pattern.compile("(^(((0[1-9]|1[0-9]|2[0-8])[.](0[1-9]|1[012]))|((29|30|31)[.](0[13578]|1[02]))|((29|30)[.](0[4,6,9]|11)))[.](19|[2-9][0-9])\\d\\d$)|(^29[.]02[.](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)"); // https://stackoverflow.com/a/20773444

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
