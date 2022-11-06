package dev.slasher.smartplugins.utils;

import java.util.Calendar;

/**
 * Class with TimeMillis-related utilities and {@link Calendar}.
 */
public class TimeUtils {

  /**
   * Analyzes whether the current date is New Year's.
   *
   * @return TRUE if so, FALSE if not.
   */
  public static boolean isNewYear() {
    Calendar cl = Calendar.getInstance();
    return (cl.get(Calendar.MONTH) == Calendar.DECEMBER && cl.get(Calendar.DATE) == 31) || (cl.get(Calendar.MONTH) == Calendar.JANUARY && cl.get(Calendar.DATE) == 1);
  }

  /**
   * Analyzes if the current date is Christmas.
   *
   * @return TRUE if it is, FALSE if not.
   */
  public static boolean isChristmas() {
    Calendar cl = Calendar.getInstance();
    return cl.get(Calendar.MONTH) == Calendar.DECEMBER && (cl.get(Calendar.DATE) == 24 || cl.get(Calendar.DATE) == 25);
  }

  /**
   * Requires the last date of the month from its number.
   *
   * @param month Month 1 to 12
   * @return the last day of the chosen month.
   */
  public static int getLastDayOfMonth(int month) {
    Calendar cl = Calendar.getInstance();
    cl.set(Calendar.MONTH, month - 1);
    return cl.getActualMaximum(Calendar.DATE);
  }

  /**
   * Requires the last date of the current month.
   *
   * @return the last day of the month.
   */
  public static int getLastDayOfMonth() {
    return Calendar.getInstance().getActualMaximum(Calendar.DATE);
  }

  /**
   * Create a date from the current one to expire after the requested days.
   *
   * @param days Number of days from today.
   * @return The expiration date.
   */
  public static long getExpireIn(int days) {
    Calendar cooldown = Calendar.getInstance();
    cooldown.set(Calendar.HOUR, 24);
    for (int day = 0; day < days - 1; day++) {
      cooldown.add(Calendar.HOUR, 24);
    }
    cooldown.set(Calendar.MINUTE, 0);
    cooldown.set(Calendar.SECOND, 0);

    return cooldown.getTimeInMillis();
  }

  /**
   * Get how much time is left between a timemillis and the current one.<br/>
   * Note: {@link #getTimeUntil(long, boolean)} shortener with seconds enabled.
   *
   * @param epoch Timemillis to get the remaining time.
   * @return The remaining or empty time (<code>""</code>) if time has passed.
   */
  public static String getTimeUntil(long epoch) {
    return getTimeUntil(epoch, true);
  }

  /**
   * Get how much time is left between a timemillis and the current one.
   *
   * @param epoch Timemillis to get the remaining time.
   * @param seconds Whether to show the seconds in the time remaining.
   * @return The remaining or empty time (<code>""</code>) if time has passed.
   */
  public static String getTimeUntil(long epoch, boolean seconds) {
    epoch -= System.currentTimeMillis();
    return getTime(epoch, seconds);
  }

  /**
   * Transforms a timemillis into a String to know how many days, hours, minutes and seconds are in the timemillis.<br/>
   * Note: {@link #getTime(long, boolean)} shortener with seconds enabled.
   *
   * @param time Timemillis to catch the time.
   * @return Time in String or empty (<code>""</code>) case <code>time <= 0</code>
   */
  public static String getTime(long time) {
    return getTime(time, true);
  }

  /**
   * Transforms a timemillis into a String to know how many days, hours, minutes and seconds there are in the timemillis.
   *
   * @param time Timemillis to catch the time.
   * @param seconds If it will show the seconds in time.
   * @return Time in String or empty (<code>""</code>) case <code>time <= 0</code>
   */
  public static String getTime(long time, boolean seconds) {
    long ms = time / 1000;
    if (ms <= 0) {
      return "";
    }

    StringBuilder result = new StringBuilder();
    long days = ms / 86400;
    if (days > 0) {
      result.append(days).append("d");
      ms -= days * 86400;
      if (ms / 3600 > 0) {
        result.append(" ");
      }
    }
    long hours = ms / 3600;
    if (hours > 0) {
      result.append(hours).append("h");
      ms -= hours * 3600;
      if (ms / 60 > 0) {
        result.append(" ");
      }
    }
    long minutes = ms / 60;
    if (minutes > 0) {
      result.append(minutes).append("m");
      ms -= minutes * 60;
      if (ms > 0 && seconds) {
        result.append(" ");
      }
    }
    if (seconds) {
      if (ms > 0) {
        result.append(ms).append("s");
      }
    }

    return result.toString();
  }
}
