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

package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.EntityCopyStatus;
import de.micromata.genome.jpa.IEmgr;

/**
 * Filters a update copy event.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrUpdateCopyFilterEvent extends EmgrFilterEvent<EntityCopyStatus>
{

  /**
   * The iface.
   */
  private Class<?> iface;

  /**
   * The entity class.
   */
  private Class<?> entityClass;

  /**
   * The target.
   */
  private DbRecord target;

  /**
   * The source.
   */
  private Object source;

  /**
   * The force update.
   */
  private boolean forceUpdate;

  /**
   * Instantiates a new emgr update copy filter event.
   *
   * @param emgr the emgr
   * @param iface the iface
   * @param entityClass the entity class
   * @param target the target
   * @param source the source
   * @param forceUpdate the force update
   */
  public EmgrUpdateCopyFilterEvent(IEmgr<?> emgr, Class<?> iface, Class<? extends DbRecord> entityClass,
      DbRecord target, DbRecord source,
      boolean forceUpdate)
  {
    super(emgr);
    this.iface = iface;
    this.entityClass = entityClass;
    this.target = target;
    this.source = source;
    this.forceUpdate = forceUpdate;
  }

  public Class<?> getIface()
  {
    return iface;
  }

  public void setIface(Class<?> iface)
  {
    this.iface = iface;
  }

  public DbRecord getTarget()
  {
    return target;
  }

  public void setTarget(DbRecord target)
  {
    this.target = target;
  }

  public Object getSource()
  {
    return source;
  }

  public void setSource(Object source)
  {
    this.source = source;
  }

  public boolean isForceUpdate()
  {
    return forceUpdate;
  }

  public void setForceUpdate(boolean forceUpdate)
  {
    this.forceUpdate = forceUpdate;
  }

  public Class<?> getEntityClass()
  {
    return entityClass;
  }

  public void setEntityClass(Class<?> entityClass)
  {
    this.entityClass = entityClass;
  }

}
