package de.micromata.genome.db.jpa.history.api;

import de.micromata.genome.db.jpa.history.entities.PropertyOpType;

/**
 * The Class DiffEntry.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class DiffEntry
{

  /**
   * The property op type.
   */
  private PropertyOpType propertyOpType;

  /**
   * The property name.
   */
  private String propertyName;

  /**
   * The old value.
   */
  private String oldValue;

  /**
   * The new value.
   */
  private String newValue;

  public PropertyOpType getPropertyOpType()
  {
    return propertyOpType;
  }

  public void setPropertyOpType(PropertyOpType propertyOpType)
  {
    this.propertyOpType = propertyOpType;
  }

  public String getPropertyName()
  {
    return propertyName;
  }

  public void setPropertyName(String propertyName)
  {
    this.propertyName = propertyName;
  }

  public String getOldValue()
  {
    return oldValue;
  }

  public void setOldValue(String oldValue)
  {
    this.oldValue = oldValue;
  }

  public String getNewValue()
  {
    return newValue;
  }

  public void setNewValue(String newValue)
  {
    this.newValue = newValue;
  }
}
