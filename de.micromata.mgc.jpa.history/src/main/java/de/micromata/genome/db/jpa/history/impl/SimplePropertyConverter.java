package de.micromata.genome.db.jpa.history.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import de.micromata.genome.db.jpa.history.api.HistProp;
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

  /**
   * Read property value.
   *
   * @param entity the entity
   * @param pd the pd
   * @return null on failure.
   */
  public static Object readPropertyValue(Object entity, PropertyDescriptor pd)
  {
    try {
      Method method = pd.getReadMethod();
      Object value = method.invoke(entity);
      return value;
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
      GLog.warn(GenomeLogCategory.Jpa, "Hist; Cannot read property: " + ex.getMessage(), new LogExceptionAttribute(ex));
      return null;
    }
  }

  @Override
  public List<HistProp> convert(Object entity, PropertyDescriptor pd)
  {
    Object value = readPropertyValue(entity, pd);
    return Collections.singletonList(convertInternal(value, pd));
  }

  /**
   * Convert.
   *
   * @param value the value
   * @return the string
   */
  protected HistProp convertInternal(Object value, PropertyDescriptor pd)
  {
    HistProp ret = new HistProp();
    ret.setName("");
    ret.setType(pd.getPropertyType().getName());
    String sval;
    if (value instanceof Enum<?>) {
      sval = ((Enum<?>) value).name();
    } else {
      sval = stringConverter.asString(value);
    }
    ret.setValue(sval);
    return ret;
  }
}
