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

package de.micromata.genome.db.jpa.logging.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

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
  @Column(name = "BASE_LOG_ATTRIBUTE", length = 30)
  public String getBaseLogAttribute()
  {
    return baseLogAttribute;
  }

  /**
   * Sets the base llog attribute.
   *
   * @param base_LOG_attribute the new base llog attribute
   */
  public void setBaseLogAttribute(String base_LOG_attribute)
  {
    this.baseLogAttribute = base_LOG_attribute;
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
