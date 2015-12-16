package de.micromata.genome.util.bean;

import java.beans.PropertyDescriptor;

/**
 * Exception while accessing Properties via PropertyDescriptor.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class PropertyAccessException extends RuntimeException
{
  public PropertyAccessException(String operation, Object bean, PropertyDescriptor desc, Exception ex)
  {
    super("Error " + operation + " on " + (bean == null ? "null" : bean.getClass().getName()) + "." + desc.getName()
        + ": " + ex.getMessage(), ex);
  }
}
