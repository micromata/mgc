package de.micromata.genome.db.jpa.history.impl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.persistence.Transient;

import de.micromata.genome.db.jpa.history.api.HistoryProperty;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyConverter;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyProvider;
import de.micromata.genome.db.jpa.history.api.NoHistory;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LoggedRuntimeException;
import de.micromata.genome.util.bean.PrivateBeanUtils;

/**
 * Use the default properties of the entity.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class DefaultHistoryPropertyProvider implements HistoryPropertyProvider
{
  /**
   * {@inheritDoc}
   *
   */
  @Override
  public void getProperties(HistoryMetaInfo historyMetaInfo, Object entity, Map<String, String> ret)
  {
    try {
      BeanInfo beanInfo = Introspector.getBeanInfo(entity.getClass(), Object.class);
      PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
      for (PropertyDescriptor pd : pds) {

        if (ignoreForHistory(historyMetaInfo, entity, pd) == true) {
          continue;
        }
        HistoryPropertyConverter conv = getPropertyConverter(entity, pd);
        String value = conv.convert(entity, pd);
        ret.put(pd.getName(), value);
      }
    } catch (IntrospectionException ex) {
      GLog.error(GenomeLogCategory.Jpa, "Hist; Failure to introspect bean: " + ex.getMessage(),
          new LogExceptionAttribute(ex));
    }
  }

  /**
   * Gets the property converter.
   *
   * @param entity the entity
   * @param pd the pd
   * @return the property converter
   */
  protected HistoryPropertyConverter getPropertyConverter(Object entity, PropertyDescriptor pd)
  {
    Method rm = pd.getReadMethod();
    HistoryProperty annot = rm.getAnnotation(HistoryProperty.class);
    if (annot == null) {
      Field f = PrivateBeanUtils.findField(entity.getClass(), pd.getName());
      if (f != null) {
        annot = f.getAnnotation(HistoryProperty.class);
      }
    }
    if (annot == null) {
      return new SimplePropertyConverter();
    }
    try {
      return annot.converter().newInstance();
    } catch (InstantiationException | IllegalAccessException ex) {
      throw new LoggedRuntimeException(LogLevel.Fatal, GenomeLogCategory.Jpa,
          "Hist; Cannot create: " + annot.converter() + "; " + ex.getMessage(), new LogExceptionAttribute(ex));
    }
  }

  /**
   * Read property value.
   *
   * @param entity the entity
   * @param pd the pd
   * @return the object
   */
  protected Object readPropertyValue(Object entity, PropertyDescriptor pd)
  {
    Method method = pd.getReadMethod();

    try {
      Object value = method.invoke(entity);
      return value;
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
      GLog.warn(GenomeLogCategory.Jpa, "Hist; Cannot read property: " + ex.getMessage(), new LogExceptionAttribute(ex));
      return null;
    }

  }

  /**
   * Ignore for history.
   *
   * @param historyMetaInfo the history meta info
   * @param entity the entity
   * @param pd the pd
   * @return true, if successful
   */
  protected boolean ignoreForHistory(HistoryMetaInfo historyMetaInfo, Object entity, PropertyDescriptor pd)
  {
    if (historyMetaInfo.ignoreProperty(pd.getName()) == true) {
      return true;
    }
    if (isStandardIgnoreField(pd) == true) {
      return true;
    }

    Method method = pd.getReadMethod();
    if (method.getAnnotation(Transient.class) != null) {
      return true;
    }
    if (method.getAnnotation(NoHistory.class) != null) {
      return true;
    }
    Field f = PrivateBeanUtils.findField(entity, pd.getName());
    if (f == null) {
      return false;
    }
    if (f.getAnnotation(NoHistory.class) != null) {
      return true;
    }
    return false;
  }

  /**
   * Checks if is standard ignore field.
   *
   * @param pd the pd
   * @return true, if is standard ignore field
   */
  protected boolean isStandardIgnoreField(PropertyDescriptor pd)
  {
    String name = pd.getName();
    if (name.equals("updateCounter") == true ||
        name.equals("modifiedAt") == true ||
        name.equals("modifiedBy") == true) {
      return true;
    }
    return false;
  }

}
