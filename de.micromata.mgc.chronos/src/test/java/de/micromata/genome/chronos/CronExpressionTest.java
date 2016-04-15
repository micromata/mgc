/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   08.12.2007
// Copyright Micromata 08.12.2007
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;
import junit.framework.TestCase;
import de.micromata.genome.chronos.spi.CronExpression;

public class CronExpressionTest extends TestCase
{
  private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
  public void testExpression() throws ParseException
  {
    String exr = "15 1 * * *";
    CronExpression ex = new CronExpression(exr);
    Date ft = ex.getNextFireTime(sdf.parse("09.02.2009 17:30"));
    Assert.assertEquals(sdf.parse("10.02.2009 01:15"), ft);
  }

  public void testWeeklyFire() throws ParseException
  {
    String exr = "20 3 * * MON";
    CronExpression ex = new CronExpression(exr);
    Date ft = ex.getNextFireTime(sdf.parse("09.02.2009 17:30"));
    Assert.assertEquals(sdf.parse("16.02.2009 03:20"), ft);
  }

  public void testWeeklyFire2() throws ParseException
  {
    String exr = "20 3 * * 1";
    CronExpression ex = new CronExpression(exr);
    Date ft = ex.getNextFireTime(sdf.parse("15.02.2009 18:00"));
    Assert.assertEquals(sdf.parse("16.02.2009 03:20"), ft);
  }

  public void testMonthlyFire() throws ParseException
  {
    String exr = "20 3 16 * 1";
    CronExpression ex = new CronExpression(exr);
    Date ft = ex.getNextFireTime(sdf.parse("15.02.2009 18:00"));
    Assert.assertEquals(sdf.parse("16.02.2009 03:20"), ft);
  }

  public void testMonthlyFire2() throws ParseException
  {
    String exr = "20 3 16 * *";
    CronExpression ex = new CronExpression(exr);
    Date ft = ex.getNextFireTime(sdf.parse("16.02.2009 03:19"));
    Assert.assertEquals(sdf.parse("16.02.2009 03:20"), ft);
  }

}
