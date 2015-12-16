package de.micromata.genome.jpa;

import java.beans.PropertyDescriptor;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.bean.PropertyAccessException;
import de.micromata.genome.util.bean.PropertyDescriptorUtils;

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
  public <T> EntityCopyStatus copyTo(IEmgr<?> emgr, Class<? extends T> iface, T dest, T orig,
      String... ignoreCopyFields)
  {
    return copyToImpl(emgr, iface, dest, orig, ignoreCopyFields);
  }

  protected <T> EntityCopyStatus copyToImpl(IEmgr<?> emgr, Class<? extends T> iface, T dest, T orig, String... ignores)
  {
    EntityCopyStatus ret = EntityCopyStatus.NONE;
    for (PropertyDescriptor pd : EmgrPropertyUtils.getEntityPropertyDescriptors(iface)) {
      if (ArrayUtils.contains(ignores, pd.getName()) == true) {
        continue;
      }
      ret = ret.combine(copyProperty(emgr, pd, iface, dest, orig));
    }
    return ret;
  }

  public static EntityCopyStatus copyProperty(IEmgr<?> emgr, PropertyDescriptor pd, Class<?> iface, Object target,
      Object source)
  {
    EntityCopy ecant = PropertyDescriptorUtils.findAnotation(target.getClass(), pd, EntityCopy.class);
    if (ecant == null) {
      return copyPropertyDefault(emgr, pd, iface, target, source);
    }
    if (ecant.noCopy() == true) {
      return EntityCopyStatus.NONE;
    }
    if (ecant.deepCopy() == true) {
      return copyPropertyDeep(emgr, pd, iface, target, source);
    }
    if (ecant.copier().length == 1 && ecant.copier()[0] == PropertyEntityCopier.class) {
      return copyPropertyDefault(emgr, pd, iface, target, source);
    }

    EntityCopyStatus ret = EntityCopyStatus.NONE;
    for (Class<? extends EntityCopier> clazz : ecant.copier()) {
      EntityCopier copier = PrivateBeanUtils.createInstance(clazz);
      ret = ret.combine(copier.copyTo(emgr, iface, target, source));
    }
    return ret;
  }

  static EntityCopyStatus copyPropertyDeep(IEmgr<?> emgr, PropertyDescriptor pd, Class<?> iface, Object target,
      Object source)
  {
    Object svalue = null;
    try {
      svalue = PropertyDescriptorUtils.readProperty(source, pd);
    } catch (PropertyAccessException ex) {
      /**
       * @logging
       * @reason Das interne Lesen einer Objekteigenschaft ist nicht möglich.
       * @action Interner Fehler. Entwickler / Support kontaktieren.
       */
      GLog.warn(GenomeLogCategory.Jpa, "Cannot read bean property: " + source.getClass().getSimpleName() + DOT
          + pd.getName() + SEMICOLON_SPACE + ex.getMessage(), new LogExceptionAttribute(ex));
      return EntityCopyStatus.NONE;
    }
    if (svalue == null) {
      return copyPropertyDefault(emgr, pd, iface, target, source);
    }
    Object tvalue;
    try {
      tvalue = PropertyDescriptorUtils.readProperty(source, pd);
    } catch (PropertyAccessException ex) {
      /**
       * @logging
       * @reason Das interne Lesen einer Objekteigenschaft ist nicht möglich.
       * @action Interner Fehler. Entwickler / Support kontaktieren.
       */
      GLog.warn(GenomeLogCategory.Jpa, "Cannot read bean property: " + source.getClass().getSimpleName() + DOT
          + pd.getName() + SEMICOLON_SPACE + ex.getMessage(), new LogExceptionAttribute(ex));
      return EntityCopyStatus.NONE;
    }

    if (tvalue == null) {
      return copyPropertyDefault(emgr, pd, iface, target, source);
    }
    return EmgrCopyUtils.copyTo(emgr, tvalue.getClass(), tvalue, svalue);
  }

  static EntityCopyStatus copyPropertyDefault(IEmgr<?> emgr, PropertyDescriptor pd, Class<?> iface, Object target,
      Object source)
  {
    List<Class<? extends EntityCopier>> copierClazzes = emgr.getEmgrFactory()
        .getRegisteredCopierForBean(pd.getPropertyType());
    if (copierClazzes.isEmpty() == true) {
      return copyPropertyRaw(pd, target, source);
    }
    EntityCopyStatus ret = EntityCopyStatus.NONE;
    for (Class<? extends EntityCopier> copierClazz : copierClazzes) {
      EntityCopier copier = PrivateBeanUtils.createInstance(copierClazz);
      ret = ret.combine(copier.copyTo(emgr, iface, target, source));
    }
    return ret;
  }

  /**
   * Copy property.
   *
   * @param pd the pd
   * @param target the target
   * @param source the source
   */
  public static EntityCopyStatus copyPropertyRaw(PropertyDescriptor pd, Object target, Object source)
  {
    Object value = null;
    try {
      value = PropertyDescriptorUtils.readProperty(source, pd);
    } catch (PropertyAccessException ex) {
      /**
       * @logging
       * @reason Das interne Lesen einer Objekteigenschaft ist nicht möglich.
       * @action Interner Fehler. Entwickler / Support kontaktieren.
       */
      GLog.warn(GenomeLogCategory.Jpa, "Cannot read bean property: " + source.getClass().getSimpleName() + DOT
          + pd.getName() + SEMICOLON_SPACE + ex.getMessage(), new LogExceptionAttribute(ex));
      return EntityCopyStatus.NONE;
    }
    Object backupValue = null;
    try {
      backupValue = PropertyDescriptorUtils.readProperty(source, pd);
    } catch (PropertyAccessException ex) {
      /**
       * @logging
       * @reason Das interne Lesen einer Objekteigenschaft ist nicht möglich.
       * @action Interner Fehler. Entwickler / Support kontaktieren.
       */
      GLog.warn(GenomeLogCategory.Jpa, "Cannot read bean property: " + source.getClass().getSimpleName() + DOT
          + pd.getName() + SEMICOLON_SPACE + ex.getMessage(), new LogExceptionAttribute(ex));
      return EntityCopyStatus.NONE;
    }
    EntityCopyStatus ret = ObjectUtils.equals(value, backupValue) ? EntityCopyStatus.NONE : EntityCopyStatus.MAJOR;
    if (writeProperty(target, pd, value) == false) {
      return EntityCopyStatus.NONE;
    }
    return ret;

  }

  /**
   * Write property.
   *
   * @param proputils the proputils
   * @param entity the entity
   * @param pd the pd
   * @param value the value
   */
  public static boolean writeProperty(Object entity, PropertyDescriptor pd, Object value)
  {
    try {
      PropertyDescriptorUtils.writeProperty(entity, pd, value);
      return true;
    } catch (PropertyAccessException ex) {
      /**
       * @logging
       * @reason Das interne Schreiben einer Objekteigenschaft ist nicht möglich.
       * @action Interner Fehler. Entwickler / Support kontaktieren.
       */
      GLog.warn(GenomeLogCategory.Jpa, "Cannot write bean property: " + entity.getClass().getSimpleName() + DOT
          + pd.getName() + SEMICOLON_SPACE + ex.getMessage(), new LogExceptionAttribute(ex));
      return false;
    }
  }
}
