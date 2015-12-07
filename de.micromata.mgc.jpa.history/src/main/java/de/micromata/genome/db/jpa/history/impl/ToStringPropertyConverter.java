package de.micromata.genome.db.jpa.history.impl;

import java.beans.PropertyDescriptor;

/**
 * Reads property an use toString() on the return value.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ToStringPropertyConverter extends SimplePropertyConverter
{

  @Override
  public String convert(Object entity, PropertyDescriptor pd)
  {
    Object bo = super.convert(entity, pd);
    if (bo == null) {
      return null;
    }
    return bo.toString();
  }

}
