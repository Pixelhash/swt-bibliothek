package de.fhl.swtlibrary.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

  public static long getDaysBetween(Date start, Date end) {
    return TimeUnit.MILLISECONDS.toDays(end.getTime() - start.getTime());
  }

}
