package de.micromata.mgc.db.jpa.trace.eventhandler;

import de.micromata.mgc.db.jpa.api.events.EmgrEventHandler;
import de.micromata.mgc.db.jpa.api.events.EmgrUpdateDbRecordFilterEvent;
import de.micromata.mgc.db.jpa.trace.JpaTracer;

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
