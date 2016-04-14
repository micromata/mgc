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
import de.micromata.genome.jpa.IEmgr;

/**
 * Filters de.micromata.genome.jpa.Emgr.merge(DbRecord).
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <E> the element type
 */
public class EmgrMergeDbRecordFilterEvent<E extends DbRecord>extends EmgrFilterEvent<E>
{

  /**
   * The entity.
   */
  private E entity;

  /**
   * Instantiates a new emgr merge db record filter event.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public EmgrMergeDbRecordFilterEvent(IEmgr<?> emgr, E entity)
  {
    super(emgr);
    this.entity = entity;
  }

  public E getEntity()
  {
    return entity;
  }
}
