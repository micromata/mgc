/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   24.01.2009
// Copyright Micromata 24.01.2009
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;

/**
 * AttrSetter/Getter implementation based on BeanUtils
 * 
 * The set-Method must called always with the same BEAN type.
 *
 * @author roger@micromata.de
 * @param <BEAN> the generic type
 * @param <VAL> the generic type
 */
public class PropertyAttrSetterGetter<BEAN, VAL>implements NamedAttrSetter<BEAN, VAL>, NamedAttrGetter<BEAN, VAL>
{

  /**
   * The property name.
   */
  private String propertyName;

  /**
   * The prop descriptor.
   */
  private transient PropertyDescriptor propDescriptor;

  /**
   * Instantiates a new property attr setter getter.
   *
   * @param propertyName the property name
   */
  public PropertyAttrSetterGetter(String propertyName)
  {
    this.propertyName = propertyName;
  }

  /**
   * Instantiates a new property attr setter getter.
   *
   * @param propDescriptor the prop descriptor
   */
  public PropertyAttrSetterGetter(PropertyDescriptor propDescriptor)
  {
    this.propertyName = propDescriptor.getName();
    this.propDescriptor = propDescriptor;
  }

  /**
   * Inits the.
   *
   * @param bean the bean
   */
  private void init(BEAN bean)
  {
    if (propDescriptor != null) {
      return;
    }

    try {
      propDescriptor = PropertyUtils.getPropertyDescriptor(bean, propertyName);
    } catch (Exception ex) {
      throw new RuntimeException(
          "Cannot retrieve property " + propertyName + " from bean class " + bean.getClass().getName());
    }
  }

  @Override
  public void set(BEAN bean, VAL value)
  {
    init(bean);
    try {
      propDescriptor.getWriteMethod().invoke(bean, new Object[] { value });
    } catch (RuntimeException ex) { // NOSONAR "Illegal Catch" framework
      throw ex;// NOSONAR "Illegal Catch" framework
    } catch (InvocationTargetException ex) {
      Throwable nex = ex.getCause();
      if (nex instanceof RuntimeException) {
        throw (RuntimeException) nex; // NOSONAR "Illegal Catch" framework
      }
      throw new RuntimeException(nex); // NOSONAR "Illegal Catch" framework
    } catch (Exception ex) { // NOSONAR "Illegal Catch" framework
      throw new RuntimeException(ex); // NOSONAR "Illegal Catch" framework
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public VAL get(BEAN bean)
  {
    init(bean);
    try {
      return (VAL) propDescriptor.getReadMethod().invoke(bean, ArrayUtils.EMPTY_OBJECT_ARRAY);
    } catch (RuntimeException ex) {// NOSONAR "Illegal Catch" framework 
      throw ex;// NOSONAR "Illegal Catch" framework
    } catch (InvocationTargetException ex) {
      Throwable nex = ex.getCause();
      if (nex instanceof RuntimeException) { // NOSONAR "Illegal Catch" framework
        throw (RuntimeException) nex; // NOSONAR "Illegal Catch" framework
      }
      throw new RuntimeException(nex);// NOSONAR "Illegal Catch" framework
    } catch (Exception ex) {// NOSONAR "Illegal Catch" framework
      throw new RuntimeException(ex);// NOSONAR "Illegal Catch" framework
    }
  }

  @Override
  public String getName()
  {
    return propertyName;
  }

  public String getPropertyName()
  {
    return propertyName;
  }

  public void setPropertyName(String propertyName)
  {
    this.propertyName = propertyName;
  }

  public PropertyDescriptor getPropDescriptor()
  {
    return propDescriptor;
  }

  public void setPropDescriptor(PropertyDescriptor propDescriptor)
  {
    this.propDescriptor = propDescriptor;
  }

}
