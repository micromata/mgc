package de.micromata.genome.jpa.trace.eventhandler;

import de.micromata.genome.jpa.events.EmgrEventHandler;
import de.micromata.genome.jpa.events.EmgrMergeDbRecordFilterEvent;
import de.micromata.genome.jpa.trace.JpaTracer;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TracerEmgrMergeDbRecordFilterEventHandler implements EmgrEventHandler<EmgrMergeDbRecordFilterEvent<?>>
{

  @Override
  public void onEvent(EmgrMergeDbRecordFilterEvent<?> event)
  {
    JpaTracer jpaTracer = StatsJpaTracer.get();
    jpaTracer.execute("merge " + event.getEntity().getClass().getSimpleName(),
        new Object[] { "pk", event.getEntity().getPk() },
        () -> {
          event.nextFilter();
          return null;
        });

  }

}
