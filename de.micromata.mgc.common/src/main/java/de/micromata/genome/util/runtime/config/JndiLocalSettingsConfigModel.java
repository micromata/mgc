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

import de.micromata.genome.util.validation.ValContext;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JndiLocalSettingsConfigModel extends AbstractLocalSettingsConfigModel
{
  public static enum DataType
  {
    DataSource, MailSession, String, Boolean
  }

  /**
   * Internal id inside the condif
   */
  @ALocalSettingsPath(comment = "Intern name of of the jndi")
  private String name;

  @ALocalSettingsPath(comment = "type of the jndi target value")
  private String type;
  @ALocalSettingsPath(comment = "reference to the source of the jndi target value")
  private String source;
  /**
   * Target JDNI name
   */
  @ALocalSettingsPath(comment = "JNDI name published the jndi value")
  private String target;

  public JndiLocalSettingsConfigModel(String name)
  {
    this.name = name;
  }

  public JndiLocalSettingsConfigModel(String name, DataType type, String target)
  {
    this.name = name;
    this.source = name;
    this.type = type.name();
    this.target = target;
  }

  @Override
  public String getKeyPrefix()
  {
    return "jndi.bind." + name;
  }

  @Override
  public void validate(ValContext ctx)
  {

  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getSource()
  {
    return source;
  }

  public void setSource(String source)
  {
    this.source = source;
  }

  public String getTarget()
  {
    return target;
  }

  public void setTarget(String target)
  {
    this.target = target;
  }

}
