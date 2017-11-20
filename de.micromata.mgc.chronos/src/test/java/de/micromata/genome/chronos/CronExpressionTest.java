//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.chronos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
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
