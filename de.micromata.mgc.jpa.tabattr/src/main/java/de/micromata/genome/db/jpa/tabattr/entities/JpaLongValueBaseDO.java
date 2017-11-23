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

package de.micromata.genome.db.jpa.tabattr.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.db.jpa.tabattr.api.EntityWithLongValue;
import de.micromata.genome.jpa.ComplexEntity;
import de.micromata.genome.jpa.ComplexEntityVisitor;
import de.micromata.genome.jpa.StdRecordDO;

/**
 * Base table to hold a column VALUE witch can be 3000 or more character long.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <M> the generic type
 */
@MappedSuperclass
public abstract class JpaLongValueBaseDO<M extends JpaLongValueBaseDO<?, PK>, PK extends Serializable>
    extends StdRecordDO<PK>
    implements EntityWithLongValue<PK>,
    ComplexEntity
{
  /**
   * value of the attribute. If the value is longer as VALUE_MAXLENGHT, following parts are stored in data children.
   */
  protected String value;

  /**
   * The data.
   */
  private List<JpaLongValueDataBaseDO<?, PK>> data = new ArrayList<JpaLongValueDataBaseDO<?, PK>>();

  /**
   * Instantiates a new jpa long value base do.
   */
  public JpaLongValueBaseDO()
  {

  }

  /**
   * Instantiates a new jpa long value base do.
   * 
   * @param value the value
   */
  public JpaLongValueBaseDO(String value)
  {
    setStringData(value);
  }

  /**
   * Creates the data.
   * 
   * @param data the data
   * @return the jpa long value base do
   */
  public abstract JpaLongValueDataBaseDO<?, PK> createData(String data);

  /**
   * Gets the max data length.
   *
   * @return the max data length
   */
  @Transient
  public int getMaxDataLength()
  {
    return getValueMaxLength();
  }

  /**
   * Gets the value max length.
   *
   * @return the value max length
   */
  @Transient
  public int getValueMaxLength()
  {
    return JpaLongValueDataBaseDO.DATA_MAXLENGTH;
  }

  /**
   * {@inheritDoc}
   * 
   */

  @Override
  public void visit(ComplexEntityVisitor visitor)
  {
    visitor.visit(this);
    List<JpaLongValueDataBaseDO<?, PK>> data = getData();

    for (JpaLongValueDataBaseDO<?, PK> d : data) {
      visitor.visit(d);
    }
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  @Transient
  public String getValue()
  {
    return getStringData();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setValue(String value)
  {
    setStringData(value);

  }

  /**
   * Get the value of the attribute.
   * 
   * If data children exists, the resulting value will be joined.
   *
   * @return the string data
   */
  @Transient
  public String getStringData()
  {
    List<JpaLongValueDataBaseDO<?, PK>> data = getData();
    if (data.isEmpty() == true) {
      return value;
    }
    StringBuilder sb = new StringBuilder();
    sb.append(value);
    for (JpaLongValueDataBaseDO<?, PK> dv : data) {
      sb.append(dv.getData());
    }
    return sb.toString();
  }

  /**
   * Set the value of the attribute.
   * 
   * if value is longer than VALUE_MAXLENGHT the string will be split and stored in additional data children entities.
   *
   * @param value the new string data
   */
  public void setStringData(String value)
  {
    List<JpaLongValueDataBaseDO<?, PK>> data = getData();
    data.clear();
    int maxValLength = getValueMaxLength();
    if (StringUtils.length(value) > maxValLength) {
      this.value = value.substring(0, maxValLength);
      String rest = value.substring(maxValLength);
      int maxDataLength = getMaxDataLength();
      int rowIdx = 0;
      while (rest.length() > 0) {
        String ds = StringUtils.substring(rest, 0, maxDataLength);
        rest = StringUtils.substring(rest, maxDataLength);
        JpaLongValueDataBaseDO<?, PK> dataDo = createData(ds);
        dataDo.setDatarow(rowIdx++);
        data.add(dataDo);
      }
    } else {
      this.value = value;
    }
  }

  /**
   * Gets the internal value.
   *
   * @return the internal value
   */
  @Column(name = "VALUE", length = 3000)
  public String getInternalValue()
  {
    return value;
  }

  /**
   * Sets the internal value.
   *
   * @param value the new internal value
   */
  public void setInternalValue(String value)
  {
    this.value = value;
  }

  /**
   * Gets the data.
   *
   * @return the data
   */
  @Transient
  public List<JpaLongValueDataBaseDO<?, PK>> getData()
  {
    return data;
  }

  /**
   * Sets the data.
   *
   * @param data the new data
   */
  public void setData(List<JpaLongValueDataBaseDO<?, PK>> data)
  {
    this.data = data;
  }
}
