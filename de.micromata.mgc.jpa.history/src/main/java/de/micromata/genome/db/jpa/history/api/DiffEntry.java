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
  private HistProp oldProp;

  private HistProp newProp;

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
    return oldProp == null ? null : oldProp.getValue();
  }

  public String getNewValue()
  {
    return newProp == null ? null : newProp.getValue();
  }

  public HistProp getOldProp()
  {
    return oldProp;
  }

  public void setOldProp(HistProp oldProp)
  {
    this.oldProp = oldProp;
  }

  public HistProp getNewProp()
  {
    return newProp;
  }

  public void setNewProp(HistProp newProp)
  {
    this.newProp = newProp;
  }

}
