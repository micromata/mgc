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

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.db.jpa.tabattr.api.EntityWithAttributes;
import de.micromata.genome.db.jpa.tabattr.api.TabAttributeEntry;
import de.micromata.genome.jpa.ComplexEntity;
import de.micromata.genome.jpa.ComplexEntityVisitor;
import de.micromata.genome.jpa.StdRecordDO;
import de.micromata.genome.util.strings.converter.ConvertedStringTypes;
import de.micromata.genome.util.strings.converter.StringConverter;
import de.micromata.genome.util.types.Pair;

/**
 * Generic base table related attributes.
 *
 * @author roger
 * @param <M> the generic type
 */
@MappedSuperclass
public abstract class JpaTabAttrBaseDO<M extends EntityWithAttributes, PK extends Serializable>
    extends StdRecordDO<PK>
    implements ComplexEntity, TabAttributeEntry<PK>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 5347817201583563066L;

  /**
   * Link to the owning table.
   * 
   * Annotate the getter in the derived class with proper ManyToOne annotation
   */
  protected M parent;

  /**
   * Key of the attribute.
   */
  private String propertyName;

  /**
   * Type of the value.
   */
  private char type;

  /**
   * value of the attribute. If the value is longer as VALUE_MAXLENGHT, following parts are stored in data children.
   */
  protected String value;

  /**
   * Optional data elements, if value doesn't fit into the value field.
   */
  private List<JpaTabAttrDataBaseDO<?, PK>> data = new ArrayList<JpaTabAttrDataBaseDO<?, PK>>();

  /**
   * Instantiates a new jpa tab attr base do.
   */
  public JpaTabAttrBaseDO()
  {

  }

  /**
   * Instantiates a new jpa tab attr base do.
   *
   * @param parent the parent
   */
  public JpaTabAttrBaseDO(M parent)
  {
    this.parent = parent;
  }

  /**
   * Instantiates a new jpa tab attr base do.
   *
   * @param parent the parent
   * @param propertyName the property name
   * @param type the type
   * @param value the value
   */
  public JpaTabAttrBaseDO(M parent, String propertyName, char type, String value)
  {
    this.parent = parent;
    this.type = type;
    this.propertyName = propertyName;
    setStringData(value);
  }

  /**
   * Creates the data.
   *
   * @param data the data
   * @return the jpa tab attr data base do
   */
  public abstract JpaTabAttrDataBaseDO<?, PK> createData(String data);

  /**
   * Gets the max data length.
   *
   * @return the max data length
   */
  @Transient
  public int getMaxDataLength()
  {
    return JpaTabAttrDataBaseDO.DATA_MAXLENGTH;
  }

  /**
   * Gets the value max length.
   *
   * @return the value max length
   */
  @Transient
  public int getValueMaxLength()
  {
    return JpaTabAttrDataBaseDO.DATA_MAXLENGTH;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void visit(ComplexEntityVisitor visitor)
  {
    visitor.visit(this);
    List<JpaTabAttrDataBaseDO<?, PK>> data = getData();

    for (JpaTabAttrDataBaseDO<?, PK> d : data) {
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
    setType(ConvertedStringTypes.STRING.getShortType());
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Object getValueObject(StringConverter converter)
  {
    return converter.stringToObject(getType(), getStringData());
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setValueObject(StringConverter converter, Object value)
  {
    Pair<Character, String> pair = converter.objectToString(value);
    setType(pair.getFirst());
    setStringData(pair.getSecond());
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
    List<JpaTabAttrDataBaseDO<?, PK>> data = getData();
    if (data.isEmpty() == true) {
      return value;
    }
    StringBuilder sb = new StringBuilder();
    sb.append(value);
    for (JpaTabAttrDataBaseDO<?, PK> dv : data) {
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
    List<JpaTabAttrDataBaseDO<?, PK>> data = getData();
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
        JpaTabAttrDataBaseDO<?, PK> dataDo = createData(ds);
        dataDo.setDatarow(rowIdx++);
        data.add(dataDo);
      }
    } else {
      this.value = value;
    }
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  @Column(name = "PROPERTYNAME", length = 255)
  public String getPropertyName()
  {
    return propertyName;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setPropertyName(String propertyName)
  {
    this.propertyName = propertyName;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  @Column(name = "TYPE", nullable = false)
  public char getType()
  {
    return type;
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
   * {@inheritDoc}
   *
   */

  @Override
  public void setType(char type)
  {
    this.type = type;
  }

  /**
   * Gets the parent.
   *
   * @return the parent
   */
  @Transient
  public M getParent()
  {
    return parent;
  }

  /**
   * Sets the parent.
   *
   * @param parent the new parent
   */
  public void setParent(M parent)
  {
    this.parent = parent;
  }

  /**
   * Gets the data.
   *
   * @return the data
   */
  @Transient
  public List<JpaTabAttrDataBaseDO<?, PK>> getData()
  {
    return data;
  }

  /**
   * Sets the data.
   *
   * @param data the new data
   */
  public void setData(List<JpaTabAttrDataBaseDO<?, PK>> data)
  {
    this.data = data;
  }
}
