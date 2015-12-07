package de.micromata.mgc.db.jpa.trace.eventhandler;

import de.micromata.mgc.db.jpa.api.events.EmgrEventHandler;
import de.micromata.mgc.db.jpa.api.events.EmgrRemoveDbRecordFilterEvent;
import de.micromata.mgc.db.jpa.trace.JpaTracer;

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
    jpaTracer.execute("remove " + event.getEntity().getClass().getSimpleName(),
        new Object[] { "pk", event.getEntity().getPk() },
        () -> {
          event.nextFilter();
          return null;
        });
  }

}
