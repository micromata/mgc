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

package de.micromata.mgc.javafx.launcher.sample;

import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.util.runtime.config.ALocalSettingsPath;
import de.micromata.genome.util.runtime.config.AbstractLocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

/**
 * A simple configuration model
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SampleConfigModel extends AbstractLocalSettingsConfigModel
{
  @ALocalSettingsPath(comment = "A Sample configuration value")
  private String myValue;

  /**
   * Build property names with given prefix.
   * 
   * In this case sample.launcher.myValue.
   * 
   * {@inheritDoc}
   *
   */
  @Override
  public String getKeyPrefix()
  {

    return "sample.launcher";
  }

  /**
   * Check if configuration is valid.
   * 
   * If an error will be added to ValContext, the application will not be started.
   * 
   * {@inheritDoc}
   *
   */
  @Override
  public void validate(ValContext ctx)
  {
    if (StringUtils.isBlank(myValue) == true) {
      ctx.directError("myValue", "Please give a value");
    }
  }
}
