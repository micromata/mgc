package de.micromata.mgc.db.jpa;

import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;

/**
 * Copy from/to entities using Properties descriptors with standard annotations.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class PropertyEntityCopier implements EntityCopier
{

  /**
   * The Constant SEMICOLON_SPACE.
   */
  private static final String SEMICOLON_SPACE = "; ";

  /**
   * The Constant DOT.
   */
  private static final String DOT = ".";

  @Override
  public <T> void copyTo(Class<? extends T> iface, T dest, T orig)
  {
    for (PropertyDescriptor pd : EmgrPropertyUtils.getEntityPropertyDescriptors(iface)) {
      copyProperty(pd, dest, orig);
    }

  }

  /**
   * Copy property.
   *
   * @param pd the pd
   * @param target the target
   * @param source the source
   */
  public static void copyProperty(PropertyDescriptor pd, Object target, Object source)
  {
    PropertyUtilsBean proputils = BeanUtilsBean.getInstance().getPropertyUtils();
    Object value = null;
    try {
      value = proputils.getSimpleProperty(source, pd.getName());
    } catch (Exception ex) { // NOSONAR "Illegal Catch" Framework
      /**
       * @logging
       * @reason Das interne Lesen einer Objekteigenschaft ist nicht möglich.
       * @action Interner Fehler. Entwickler / Support kontaktieren.
       */
      GLog.warn(GenomeLogCategory.Jpa, "Cannot read bean property: " + source.getClass().getSimpleName() + DOT
          + pd.getName() + SEMICOLON_SPACE + ex.getMessage(), new LogExceptionAttribute(ex));
      return;
    }
    writeProperty(proputils, target, pd, value);
  }

  /**
   * Write property.
   *
   * @param proputils the proputils
   * @param entity the entity
   * @param pd the pd
   * @param value the value
   */
  public static void writeProperty(PropertyUtilsBean proputils, Object entity, PropertyDescriptor pd, Object value)
  {
    try {
      proputils.setSimpleProperty(entity, pd.getName(), value);
    } catch (Exception ex) { // NOSONAR "Illegal catch" framework wants to handle common exceptions
      /**
       * @logging
       * @reason Das interne Schreiben einer Objekteigenschaft ist nicht möglich.
       * @action Interner Fehler. Entwickler / Support kontaktieren.
       */
      GLog.warn(GenomeLogCategory.Jpa, "Cannot write bean property: " + entity.getClass().getSimpleName() + DOT
          + pd.getName() + SEMICOLON_SPACE + ex.getMessage(), new LogExceptionAttribute(ex));
    }
  }
}
