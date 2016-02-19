package de.micromata.genome.util.runtime.config;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

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
  public static void initFromLocalSettings(Object bean, LocalSettings localSettings)
  {
    List<Field> fields = PrivateBeanUtils.findAllFields(bean.getClass(),
        FieldMatchers.hasAnnotation(ALocalSettingsPath.class));
    for (Field field : fields) {
      ALocalSettingsPath lsp = field.getAnnotation(ALocalSettingsPath.class);
      PrivateBeanUtils.writeField(bean, field, localSettings.get(lsp.key(), lsp.defaultValue()));
    }
  }

  /**
   * To properties.
   *
   * @param bean the bean
   * @param ret the ret
   */
  public static void toProperties(Object bean, Map<String, String> ret)
  {
    List<Field> fields = PrivateBeanUtils.findAllFields(bean.getClass(),
        FieldMatchers.hasAnnotation(ALocalSettingsPath.class));
    for (Field field : fields) {
      ALocalSettingsPath lsp = field.getAnnotation(ALocalSettingsPath.class);
      String val = (String) PrivateBeanUtils.readField(bean, field);
      ret.put(lsp.key(), val);
    }
  }
}
