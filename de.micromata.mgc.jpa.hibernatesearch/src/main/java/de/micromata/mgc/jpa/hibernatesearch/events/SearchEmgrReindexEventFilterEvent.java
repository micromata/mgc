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

package de.micromata.mgc.jpa.hibernatesearch.events;

import de.micromata.genome.jpa.events.EmgrFilterEvent;
import de.micromata.mgc.jpa.hibernatesearch.api.ISearchEmgr;

/**
 * Filters an explicite reindex call to SearchEmgr.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SearchEmgrReindexEventFilterEvent extends EmgrFilterEvent<Void>
{

  /**
   * The entity.
   */
  private Object entity;

  /**
   * The search emgr.
   */
  private ISearchEmgr<?> searchEmgr;

  /**
   * Instantiates a new search emgr reindex event filter event.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public SearchEmgrReindexEventFilterEvent(ISearchEmgr<?> emgr, Object entity)
  {
    super(emgr);
    this.entity = entity;
    this.searchEmgr = emgr;
  }

  /**
   * Gets the entity.
   *
   * @return the entity
   */
  public Object getEntity()
  {
    return entity;
  }

  /**
   * Sets the entity.
   *
   * @param entity the new entity
   */
  public void setEntity(Object entity)
  {
    this.entity = entity;
  }

  /**
   * Gets the search emgr.
   *
   * @return the search emgr
   */
  public ISearchEmgr<?> getSearchEmgr()
  {
    return searchEmgr;
  }

}
