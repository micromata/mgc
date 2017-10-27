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

package de.micromata.genome.logging.spi;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import de.micromata.genome.logging.Logging;
import de.micromata.genome.util.runtime.config.ALocalSettingsPath;
import de.micromata.genome.util.runtime.config.AbstractLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.JdbcLocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class BaseLoggingLocalSettingsConfigModel extends AbstractLocalSettingsConfigModel
{
  protected String prefix = "genome.logging";

  @ALocalSettingsPath(defaultValue = "log4j", comment = "Type of the used logging")
  private String typeId;

  @ALocalSettingsPath(defaultValue = "10241024", comment = "Default LogAttribute length. Default value is 1 MB")
  private String maxLogAttrLength;

  public BaseLoggingLocalSettingsConfigModel()
  {
    this("genome.logging");
  }

  public BaseLoggingLocalSettingsConfigModel(String prefix)
  {
    this.prefix = prefix;
  }

  public abstract Logging createLogging();

  public JdbcLocalSettingsConfigModel getJdbcConfig()
  {
    return null;
  }

  @Override
  public String getKeyPrefix()
  {
    return prefix;
  }

  public String getPrefix()
  {
    return prefix;
  }

  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }

  @Override
  public void validate(ValContext ctx)
  {
    if (StringUtils.isBlank(maxLogAttrLength) == true) {
      maxLogAttrLength = "10241024";
    }
    if (NumberUtils.isDigits(maxLogAttrLength) == false) {
      ctx.directError("maxLogAttrLength", "maxLogAttrLength requires int");
    }
  }

  public String getTypeId()
  {
    return typeId;
  }

  public void setTypeId(String typeId)
  {
    this.typeId = typeId;
  }

  public String getMaxLogAttrLength()
  {
    return maxLogAttrLength;
  }

  public int getMaxLogAttrLengthAsInt()
  {
    return Integer.parseInt(maxLogAttrLength);
  }

  public void setMaxLogAttrLength(String maxLogAttrLength)
  {
    this.maxLogAttrLength = maxLogAttrLength;
  }

}
