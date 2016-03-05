package de.micromata.genome.util.runtime.config;

import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.runtime.LocalSettings;

/**
 * Abstract implementation for a LocalSettingsConfigModel.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class AbstractLocalSettingsConfigModel implements LocalSettingsConfigModel
{
  /**
   * Used to create a section comment.
   */
  protected String comment;

  public AbstractLocalSettingsConfigModel()
  {

  }

  public AbstractLocalSettingsConfigModel(String comment)
  {
    this.comment = comment;
  }

  @Override
  public String getSectionComment()
  {
    return comment;
  }

  public void setSectionComment(String comment)
  {
    this.comment = comment;
  }

  /**
   * store the configuration into local settings.
   * 
   * @param props
   */
  @Override
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    return LocalSettingsConfigUtils.toProperties(this, writer);
  }

  /**
   * load the configuration from local settings.
   * 
   * @param localSettings
   */
  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    LocalSettingsConfigUtils.initFromLocalSettings(this, localSettings);
  }

  /**
   * Used for prefix map frm ALocalSettingsPath key.
   * 
   * @return
   */
  public String getKeyPrefix()
  {
    return "";
  }

  @Override
  public String buildKey(String key)
  {
    return LocalSettingsConfigUtils.join(getKeyPrefix(), key);
  }

  @Override
  public String findCommentForProperty(String localProperty)
  {
    Field field = PrivateBeanUtils.findField(getClass(), localProperty);
    if (field == null) {
      return null;
    }
    ALocalSettingsPath asp = field.getAnnotation(ALocalSettingsPath.class);
    if (asp == null) {
      return null;
    }
    return asp.comment();
  }

  public static LocalSettings setIfBlank(LocalSettings localSettings, String key, String value)
  {
    if (StringUtils.isBlank(localSettings.get(key)) == true) {
      localSettings.getMap().put(key, value);
    }
    return localSettings;
  }
}
