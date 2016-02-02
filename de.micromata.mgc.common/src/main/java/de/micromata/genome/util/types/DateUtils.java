/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    noodles@micromata.de
// Created   20.12.2006
// Copyright Micromata 20.12.2006
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.types;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * Collection of methods to handle dates.
 *
 * @author roger@micromata.de
 */
public class DateUtils
{

  /**
   * The Constant MINUTES_PER_DAY.
   */
  public static final int MINUTES_PER_DAY = 24 * 60;

  /**
   * The Constant ddMMyyyy_DATE_FORMAT.
   */
  public static final DateFormat ddMMyyyy_DATE_FORMAT = new SimpleDateFormat("ddMMyyyy");

  /**
   * Locale?
   *
   * @return Eine neue Instanz.
   */
  private static Calendar getCalendarInstance()
  {
    final Calendar cal = Calendar.getInstance();
    return cal;
  }

  /**
   * Arbeitet über die Millisekunden.
   *
   * @param date Nie <code>null</code>.
   * @param days Positive und negative Zahlen erlaubt.
   * @return Das neue Datum.
   */
  public static Date addDays(final Date date, final int days)
  {
    Validate.notNull(date, "date not set");

    final Calendar cal = getCalendarInstance();
    cal.setTime(date);
    cal.add(Calendar.DAY_OF_YEAR, days);
    final Date result = cal.getTime();
    return result;
  }

  /**
   * Arbeitet über die Millisekunden.
   *
   * @param date Nie <code>null</code>.
   * @param millis Positive und negative Zahlen erlaubt. Muss im IntRange-Bereich liegen!
   * @return Das neue Datum.
   */
  public static Date addTics(final Date date, final long millis)
  {
    if (millis < Integer.MIN_VALUE || Integer.MAX_VALUE < millis) {
      throw new IllegalArgumentException("millis out of range: " + millis);
    }
    final Calendar cal = getCalendarInstance();
    cal.setTime(date);
    cal.add(Calendar.MILLISECOND, (int) millis);
    final Date result = cal.getTime();
    return result;
  }

  /**
   * Addiert einen Monat.
   *
   * @param date the date
   * @param month the month
   * @return the date
   */
  public static Date addMonth(Date date, int month)
  {
    final Calendar cal = getCalendarInstance();
    cal.setTime(date);
    cal.add(Calendar.MONTH, month);
    final Date result = cal.getTime();
    return result;
  }

  /**
   * If one of the date is null, it returns the other.
   *
   * @param left the left
   * @param right the right
   * @return smaller of both dates.
   */
  public static Date min(final Date left, final Date right)
  {
    if (left == null) {
      return right;
    }
    if (right == null) {
      return left;
    }
    if (left.after(right) == true) {
      return right;
    } else {
      return left;
    }
  }

  /**
   * If one of the date is null, it returns the other.
   *
   * @param left the left
   * @param right the right
   * @return later of both dates.
   */
  public static Date max(final Date left, final Date right)
  {
    if (left == null) {
      return right;
    }
    if (right == null) {
      return left;
    }
    if (left.after(right) == true) {
      return left;
    } else {
      return right;
    }
  }

  /**
   * Berechnet die Minuten im angegebenen Datum.
   *
   * @param date Nie <code>null</code>.
   * @return Wert im Bereich 0 bis 24*60-1.
   */
  public static int getMinutesInDay(Date date)
  {
    Validate.notNull(date, "date not set");

    final Calendar cal = getCalendarInstance();
    cal.setTime(date);

    final int minutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
    return minutes;
  }

  /**
   * Parses the h h_mm to minutes.
   *
   * @param time Zeitangabe der Form "12:23". Minimalwert "00:00", Maximalwert "24:00".
   * @return Minutenangabe der Tageszeit.
   */
  public static int parseHH_mmToMinutes(String time)
  {
    Validate.notEmpty(time, "time is empty");
    String[] tokens = StringUtils.split(time.trim(), ":");
    Validate.isTrue(tokens.length == 2, "Wrong format; 2 values separated by ':' expected");

    int hours;
    int minutes;

    hours = Integer.parseInt(tokens[0]);
    Validate.isTrue(hours >= 0, "hours negative");
    Validate.isTrue(hours <= 24, "hours too big");

    minutes = Integer.parseInt(tokens[1]);
    Validate.isTrue(minutes >= 0, "minutes negative");
    Validate.isTrue(minutes <= 60, "minutes too big");

    final int result = hours * 60 + minutes;
    Validate.isTrue(result <= 24 * 60, "result too big");
    return result;
  }

  /**
   * Parses the h hmm to minutes.
   *
   * @param time Zeitangabe der Form "1223". Minimalwert "0000", Maximalwert "2400".
   * @return Minutenangabe der Tageszeit.
   */
  public static int parseHHmmToMinutes(String time)
  {
    Validate.notEmpty(time, "time is empty");

    try {
      Validate.isTrue(time.length() == 4, "time string expected to be 4 digits, but was ", time);

      String hoursPart = StringUtils.left(time, 2);
      String minutesPart = StringUtils.right(time, 2);

      int hours;
      int minutes;

      hours = Integer.parseInt(hoursPart);
      Validate.isTrue(hours >= 0, "hours negative");
      Validate.isTrue(hours <= 24, "hours too big");

      minutes = Integer.parseInt(minutesPart);
      Validate.isTrue(minutes >= 0, "minutes negative");
      Validate.isTrue(minutes <= 60, "minutes too big");

      final int result = hours * 60 + minutes;
      Validate.isTrue(result <= 24 * 60, "result too big");
      return result;
    } catch (Exception e) {
      throw new RuntimeException("Error parsing time string '" + time + "': " + e);
    }
  }

  /**
   * Formatiert die angegebene Minutenzahl in die Form "HH:mm".
   *
   * @param minutes Positiver Wert.
   * @return Nie <code>null</code>. Maximal "24:00", sonst IllegalArgumentException
   */
  public static String formatHH_mm(int minutes)
  {
    Validate.isTrue(minutes >= 0, "minutes negative: ", minutes);
    Validate.isTrue(minutes <= 60 * 24, "minutes exceeding dayrange: ", minutes);

    final String result = String.format("%d:%02d", minutes / 60, minutes % 60);
    return result;
  }

  /**
   * Datum formatieren analog zu "31122007).
   *
   * @param date .
   * @return Nie <code>null</code>.
   */
  public static String formatDDMMYYYY(Date date)
  {
    final String result = ddMMyyyy_DATE_FORMAT.format(date);
    return result;
  }

  /**
   * Formatiert die angegebene Minutenzahl in die Form "HHmm".
   *
   * @param minutes Positiver Wert.
   * @return Nie <code>null</code>. Maximal "2400", sonst IllegalArgumentException
   */
  public static String formatHHmm(int minutes)
  {
    Validate.isTrue(minutes >= 0, "minutes negative: ", minutes);
    Validate.isTrue(minutes <= 60 * 24, "minutes exceeding dayrange: ", minutes);

    final String result = String.format("%02d%02d", minutes / 60, minutes % 60);
    return result;
  }

  /**
   * Minuten zu gegebenem Datum addieren.
   *
   * @param date Nie <code>null</code>
   * @param minutes Positiv oder negativ.
   * @return the date
   */
  public static Date addMinutes(Date date, int minutes)
  {
    Validate.notNull(date, "date not set");

    final Calendar cal = getCalendarInstance();
    cal.setTime(date);
    cal.add(Calendar.MINUTE, minutes);
    final Date result = cal.getTime();
    return result;
  }

  /**
   * Die Uhrzeit aus dem gegebenen Datum entfernen und nur den reinen Datumsanteil übrig lassen.
   *
   * @param timestamp Nie <code>null</code>.
   * @return the day of date
   */
  public static Date getDayOfDate(Date timestamp)
  {
    Validate.notNull(timestamp, "date not set");

    final Calendar cal = getCalendarInstance();
    cal.setTime(timestamp);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    final Date result = cal.getTime();
    return result;
  }

  /**
   * Prüfen, ob das gegebene Datum innerhalb der gegebenen Tage liegt.
   *
   * @param date Nie <code>null</code>.
   * @param dayIndices Konstanten der Form <code>Calendar.SUNDAY</code>.
   * @return <code>true</code>, falls das Datum in den Tagen enthalten ist.
   */
  public static boolean isWithinDays(Date date, Integer... dayIndices)
  {
    Validate.notNull(date, "date not set");

    final Calendar cal = Calendar.getInstance();
    cal.setTime(date);

    final Integer dayOfWeek = cal.get(GregorianCalendar.DAY_OF_WEEK);
    boolean result = false;

    for (Integer dayIndex : dayIndices) {
      if (dayOfWeek.equals(dayIndex) == true) {
        result = true;
        break;
      }
    } // for

    return result;
  }

  /**
   * prüfen, ob das gegebene Datum in bestimmten tagen liegt.
   *
   * @param date the date
   * @param dayList Kann <code>null</code> sein (liefert dann false als Ergebnis). Konstanten analog Klasse
   *          <code>Calendar</code>
   * @return <code>true</code>, wenn das Datum in einem der spez. Tage liegt.
   */
  public static boolean isInDayList(Date date, int... dayList)
  {
    Validate.notNull(date, "date not set");

    final Calendar cal = getCalendarInstance();
    cal.setTime(date);
    final int dayIndex = cal.get(Calendar.DAY_OF_WEEK);

    for (int day : dayList) {
      if (day == dayIndex) {
        return true;
      }
    }
    return false;

  }

  /**
   * Parsedd m myyyy.
   *
   * @param dateStr the date str
   * @return the date
   * @throws ParseException the parse exception
   */
  public static Date parseddMMyyyy(String dateStr) throws ParseException
  {
    final Date result = ddMMyyyy_DATE_FORMAT.parse(dateStr);
    return result;
  }

  /**
   * Kopie des Dates als Date zurückliefern (<code>null</code>-save).
   *
   * @param date the date
   * @return the date
   */
  public static Date clone(Date date)
  {
    if (date == null) {
      return null;
    }

    return (Date) date.clone();
  }

  /**
   * Ensure workday.
   *
   * @param cal the cal
   */
  protected static void ensureWorkday(Calendar cal)
  {
    int dow = cal.get(Calendar.DAY_OF_WEEK);
    while (dow == Calendar.SATURDAY || dow == Calendar.SUNDAY) {
      cal.add(Calendar.DAY_OF_YEAR, 1);
      dow = cal.get(Calendar.DAY_OF_WEEK);
    }
  }

  /**
   * Die Anzahl Arbeitstage (d.h. nicht Samstag und Sonntag) addieren.
   *
   * @param date Nie <code>null</code>
   * @param workdayOffset Offset in Tagen (1=morgen)
   * @return Nie <code>null</code> (Tagesdatum ohne
   */
  public static Date addWorkdays(Date date, int workdayOffset)
  {
    final Calendar cal = getCalendarInstance();
    cal.setTime(date);

    ensureWorkday(cal);
    for (int countdown = workdayOffset; countdown > 0; countdown--) {
      cal.add(Calendar.DAY_OF_YEAR, 1);
      ensureWorkday(cal);
    }

    final Date result = cal.getTime();
    return result;
  }

}
