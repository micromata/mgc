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

package de.micromata.genome.jpa.events.impl;

import java.util.List;

import jakarta.persistence.Query;

import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.WrappedQuery;
import de.micromata.genome.jpa.events.EmgrQueryGetResultListFilterEvent;

/**
 * Event on the call Query.getResultList().
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrEventQuery extends WrappedQuery
{
  /**
   * The emgr.
   */
  private IEmgr<?> emgr;

  public EmgrEventQuery(IEmgr<?> emgr, Query nested)
  {
    super(nested);
    this.emgr = emgr;
  }

  @Override
  public List getResultList()
  {
    return emgr.getEmgrFactory().getEventFactory().invokeEvents(
        new EmgrQueryGetResultListFilterEvent(emgr, nested),
        (event) -> {
          event.setResult(nested.getResultList());
        });
  }

}
