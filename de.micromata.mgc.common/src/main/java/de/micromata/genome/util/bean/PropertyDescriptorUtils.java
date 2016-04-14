//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.util.bean;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;

/**
 * Utils to simplify usage of PropertyUtils.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class PropertyDescriptorUtils
{

  /**
   * Gets the property descriptors.
   *
   * @param clazz the clazz
   * @return the property descriptors
   */
  public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz)
  {
    return BeanUtilsBean.getInstance().getPropertyUtils().getPropertyDescriptors(clazz);
  }

  /**
   * Find anotation first on getter than from field.
   *
   * @param <T> the generic type
   * @param beanClazz the bean clazz
   * @param pd the pd
   * @param anotclass the anotclass
   * @return the t
   */
  public static <T extends Annotation> T findAnotation(Class<?> beanClazz, PropertyDescriptor pd, Class<T> anotclass)
  {
    Method rm = pd.getReadMethod();
    if (rm != null) {
      T anot = rm.getAnnotation(anotclass);
      if (anot != null) {
        return anot;
      }
    }
    Field field = PrivateBeanUtils.findField(beanClazz, pd.getName());
    if (field == null) {
      return null;
    }
    T anot = field.getAnnotation(anotclass);
    return anot;
  }

  /**
   * Read property.
   *
   * @param bean the bean
   * @param pd the pd
   * @return the object
   * @throws PropertyAccessException the property access exception
   */
  public static Object readProperty(Object bean, PropertyDescriptor pd) throws PropertyAccessException
  {
    PropertyUtilsBean proputils = BeanUtilsBean.getInstance().getPropertyUtils();
    Object value;
    try {
      value = proputils.getSimpleProperty(bean, pd.getName());
      return value;
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
      throw new PropertyAccessException("getSimpleProperty", bean, pd, ex);
    }
  }

  /**
   * Write property.
   *
   * @param bean the bean
   * @param pd the pd
   * @param value the value
   * @throws PropertyAccessException the property access exception
   */
  public static void writeProperty(Object bean, PropertyDescriptor pd, Object value) throws PropertyAccessException
  {
    PropertyUtilsBean proputils = BeanUtilsBean.getInstance().getPropertyUtils();
    try {
      proputils.setSimpleProperty(bean, pd.getName(), value);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
      throw new PropertyAccessException("setSimpleProperty", bean, pd, ex);
    }
  }
}
