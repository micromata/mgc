package de.micromata.genome.jpa;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Transient;

import de.micromata.genome.util.bean.PropertyDescriptorUtils;

/**
 * Used to copy properties from/to Entities.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrPropertyUtils
{

  /**
   * The base types.
   */
  private static Set<Class<?>> baseTypes = new HashSet<Class<?>>();

  static {
    baseTypes.add(String.class);
    baseTypes.add(Short.class);
    baseTypes.add(Short.TYPE);
    baseTypes.add(Integer.class);
    baseTypes.add(Integer.TYPE);
    baseTypes.add(Long.class);
    baseTypes.add(Long.TYPE);
    baseTypes.add(Character.class);
    baseTypes.add(Character.TYPE);
    baseTypes.add(Date.class);
    baseTypes.add(BigDecimal.class);
    baseTypes.add(Boolean.class);
    baseTypes.add(Boolean.TYPE);
    baseTypes.add(Enum.class);
  }

  /**
   * these properties should not be copied.
   */
  private static Set<String> ignoreProperties = new HashSet<String>();

  static {
    ignoreProperties.add("modifiedAt");
    ignoreProperties.add("modifiedBy");
    ignoreProperties.add("createdAt");
    ignoreProperties.add("createdBy");
  }

  /**
   * Cache of the descriptors of an destination.
   */
  private static Map<Class<?>, Map<String, Column>> destDescriptorMap = new ConcurrentHashMap<Class<?>, Map<String, Column>>();

  /**
   * Cache of the descriptors of an interface.
   */
  private static Map<Class<?>, List<PropertyDescriptor>> descriptorMap = new ConcurrentHashMap<Class<?>, List<PropertyDescriptor>>();

  /**
   * Gets the entity property descriptors.
   *
   * @param iface the iface
   * @return the entity property descriptors
   */
  public static List<PropertyDescriptor> getEntityPropertyDescriptors(Class<?> iface) // NOSONAR "Methods should not be too complex" Framework
  {
    List<PropertyDescriptor> ret = descriptorMap.get(iface);
    if (ret != null) {
      return ret;
    }
    ret = new ArrayList<PropertyDescriptor>();
    List<Class<?>> ifaces = new ArrayList<Class<?>>();
    collectInterfaces(iface, ifaces);
    Set<String> names = new HashSet<String>();
    for (Class<?> ifac : ifaces) {

      PropertyDescriptor[] descriptors = PropertyDescriptorUtils.getPropertyDescriptors(ifac);
      for (PropertyDescriptor pd : descriptors) {
        String name = pd.getName();
        if (names.contains(name) == true) {
          continue;
        }
        names.add(name);
        if (ignoreProperties.contains(name) == true) {
          continue;
        }
        if (pd.getReadMethod() == null || pd.getWriteMethod() == null) {
          continue;
        }

        if (pd.getReadMethod().getAnnotation(Transient.class) != null) {
          continue;
        }
        ret.add(pd);
      }
    }
    descriptorMap.put(iface, ret);
    return ret;
  }

  /**
   * Checks if is base type.
   *
   * @param cls the cls
   * @return true, if is base type
   */
  public static boolean isBaseType(Class<?> cls)
  {
    return baseTypes.contains(cls);
  }

  /**
   * Checks if is base type or enum.
   *
   * @param cls the cls
   * @return true, if is base type or enum
   */
  public static boolean isBaseTypeOrEnum(Class<?> cls)
  {
    if (cls.isEnum() == true) {
      return true;
    }
    if (isBaseType(cls) == true) {
      return true;
    }
    return false;
  }

  /**
   * Collect interfaces.
   *
   * @param clazz the clazz
   * @param clazzes the clazzes
   */
  static void collectInterfaces(Class<?> clazz, List<Class<?>> clazzes)
  {
    clazzes.add(clazz);
    for (Class<?> ifc : clazz.getInterfaces()) {
      collectInterfaces(ifc, clazzes);
    }
  }

  /**
   * Gets the entity property descriptors.
   *
   * @param iface the iface
   * @param name the name
   * @return the entity property descriptors. null if not found.
   */
  public static PropertyDescriptor getEntityPropertyDescriptor(Class<?> iface, String name)
  {
    List<PropertyDescriptor> props = getEntityPropertyDescriptors(iface);
    for (PropertyDescriptor prop : props) {
      if (prop.getName().equals(name) == true) {
        return prop;
      }
    }
    return null;
  }
}
