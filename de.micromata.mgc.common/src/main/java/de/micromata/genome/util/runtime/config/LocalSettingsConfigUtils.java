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
  public static void toProperties(LocalSettingsConfigModel bean, LocalSettingsWriter writer)
  {
    String comment = bean.getSectionComment();
    LocalSettingsWriter lw = writer.newSection(comment);
    toPropertiesInSection(bean, lw);
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
