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

package de.micromata.genome.db.jpa.tabattr.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderColumn;

import de.micromata.genome.db.jpa.tabattr.api.TimeableAttrRow;

/**
 * Base class for a timeable table.
 *
 * @author Roger Kommer (r.kommer.extern@micromata.de)
 *
 */
@MappedSuperclass
public abstract class TimeableBaseDO<M extends TimeableBaseDO<?, ?>, PK extends Serializable>
    extends JpaTabMasterBaseDO<M, PK>
    implements TimeableAttrRow<PK>
{
  private Date startTime;

  private String groupName;

  public TimeableBaseDO()
  {

  }

  @Override
  @OrderColumn()
  @Column(name = "START_TIME", nullable = false)
  public Date getStartTime()
  {
    return startTime;
  }

  @Override
  public void setStartTime(final Date startTime)
  {
    this.startTime = startTime;
  }

  @Column(name = "GROUP_NAME", nullable = false)
  public String getGroupName()
  {
    return groupName;
  }

  public void setGroupName(String groupName)
  {
    this.groupName = groupName;
  }

}
