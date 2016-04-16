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

package de.micromata.genome.chronos.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;

import de.micromata.genome.chronos.JobControlException;
import de.micromata.genome.chronos.JobRetryException;
import de.micromata.genome.chronos.spi.AbstractFutureJob;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.LoggedRuntimeException;
import de.micromata.genome.util.text.PipeValueList;
import de.micromata.genome.util.types.Converter;
import de.micromata.genome.util.types.Pair;

/**
 * Argument is a Map<String, String> map.
 * 
 * TODO this very genome specific
 *
 * @author roger@micromata.de
 */
public abstract class AbstractMgcCommandLineJob extends AbstractFutureJob
{

  /**
   * The string arg.
   */
  transient protected String stringArg;

  /**
   * The args.
   */
  transient protected Map<String, String> args;

  /**
   * The admin user name.
   */
  transient protected String adminUserName;

  /**
   * Method to be implemented.
   *
   * @param args Parsed arguments
   * @return the object
   * @throws Exception the exception
   */
  public abstract Object call(Map<String, String> args) throws Exception;

  @Override
  public Object call(Object argument) throws Exception
  {
    StopWatch sw = AbstractMgcJob.prepareJob(this);
    long waitTime = getWaitTime();
    try {
      return call(args);

    } catch (JobControlException ex) {
      throw ex;
    } catch (LoggedRuntimeException ex) {
      throw new JobRetryException(ex.getMessage(), ex);
    } catch (Throwable ex) { // NOSONAR "Illegal Catch" framework
      this.getTriggerJobDO();
      /**
       * @logging
       * @reason Ein Job ist abgebrochen
       * @action Abhaengig von der Exception
       */
      GLog.error(GenomeLogCategory.Scheduler,
          "AdminJob failed. JobName: " + getClass().getSimpleName() + ": " + ex.getMessage(),
          new LogExceptionAttribute(ex));
      throw new JobRetryException(ex.getMessage(), ex);
    } finally {
      AbstractMgcJob.finishJob(this, sw, waitTime);

    }

  }

  /**
   * Parses the standard values.
   *
   * @param args the args
   */
  protected void parseStandardValues(Map<String, String> args)
  {

    // popConfigId = args.get("POPCONFIG");
    adminUserName = args.get("ADMINUSER");
    if (StringUtils.isBlank(adminUserName) == true) {
      adminUserName = "job_" + getTriggerJobDO().getPk();
    }
  }

  /**
   * Gets the standard args.
   *
   * @param obj the obj
   * @return the standard args
   */
  public static Map<String, String> getStandardArgs(Object obj)
  {
    if (obj == null) {
      return new HashMap<String, String>();
    }
    if ((obj instanceof String) == false) {
      throw new RuntimeException("Job expects standard pipe seperated args");
    }
    String args = (String) obj;
    if (StringUtils.isBlank(args) == true) {
      return new HashMap<String, String>();
    }
    Map<String, String> margs = PipeValueList.decode(args);
    return margs;
  }

  /**
   * Parses the date time.
   *
   * @param s the s
   * @return the date
   */
  public static Date parseDateTime(String s)
  {
    if (s == null) {
      return null;
    }
    return Converter.parseIsoDateToDate(s);
  }

  /**
   * Parses the int.
   *
   * @param s the s
   * @return the integer
   */
  public static Integer parseInt(String s)
  {
    if (s == null) {
      return null;
    }
    return Integer.parseInt(s);
  }

  /**
   * Parses the long.
   *
   * @param s the s
   * @return the long
   */
  public static Long parseLong(String s)
  {
    if (s == null) {
      return null;
    }
    return Long.parseLong(s);
  }

  /**
   * Parses the big decimal.
   *
   * @param s the s
   * @return the big decimal
   */
  public static BigDecimal parseBigDecimal(String s)
  {
    if (s == null || s.length() == 0) {
      return null;
    }
    Pair<Boolean, BigDecimal> r = Converter.convertBigDecimal(s);
    if (r.getFirst() == false) {
      throw new RuntimeException("Cannot parse Bigdecimal: " + s);
    }
    return r.getSecond();
  }

  /**
   * Parses the date.
   *
   * @param args the args
   * @param name the name
   * @param required the required
   * @return the date
   */
  public static Date parseDate(Map<String, String> args, String name, boolean required)
  {
    try {
      Date start = parseDateTime(args.get(name));
      if (start == null && required) {
        throw new RuntimeException(name + " muss angegeben werden");
      }
      return start;
    } catch (Exception ex) {
      throw new RuntimeException(name + " kann nicht geparst werden", ex);
    }
  }

  /**
   * Parses the integer.
   *
   * @param args the args
   * @param name the name
   * @param required the required
   * @return the integer
   */
  public static Integer parseInteger(Map<String, String> args, String name, boolean required)
  {
    try {
      Integer v = parseInt(args.get(name));
      if (v == null && required) {
        throw new RuntimeException(name + " muss angegeben werden");
      }
      return v;
    } catch (Exception ex) {
      throw new RuntimeException(name + " kann nicht geparst werden", ex);
    }
  }

  /**
   * Parses the long.
   *
   * @param args the args
   * @param name the name
   * @param required the required
   * @return the long
   */
  public static Long parseLong(Map<String, String> args, String name, boolean required)
  {
    try {
      Long v = parseLong(args.get(name));
      if (v == null && required) {
        throw new RuntimeException(name + " muss angegeben werden");
      }
      return v;
    } catch (Exception ex) {
      throw new RuntimeException(name + " kann nicht geparst werden", ex);
    }
  }

  /**
   * Parses the boolean.
   *
   * @param args the args
   * @param name the name
   * @param required the required
   * @return true, if successful
   */
  public static boolean parseBoolean(Map<String, String> args, String name, boolean required)
  {
    String v = args.get(name);

    if (required == true && v == null) {
      throw new RuntimeException(name + " muss angegeben werden");
    }

    if (v == null) {
      return false;
    }

    return v.equals("true");
  }

  /**
   * Parses the big decimal.
   *
   * @param args the args
   * @param name the name
   * @param required the required
   * @return the big decimal
   */
  public static BigDecimal parseBigDecimal(Map<String, String> args, String name, boolean required)
  {
    try {
      BigDecimal v = parseBigDecimal(args.get(name));
      if (v == null && required) {
        throw new RuntimeException(name + " muss angegeben werden");
      }
      return v;
    } catch (Exception ex) {
      throw new RuntimeException(name + " kann nicht geparst werden", ex);
    }
  }

  /**
   * Parses the not empty string.
   *
   * @param args the args
   * @param name the name
   * @return the string
   */
  public static String parseNotEmptyString(Map<String, String> args, String name)
  {
    String v = args.get(name);
    if (StringUtils.isBlank(v) == true) {
      throw new RuntimeException("Fuer " + name + " muss ein Wert angegeben werden");
    }
    return v;
  }
}
