package de.micromata.mgc.db.jpa.trace.eventhandler;

import de.micromata.mgc.db.jpa.api.events.EmgrEventHandler;
import de.micromata.mgc.db.jpa.api.events.EmgrMergeDbRecordFilterEvent;
import de.micromata.mgc.db.jpa.trace.JpaTracer;

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
