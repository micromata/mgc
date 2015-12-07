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
