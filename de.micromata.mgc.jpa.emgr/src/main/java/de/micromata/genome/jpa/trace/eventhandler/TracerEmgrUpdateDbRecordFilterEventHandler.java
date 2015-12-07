package de.micromata.genome.jpa.trace.eventhandler;

import de.micromata.genome.jpa.events.EmgrEventHandler;
import de.micromata.genome.jpa.events.EmgrUpdateDbRecordFilterEvent;
import de.micromata.genome.jpa.trace.JpaTracer;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TracerEmgrUpdateDbRecordFilterEventHandler implements EmgrEventHandler<EmgrUpdateDbRecordFilterEvent>
{

  @Override
  public void onEvent(EmgrUpdateDbRecordFilterEvent event)
  {
    JpaTracer jpaTracer = StatsJpaTracer.get();
    jpaTracer.execute("update " + event.getEntity().getClass().getSimpleName(),
        new Object[] { "pk", event.getEntity().getPk() },
        () -> {
          event.nextFilter();
          return null;
        });

  }

}
