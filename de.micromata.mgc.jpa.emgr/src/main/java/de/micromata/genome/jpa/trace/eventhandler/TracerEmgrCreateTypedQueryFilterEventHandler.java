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

package de.micromata.genome.jpa.trace.eventhandler;

import de.micromata.genome.jpa.events.EmgrCreateTypedQueryFilterEvent;
import de.micromata.genome.jpa.events.EmgrEventHandler;
import de.micromata.genome.jpa.trace.JpaTracer;
import de.micromata.genome.jpa.trace.TracedTypedQuery;

/**
 * Wrapps the TypedQuery with a Traced TypedQuery.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TracerEmgrCreateTypedQueryFilterEventHandler
    implements EmgrEventHandler<EmgrCreateTypedQueryFilterEvent<?>>
{

  @Override
  public void onEvent(EmgrCreateTypedQueryFilterEvent<?> event)
  {
    event.nextFilter();
    JpaTracer jpaTracer = StatsJpaTracer.get();
    event.setResult(new TracedTypedQuery(jpaTracer, event.getResult(), event.getSql(), new Object[] {}));
  }

}
