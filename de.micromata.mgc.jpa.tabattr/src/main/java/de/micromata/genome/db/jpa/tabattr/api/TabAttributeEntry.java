package de.micromata.genome.db.jpa.tabattr.api;

import java.io.Serializable;

import de.micromata.genome.util.strings.converter.StringConverter;

/**
 * Interface to one entry to the table attributes.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface TabAttributeEntry<PK extends Serializable>
{

  /**
   * The PK of the attribute.
   *
   * @return may null if not stored in db.
   */
  PK getPk();

  /**
   * The PK of the attribute.
   *
   * @param pk the new pk
   */
  void setPk(PK pk);

  /**
   * The propertyName / key of the attribute.
   *
   * @return the property name
   */
  String getPropertyName();

  /**
   * The propertyName / key of the attribute.
   *
   * @param propertyName the new property name
   */
  void setPropertyName(String propertyName);

  /**
   * The type of the attribute.
   * 
   * Default is STRING('V')
   *
   * @return the type
   * @see de.micromata.genome.db.attr.value.AttrValueType.shortTypeName
   */
  char getType();

  /**
   * Sets the type.
   *
   * @param type the new type
   */
  void setType(char type);

  /**
   * The attribute value.
   *
   * @return the value
   */
  String getValue();

  /**
   * Set the String value.
   *
   * @param value the new value
   */
  void setValue(String value);

  /**
   * Get Value object. Use the converter.O
   *
   * @param converter the converter
   * @return the value object
   */
  Object getValueObject(StringConverter converter);

  /**
   * Sets the value object.
   * 
   * @param converter the converter
   * @param value the value
   */
  void setValueObject(StringConverter converter, Object value);

}
