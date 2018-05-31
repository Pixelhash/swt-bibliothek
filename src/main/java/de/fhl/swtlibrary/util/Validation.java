package de.fhl.swtlibrary.util;

public final class Validation {

  public static Tuple<Boolean, Integer> isValidInt(final String intStr) {
    if (intStr == null || intStr.isEmpty()) return new Tuple<>(false, null);
    int res = -1;
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

}
