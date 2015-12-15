/////////////////////////////////////////////////////////////////////////////
//
// Project ProjectForge Community Edition
//         www.projectforge.org
//
// Copyright (C) 2001-2014 Kai Reinhard (k.reinhard@micromata.de)
//
// ProjectForge is dual-licensed.
//
// This community edition is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License as published
// by the Free Software Foundation; version 3 of the License.
//
// This community edition is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
// Public License for more details.
//
// You should have received a copy of the GNU General Public License along
// with this program; if not, see http://www.gnu.org/licenses/.
//
/////////////////////////////////////////////////////////////////////////////

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

  private Date endTime;

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

  @Override
  @Column(name = "END_TIME", nullable = true)
  public Date getEndTime()
  {
    return endTime;
  }

  @Override
  public void setEndTime(final Date endTime)
  {
    this.endTime = endTime;
  }

}
