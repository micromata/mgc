package de.micromata.mgc.db.jpa.trace.eventhandler;

import de.micromata.mgc.db.jpa.api.events.EmgrEventHandler;
import de.micromata.mgc.db.jpa.api.events.EmgrInsertDbRecordFilterEvent;
import de.micromata.mgc.db.jpa.trace.JpaTracer;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TracerEmgrInsertDbRecordFilterEventHandler implements EmgrEventHandler<EmgrInsertDbRecordFilterEvent>
{

  @Override
  public void onEvent(EmgrInsertDbRecordFilterEvent event)
  {
    JpaTracer jpaTracer = StatsJpaTracer.get();
    jpaTracer.execute("insert " + event.getEntity().getClass().getSimpleName(),
        new Object[] {},
        () -> {
          event.nextFilter();
          return null;
        });

  }

}
