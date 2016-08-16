package de.micromata.genome.util.types;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

public class DateUtilsTest
{
  @Test
  public void testIsSameMonth()
  {
    Assert.assertTrue("Dates are not in same month!", DateUtils.isSameMonth(new Date(), new Date()));
    Assert.assertTrue("Dates are not in same month!", DateUtils.isSameMonth(createDate(2016, 0, 1, 0, 0, 0, 0), createDate(2016, 0, 31, 23, 59, 59, 999)));
    Assert.assertFalse("Dates are not in same month!", DateUtils.isSameMonth(createDate(2016, 0, 31, 23, 59, 59, 999), createDate(2016, 1, 1, 0, 0, 0, 0)));
    Assert.assertFalse("Dates are not in same month!", DateUtils.isSameMonth(createDate(2015, 0, 1, 0, 0, 0, 0), createDate(2016, 0, 1, 0, 0, 0, 0)));
  }

  private static Date createDate(final int year, final int month, final int day, final int hour, final int minute, final int second, final int millisecond)
  {
    final Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    cal.set(Calendar.YEAR, year);
    cal.set(Calendar.MONTH, month);
    cal.set(Calendar.DAY_OF_MONTH, day);
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.MINUTE, minute);
    cal.set(Calendar.SECOND, second);
    cal.set(Calendar.MILLISECOND, millisecond);
    return cal.getTime();
  }

}
