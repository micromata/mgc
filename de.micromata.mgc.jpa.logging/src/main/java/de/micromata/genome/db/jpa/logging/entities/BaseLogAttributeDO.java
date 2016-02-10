package de.micromata.genome.db.jpa.logging.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import de.micromata.genome.jpa.StdRecordDO;

@MappedSuperclass
public abstract class BaseLogAttributeDO<M extends BaseLogMasterDO<?>>extends StdRecordDO<Long>
{
  /**
   * The log master.
   */

  private M logMaster;

  /**
   * The datacol1.
   */
  private String datacol1;

  /**
   * The short value.
   */
  private String shortValue;

  /**
   * The base llog attribute.
   */
  private String baseLogAttribute;

  /**
   * The datarow.
   */
  private Long datarow;

  /**
   * {@inheritDoc}
   *
   */

  /**
   * Gets the short value.
   *
   * @return the short value
   */
  @Column(name = "SHORT_VALUE", length = 40)
  public String getShortValue()
  {
    return shortValue;
  }

  /**
   * Sets the short value.
   *
   * @param shortValue the new short value
   */
  public void setShortValue(String shortValue)
  {
    this.shortValue = shortValue;
  }

  /**
   * Gets the datacol1.
   *
   * @return the datacol1
   */
  @Column(name = "DATACOL1", length = 4000)
  public String getDatacol1()
  {
    return datacol1;
  }

  /**
   * Sets the datacol1.
   *
   * @param datacol1 the new datacol1
   */
  public void setDatacol1(String datacol1)
  {
    this.datacol1 = datacol1;
  }

  /**
   * Gets the base llog attribute.
   *
   * @return the base llog attribute
   */
  @Column(name = "BASE_GLOG_ATTRIBUTE", length = 30)
  public String getBaseLogAttribute()
  {
    return baseLogAttribute;
  }

  /**
   * Sets the base llog attribute.
   *
   * @param base_GLOG_attribute the new base llog attribute
   */
  public void setBaseLogAttribute(String base_GLOG_attribute)
  {
    this.baseLogAttribute = base_GLOG_attribute;
  }

  /**
   * Gets the datarow.
   *
   * @return the datarow
   */
  @Column(name = "DATAROW", length = 10)
  public Long getDatarow()
  {
    return datarow;
  }

  /**
   * Sets the datarow.
   *
   * @param datarow the new datarow
   */
  public void setDatarow(Long datarow)
  {
    this.datarow = datarow;
  }

  /**
   * Gets the log master.
   *
   * @return the log master
   */
  @Transient
  public M getLogMaster()
  {
    return logMaster;
  }

  /**
   * Sets the log master.
   *
   * @param logMaster the new log master
   */
  public void setLogMaster(M logMaster)
  {
    this.logMaster = logMaster;
  }
}
