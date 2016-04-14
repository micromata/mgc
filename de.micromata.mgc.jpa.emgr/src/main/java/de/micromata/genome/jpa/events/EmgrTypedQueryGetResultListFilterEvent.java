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

import java.util.List;

import javax.persistence.TypedQuery;

import de.micromata.genome.jpa.IEmgr;

/**
 * Executing getResultList on typed Query.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <R> the generic type
 */
public class EmgrTypedQueryGetResultListFilterEvent<R>extends EmgrFilterEvent<List<R>>
{

  /**
   * The query.
   */
  private TypedQuery<R> query;

  public TypedQuery<R> getQuery()
  {
    return query;
  }

  /**
   * Instantiates a new emgr type query get result list filter event.
   *
   * @param emgr the emgr
   * @param query the query
   */
  public EmgrTypedQueryGetResultListFilterEvent(IEmgr<?> emgr, TypedQuery<R> query)
  {
    super(emgr);
    this.query = query;
  }

}
