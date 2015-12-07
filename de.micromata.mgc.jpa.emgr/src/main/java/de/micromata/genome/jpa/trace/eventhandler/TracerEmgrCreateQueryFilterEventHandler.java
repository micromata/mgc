package de.micromata.genome.jpa.trace.eventhandler;

import de.micromata.genome.jpa.events.EmgrCreateQueryFilterEvent;
import de.micromata.genome.jpa.events.EmgrEventHandler;
import de.micromata.genome.jpa.trace.JpaTracer;
import de.micromata.genome.jpa.trace.TracedQuery;

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
