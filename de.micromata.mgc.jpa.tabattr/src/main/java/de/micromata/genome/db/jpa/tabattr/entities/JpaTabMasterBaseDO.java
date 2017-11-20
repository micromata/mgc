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
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.db.jpa.tabattr.api.EntityWithAttributes;
import de.micromata.genome.jpa.ComplexEntity;
import de.micromata.genome.jpa.ComplexEntityVisitor;
import de.micromata.genome.jpa.StdRecordDO;
import de.micromata.genome.util.strings.converter.StandardStringConverter;
import de.micromata.genome.util.strings.converter.StringConverter;
import de.micromata.genome.util.types.Pair;

/**
 * Base class for JPA class, which has attributes attached.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <M> the generic type
 */
@MappedSuperclass
public abstract class JpaTabMasterBaseDO<M extends JpaTabMasterBaseDO<?, ?>, PK extends Serializable>
    extends StdRecordDO<PK>
    implements EntityWithAttributes,
    ComplexEntity
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -7393715450887760905L;

  /**
   * The string converter.
   */
  private transient StringConverter stringConverter = StandardStringConverter.get();

  /**
   * holds the attributes.
   */
  private Map<String, JpaTabAttrBaseDO<M, PK>> attributes = new TreeMap<String, JpaTabAttrBaseDO<M, PK>>();

  /**
   * Instantiates a new jpa tab master base do.
   */
  public JpaTabMasterBaseDO()
  {

  }

  /**
   * Instantiates a new jpa tab master base do.
   *
   * @param stringConverter the string converter
   */
  public JpaTabMasterBaseDO(StringConverter stringConverter)
  {
    this.stringConverter = stringConverter;
  }

  /**
   * Entity for the an attribute, where the value fits into the JpaTabAttrBaseDO.value field.
   *
   * @return the attr entity class
   */
  @Transient
  public abstract Class<? extends JpaTabAttrBaseDO<M, ? extends Serializable>> getAttrEntityClass();

  /**
   * Entity for the an attribute, where the value does NOT fit into the JpaTabAttrBaseDO.value field, but need nested
   * JpaTabAttrDataBaseDO
   *
   * @return the attr entity with data class
   */
  @Transient
  public abstract Class<? extends JpaTabAttrBaseDO<M, ? extends Serializable>> getAttrEntityWithDataClass();

  /**
   * Entity class for the AttrData table.
   *
   * @return the attr data entity class
   */
  @Transient
  public abstract Class<? extends JpaTabAttrDataBaseDO<? extends JpaTabAttrBaseDO<M, PK>, PK>> getAttrDataEntityClass();

  /**
   * Create a new attribute entity where the value fits into the JpaTabAttrBaseDO.value field.
   * 
   * The constructor of the entity has to pass it to the constructor JpaTabAttrBaseDO(M parent, String propertyName,
   * String value)
   *
   * @param key JpaTabAttrBaseDO.propertyName
   * @param type the type
   * @param value JpaTabAttrBaseDO.value
   * @return new entity
   */
  public abstract JpaTabAttrBaseDO<M, PK> createAttrEntity(String key, char type, String value);

  /**
   * Create a new attribute entity where the value does NOT fit into the JpaTabAttrBaseDO.value field.
   * 
   * The constructor of the entity has to pass it to the constructor JpaTabAttrBaseDO(M parent, String propertyName,
   * String value)
   *
   * @param key JpaTabAttrBaseDO.propertyName
   * @param type the type
   * @param value JpaTabAttrBaseDO.value
   * @return new entity
   */
  public abstract JpaTabAttrBaseDO<M, PK> createAttrEntityWithData(String key, char type,
      String value);

  /**
   * Get a attribute from entity.
   *
   * @param key must not be null
   * @return null, if attribute is not defined. Otherwise value stored
   */
  @Override
  @Transient
  public String getStringAttribute(String key)
  {
    JpaTabAttrBaseDO<M, ? extends Serializable> tabr = getAttributeRow(key);
    if (tabr == null) {
      return null;
    }
    return tabr.getValue();
  }

  /**
   * Put an attribute identified by key into the attribute map.
   * 
   * @param key must not be null
   * @param value must not be null
   */
  @Override
  public void putStringAttribute(String key, String value)
  {
    putAttributeInternal(key, stringConverter.getTypeChar(value), value);
  }

  /**
   * {@inheritDoc}
   *
   */

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.jpa.EntityWithAttributes#getAttribute(java.lang.String)
   */
  @Override
  public Object getAttribute(String key)
  {
    JpaTabAttrBaseDO<M, ? extends Serializable> tabr = getAttributeRow(key);
    if (tabr == null) {
      return null;
    }
    String data = tabr.getStringData();
    return stringConverter.stringToObject(tabr.getType(), data);
  }

  /**
   * {@inheritDoc}
   *
   */

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getAttribute(String key, Class<T> expectedClass)
  {
    Object val = getAttribute(key);
    if (val == null) {
      return null;
    }
    if (expectedClass.isAssignableFrom(val.getClass()) == false) {
      throw new IllegalArgumentException("Attribute does not match type. key: "
          + key
          + "; expected: "
          + expectedClass.getName()
          + "; retreived: "
          + val.getClass().getName());
    }
    return (T) val;
  }

  /**
   * {@inheritDoc}
   *
   */

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.jpa.EntityWithAttributes#putAttribute(java.lang.String, java.lang.Object)
   */
  @Override
  public void putAttribute(String key, Object value)
  {
    Pair<Character, String> p = stringConverter.objectToString(value);
    putAttributeInternal(key, p.getFirst(), p.getSecond());
  }

  /**
   * Put attribute internal.
   *
   * @param key the key
   * @param type the type
   * @param encodedString the encoded string
   */
  public void putAttributeInternal(String key, Character type, String encodedString)
  {

    JpaTabAttrBaseDO<M, PK> tabr = getAttributeRow(key);
    Class<?> required = StringUtils.length(encodedString) > JpaTabAttrDataBaseDO.DATA_MAXLENGTH
        ? getAttrEntityWithDataClass()
        : getAttrEntityClass();

    if (tabr != null && required == tabr.getClass()) {
      tabr.setStringData(encodedString);
      tabr.setType(type);
      return;
    }

    if (StringUtils.length(encodedString) > JpaTabAttrDataBaseDO.DATA_MAXLENGTH) {
      putAttributeRow(key, createAttrEntityWithData(key, type, encodedString));
    } else {
      putAttributeRow(key, createAttrEntity(key, type, encodedString));
    }
  }

  /**
   * removes the attribute.
   * 
   * @param key aka propertyName
   */
  @Override
  public void removeAttribute(String key)
  {
    attributes.remove(key);
  }

  /**
   * The keys of the attributes.
   * 
   * @return never null
   */
  @Override
  @Transient
  public Set<String> getAttributeKeys()
  {
    return attributes.keySet();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void visit(ComplexEntityVisitor visitor)
  {
    visitor.visit(this);
    for (JpaTabAttrBaseDO<M, PK> d : getAttributes().values()) {
      d.visit(visitor);
    }
  }

  /**
   * Gets the attribute row.
   *
   * @param key the key
   * @return the attribute row
   */
  public JpaTabAttrBaseDO<M, PK> getAttributeRow(String key)
  {
    return attributes.get(key);
  }

  /**
   * Put attribute row.
   *
   * @param key the key
   * @param attr the attr
   */
  public void putAttributeRow(String key, JpaTabAttrBaseDO<M, PK> attr)
  {
    attributes.put(key, attr);
  }

  /**
   * Gets the attributes.
   *
   * @return the attributes
   */
  @Transient
  public Map<String, JpaTabAttrBaseDO<M, PK>> getAttributes()
  {
    return attributes;
  }

  /**
   * Sets the attributes.
   *
   * @param attributes the attributes
   */
  public void setAttributes(Map<String, JpaTabAttrBaseDO<M, PK>> attributes)
  {
    this.attributes = attributes;
  }

  /**
   * Gets the string converter.
   *
   * @return the string converter
   */
  @Transient
  public StringConverter getStringConverter()
  {
    return stringConverter;
  }

  /**
   * Sets the string converter.
   *
   * @param stringConverter the new string converter
   */
  public void setStringConverter(StringConverter stringConverter)
  {
    this.stringConverter = stringConverter;
  }

}
