package de.micromata.mgc.db.jpa.trace.eventhandler;

import de.micromata.mgc.db.jpa.api.events.EmgrEventHandler;
import de.micromata.mgc.db.jpa.api.events.EmgrFindByPkFilterEvent;
import de.micromata.mgc.db.jpa.trace.JpaTracer;

/**
 * Wrapps the findByPk.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TracerFindByPkFilterEventHandler implements EmgrEventHandler<EmgrFindByPkFilterEvent<?, ?>>
{

  /**
   * The jpa tracer.
   */
  private JpaTracer jpaTracer = StatsJpaTracer.get();

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public void onEvent(EmgrFindByPkFilterEvent<?, ?> event)
  {
    jpaTracer.execute("selectByPk " + event.getEntityClass().getSimpleName(), new Object[] { "pk", event.getPk() },
        () -> {
          event.nextFilter();
          return null;
        });
  }

}
