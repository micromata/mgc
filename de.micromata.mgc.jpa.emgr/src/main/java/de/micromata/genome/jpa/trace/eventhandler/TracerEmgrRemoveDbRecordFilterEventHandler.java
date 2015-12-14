package de.micromata.genome.jpa.trace.eventhandler;

import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.events.EmgrEventHandler;
import de.micromata.genome.jpa.events.EmgrRemoveDbRecordFilterEvent;
import de.micromata.genome.jpa.trace.JpaTracer;

/**
 * Traces Inserts.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TracerEmgrRemoveDbRecordFilterEventHandler implements EmgrEventHandler<EmgrRemoveDbRecordFilterEvent>
{

  @Override
  public void onEvent(EmgrRemoveDbRecordFilterEvent event)
  {
    JpaTracer jpaTracer = StatsJpaTracer.get();
    Object ent = event.getEntity();
    if (ent instanceof DbRecord) {
      jpaTracer.execute("remove " + event.getEntity().getClass().getSimpleName(),
          new Object[] { "pk", ((DbRecord) ent).getPk() },
          () -> {
            event.nextFilter();
            return null;
          });
    }

  }

}
