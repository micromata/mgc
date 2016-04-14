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

package de.micromata.genome.util.runtime.config;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.bean.FieldMatchers;
import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.runtime.LocalSettings;

/**
 * Utilities to manage configuration mappings.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class LocalSettingsConfigUtils
{

  /**
   * Inits the from local settings.
   *
   * @param bean the bean
   * @param localSettings the local settings
   */
  public static void initFromLocalSettings(LocalSettingsConfigModel bean, LocalSettings localSettings)
  {
    List<Field> fields = PrivateBeanUtils.findAllFields(bean.getClass(),
        FieldMatchers.hasAnnotation(ALocalSettingsPath.class));
    for (Field field : fields) {
      ALocalSettingsPath lsp = field.getAnnotation(ALocalSettingsPath.class);
      String key = lsp.key();
      if ("<fieldName>".equals(key) == true) {
        key = field.getName();
      }
      PrivateBeanUtils.writeField(bean, field, localSettings.get(bean.buildKey(key), lsp.defaultValue()));
    }
  }

  /**
   * To properties.
   *
   * @param bean the bean
   * @param ret the ret
   */
  public static LocalSettingsWriter toProperties(LocalSettingsConfigModel bean, LocalSettingsWriter writer)
  {
    String comment = bean.getSectionComment();
    LocalSettingsWriter lw = writer.newSection(comment);
    toPropertiesInSection(bean, lw);
    return lw;
  }

  public static void toPropertiesInSection(LocalSettingsConfigModel bean, LocalSettingsWriter writer)
  {
    List<Field> fields = PrivateBeanUtils.findAllFields(bean.getClass(),
        FieldMatchers.hasAnnotation(ALocalSettingsPath.class));
    for (Field field : fields) {
      ALocalSettingsPath lsp = field.getAnnotation(ALocalSettingsPath.class);
      String key = lsp.key();
      if ("<fieldName>".equals(key) == true) {
        key = field.getName();
      }
      String val = (String) PrivateBeanUtils.readField(bean, field);
      writer.put(bean.buildKey(key), val, lsp.comment());
    }
  }

  public static String join(String prefix, String key)
  {
    if (StringUtils.isBlank(prefix) == true) {
      return key;
    }
    return prefix + "." + key;
  }

}
