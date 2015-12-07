package de.micromata.genome.db.jpa.history.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.micromata.genome.db.jpa.history.api.HistoryPropertyConverter;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.util.strings.converter.StandardStringConverter;
import de.micromata.genome.util.strings.converter.StringConverter;

/**
 * The Class SimplePropertyConverter.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class SimplePropertyConverter implements HistoryPropertyConverter
{

  /**
   * The string converter.
   */
  private static StringConverter stringConverter = StandardStringConverter.get();

  @Override
  public String convert(Object entity, PropertyDescriptor pd)
  {
    Method method = pd.getReadMethod();

    try {
      Object value = method.invoke(entity);
      return convert(value);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
      GLog.warn(GenomeLogCategory.Jpa, "Hist; Cannot read property: " + ex.getMessage(), new LogExceptionAttribute(ex));
      return null;
    }
  }

  /**
   * Convert.
   *
   * @param value the value
   * @return the string
   */
  protected String convert(Object value)
  {
    return stringConverter.asString(value);
  }
}
