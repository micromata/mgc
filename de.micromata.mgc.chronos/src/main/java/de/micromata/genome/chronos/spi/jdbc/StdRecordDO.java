/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   16.02.2008
// Copyright Micromata 16.02.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos.spi.jdbc;

import java.io.Serializable;
import java.util.Date;

import de.micromata.genome.util.types.Converter;

/**
 * The Class StdRecordDO.
 *
 * @author roger@micromata.de
 */
public class StdRecordDO implements /** BaseStdRecord, */
    Serializable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 3891095023083295900L;

  /**
   * The pk.
   */
  protected Long pk;

  /**
   * The created by.
   */
  private String createdBy;

  /**
   * The modified by.
   */
  private String modifiedBy;

  /**
   * The modified at.
   */
  private Date modifiedAt;

  /**
   * The created at.
   */
  private Date createdAt;

  /**
   * The update counter.
   */
  private Integer updateCounter;

  /**
   * Instantiates a new std record do.
   */
  public StdRecordDO()
  {

  }

  /**
   * Instantiates a new std record do.
   *
   * @param other the other
   */
  public StdRecordDO(StdRecordDO other)
  {
    this.pk = other.pk;
    this.createdAt = other.createdAt;
    this.createdBy = other.createdBy;
    this.modifiedAt = other.modifiedAt;
    this.modifiedBy = other.modifiedBy;
    this.updateCounter = other.updateCounter;
  }

  public String getModifiedBy()
  {
    return modifiedBy;
  }

  public void setModifiedBy(String modifiedBy)
  {
    this.modifiedBy = modifiedBy;
  }

  public Date getModifiedAt()
  {
    return modifiedAt;
  }

  public String getModifiedAtString()
  {
    if (modifiedAt == null) {
      return "";
    }
    return Converter.formatByIsoDateFormat(modifiedAt);
  }

  public void setModifiedAt(Date modifiedAt)
  {
    this.modifiedAt = modifiedAt;
  }

  public void setModifiedAtString(String str)
  {
    this.modifiedAt = Converter.parseIsoDateToDate(str);
  }

  public Date getCreatedAt()
  {
    return createdAt;
  }

  public String getCreatedAtString()
  {
    if (createdAt == null) {
      return "";
    }
    return Converter.formatByIsoDateFormat(createdAt);
  }

  public void setCreatedAtString(String str)
  {
    createdAt = Converter.parseIsoDateToDate(str);
  }

  public void setCreatedAt(Date createdAt)
  {
    this.createdAt = createdAt;
  }

  // Für optimistic locking

  public Integer getUpdateCounter()
  {
    return updateCounter;
  }

  public void setUpdateCounter(Integer updateCounter)
  {
    this.updateCounter = updateCounter;
  }

  public Long getPk()
  {
    return pk;
  }

  public void setPk(Long pk)
  {
    this.pk = pk;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy(String createdBy)
  {
    this.createdBy = createdBy;
  }

  // compat < R3
  public long getId()
  {
    return getPk();
  }
}
