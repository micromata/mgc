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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;

import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.validation.ValContext;

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

  /**
   * Instantiates a new abstract local settings config model.
   */
  public AbstractLocalSettingsConfigModel()
  {

  }

  /**
   * Instantiates a new abstract local settings config model.
   *
   * @param comment the comment
   */
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
   * @param writer the writer
   * @return the local settings writer
   */
  @Override
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    return LocalSettingsConfigUtils.toProperties(this, writer);
  }

  /**
   * load the configuration from local settings.
   * 
   * @param localSettings the localsettings 
   */
  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    LocalSettingsConfigUtils.initFromLocalSettings(this, localSettings);
  }

  /**
   * Ensure the config is correct, otherwise it throws exception.
   *
   * @throws LocalSettingsInvalidException the local settings invalid exception
   */
  public void ensureValide() throws LocalSettingsInvalidException
  {
    ValContext ctx = new ValContext();
    validate(ctx);
    if (ctx.hasErrors() == true) {
      throw new LocalSettingsInvalidException(this, ctx.getMessages());
    }
  }

  /**
   * Used for prefix map frm ALocalSettingsPath key.
   * 
   * @return an empty String
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

  /**
   * Sets the if blank.
   *
   * @param localSettings the local settings
   * @param key the key
   * @param value the value
   * @return the local settings
   */
  public static LocalSettings setIfBlank(LocalSettings localSettings, String key, String value)
  {
    if (StringUtils.isBlank(localSettings.get(key)) == true) {
      localSettings.getMap().put(key, value);
    }
    return localSettings;
  }

  /**
   * Reset fiel to default.
   *
   * @param fieldName the field name
   */
  protected void resetFielToDefault(String fieldName)
  {
    Field f = PrivateBeanUtils.findField(getClass(), fieldName);
    Validate.notNull(f);
    ALocalSettingsPath ap = f.getAnnotation(ALocalSettingsPath.class);
    PrivateBeanUtils.writeField(this, f, ap.defaultValue());
  }

  /**
   * Checks if is true.
   *
   * @param value the value
   * @return true, if is true
   */
  protected boolean isTrue(String value)
  {
    return "true".equalsIgnoreCase(value);
  }

  /**
   * Checks if is int.
   *
   * @param value the value
   * @return true, if is int
   */
  protected boolean isInt(String value)
  {
    if (StringUtils.isBlank(value) == true) {
      return false;
    }
    return NumberUtils.isDigits(value);
  }

  /**
   * As int.
   *
   * @param value the value
   * @return the int
   */
  protected int asInt(String value)
  {
    return Integer.parseInt(value, 10);
  }

  /**
   * Checks if is long.
   *
   * @param value the value
   * @return true, if is long
   */
  protected boolean isLong(String value)
  {
    if (StringUtils.isBlank(value) == true) {
      return false;
    }
    return NumberUtils.isDigits(value);
  }

  /**
   * As long.
   *
   * @param value the value
   * @return the long
   */
  protected long asLong(String value)
  {
    return Long.parseLong(value, 10);
  }
}
