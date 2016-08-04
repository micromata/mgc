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
  public enum Type
  {
    NOT_TIMEABLE, PERIOD, INSTANT_OF_TIME
  }

  public enum DayMonthGranularity
  {
    DAY, MONTH;
  }

  private DayMonthGranularity dayMonthGranularity;

  public DayMonthGranularity getDayMonthGranularity()
  {
    return dayMonthGranularity;
  }

  public void setDayMonthGranularity(DayMonthGranularity dayMonthGranularity)
  {
    this.dayMonthGranularity = dayMonthGranularity;
  }

  private Type type;

  public Type getType()
  {
    return type;
  }

  public void setType(Type type)
  {
    this.type = type;
  }

  /**
   * The name of the group, used as column in the "timed master" table of the entity.
   */
  private String name;

  /**
   * The i18n key of the title.
   */
  private String i18nKey;

  /**
   * The i18n key of the start time.
   */
  private String i18nKeyStartTime;

  /**
   * The descriptions.
   */
  private List<AttrDescription> descriptions;

  /**
   * Submenu name as i18n key
   */
  private String i18nKeySubmenu;

  /**
   * Instantiates a new attr group.
   */
  public AttrGroup()
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

  public String getI18nKey()
  {
    return i18nKey;
  }

  public void setI18nKey(String i18nKey)
  {
    this.i18nKey = i18nKey;
  }

  public String getI18nKeyStartTime()
  {
    return i18nKeyStartTime;
  }

  public void setI18nKeyStartTime(String i18nKeyStartTime)
  {
    this.i18nKeyStartTime = i18nKeyStartTime;
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

  public String getI18nKeySubmenu()
  {
    return i18nKeySubmenu;
  }

  public void setI18nKeySubmenu(String i18nKeySubmenu)
  {
    this.i18nKeySubmenu = i18nKeySubmenu;
  }

}
