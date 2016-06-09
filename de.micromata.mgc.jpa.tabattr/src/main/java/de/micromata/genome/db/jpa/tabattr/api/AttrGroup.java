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

package de.micromata.genome.db.jpa.tabattr.api;

import java.io.Serializable;
import java.util.List;

public class AttrGroup implements Serializable
{

  /**
   * The descriptions.
   */
  private List<AttrDescription> descriptions;

  /**
   * The name of the group, used as column in the "timed master" table of the entity.
   */
  private String name;

  /**
   * The i18n key of the title.
   */
  private String i18nKey;

  /**
   * Instantiates a new attr group.
   */
  public AttrGroup()
  {

  }

  /**
   * Gets the descriptions.
   *
   * @return the descriptions
   */
  public List<AttrDescription> getDescriptions()
  {
    return descriptions;
  }

  /**
   * Sets the descriptions.
   *
   * @param descriptions the new descriptions
   */
  public void setDescriptions(final List<AttrDescription> descriptions)
  {
    this.descriptions = descriptions;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getI18nKey()
  {
    return i18nKey;
  }

  public void setI18nKey(String i18nKey)
  {
    this.i18nKey = i18nKey;
  }
}
