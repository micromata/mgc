/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   21.03.2008
// Copyright Micromata 21.03.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.stats;

import java.io.Serializable;
import java.util.Date;

/**
 * POJO for holding a stats entry.
 * 
 * @author roger@micromata.de
 * 
 */
public class TypeStatsDO implements Serializable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -7128205357814467249L;

  /**
   * The first date.
   */
  private Date firstDate = new Date();

  /**
   * The last date.
   */
  private Date lastDate = new Date();

  public Date getFirstDate()
  {
    return firstDate;
  }

  public void setFirstDate(Date firstDate)
  {
    this.firstDate = firstDate;
  }

  public Date getLastDate()
  {
    return lastDate;
  }

  public void setLastDate(Date lastDate)
  {
    this.lastDate = lastDate;
  }
}
