/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   19.02.2007
// Copyright Micromata 19.02.2007
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos.spi.jdbc;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

/**
 * A Job to display in GUI.
 *
 * @author roger
 *
 */
public class TriggerJobDisplayDO extends TriggerJobDO
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -2766329112135471673L;

  /**
   * The scheduler name.
   */
  private String schedulerName;

  /**
   * Last result.
   */
  private JobResultDO result = null;

  /**
   * Instantiates a new trigger job display do.
   */
  public TriggerJobDisplayDO()
  {

  }

  /**
   * Instantiates a new trigger job display do.
   *
   * @param other the other
   */
  public TriggerJobDisplayDO(TriggerJobDO other)
  {
    super(other);
  }

  @Override
  public void setCurrentResultPk(Long resultPk)
  {
    super.setCurrentResultPk(resultPk);
    if (resultPk == null) {
      return;
    }
    if (getResult() == null) {
      setResult(new JobResultDO());
    }
    getResult().setPk(resultPk);
  }

  public String getCurrentJobResultString()
  {
    if (getResult() == null) {
      return "";
    }
    return getResult().getResultString();
  }

  public JobResultDO getResult()
  {
    return result;
  }

  public void setResult(JobResultDO result)
  {
    this.result = result;
  }

  @Override
  public String getJobDefinitionString()
  {
    return StringEscapeUtils.escapeHtml(super.getJobDefinitionString());
  }

  public String getJobDefinitionStringShortShort(){
    String s = getJobDefinitionStringShort();
    return StringUtils.substringAfterLast(s, ".");
  }

  public String getJobDefinitionStringShort()
  {
    String s = super.getJobDefinitionString();

    if (s == null || s.length() == 0) {
      return "";
    }
    String st = "<classToStart>";
    int idx = s.indexOf("<classToStart>");
    if (idx != -1) {
      s = s.substring(idx + st.length());
      idx = s.indexOf("<");
      if (idx != -1) {
        s = s.substring(0, idx);
      }
      return s;
    }
    // GWAFactoryBean
    st = "<className>";
    idx = s.indexOf("<className>");
    if (idx != -1) {
      s = s.substring(idx + st.length());
      idx = s.indexOf("<");
      if (idx != -1) {
        s = s.substring(0, idx);
      }
      return s;
    }

    // if (s.startsWith("<") == true) {
    // s = StringUtils.substring(s, 1, 50);
    // } else {
    s = StringUtils.abbreviate(s, 50);
    // }
    idx = s.indexOf('\n');
    if (idx != -1) {
      s = s.substring(0, idx);
    }
    return s;
  }

  public String getSchedulerName()
  {
    return schedulerName;
  }

  public void setSchedulerName(String schedulerName)
  {
    this.schedulerName = schedulerName;
  }
}
