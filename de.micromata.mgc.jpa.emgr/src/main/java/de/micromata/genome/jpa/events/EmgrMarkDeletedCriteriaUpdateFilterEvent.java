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

import de.micromata.genome.jpa.CriteriaUpdate;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.MarkDeletableRecord;

/**
 * Event, in case an entity should be marked as deleted.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 * @param <T> the type which must extends {@link MarkDeletableRecord}
 */
public class EmgrMarkDeletedCriteriaUpdateFilterEvent<T extends MarkDeletableRecord<?>>
    extends EmgrMarkDeleteUndeleteCriteriaUpdateFilterEvent<T>
{
  /**
   * Instantiates a new emgr update criteria update filter event.
   *
   * @param emgr the emgr
   * @param update the update
   * @param entity the entity
   */
  public EmgrMarkDeletedCriteriaUpdateFilterEvent(IEmgr<?> emgr, T entity, CriteriaUpdate<T> update)
  {
    super(emgr, entity, update);
  }
}
