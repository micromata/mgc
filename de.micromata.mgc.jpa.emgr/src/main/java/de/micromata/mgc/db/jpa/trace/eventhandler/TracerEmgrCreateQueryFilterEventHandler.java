package de.micromata.mgc.db.jpa.trace.eventhandler;

import de.micromata.mgc.db.jpa.api.events.EmgrCreateQueryFilterEvent;
import de.micromata.mgc.db.jpa.api.events.EmgrEventHandler;
import de.micromata.mgc.db.jpa.trace.JpaTracer;
import de.micromata.mgc.db.jpa.trace.TracedQuery;

/**
 * Wrapps the Query with a Traced Query.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TracerEmgrCreateQueryFilterEventHandler implements EmgrEventHandler<EmgrCreateQueryFilterEvent>
{

  @Override
  public void onEvent(EmgrCreateQueryFilterEvent event)
  {

    event.nextFilter();
    JpaTracer jpaTracer = StatsJpaTracer.get();
    event.setResult(new TracedQuery(jpaTracer, event.getResult(), event.getSql(), new String[] {}));
  }

}
