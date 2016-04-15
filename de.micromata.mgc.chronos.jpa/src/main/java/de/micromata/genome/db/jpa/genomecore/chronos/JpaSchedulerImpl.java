package de.micromata.genome.db.jpa.genomecore.chronos;

import de.micromata.genome.chronos.manager.SchedulerBaseDAO;
import de.micromata.genome.chronos.spi.Dispatcher;

/**
 * The Class JpaSchedulerImpl.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class JpaSchedulerImpl extends SchedulerBaseDAO
{

  @Override
  public Dispatcher createDispatcher(String virtualHostName) throws Exception
  {
    JpaJobStore jobstore = new JpaJobStore();
    Dispatcher dispatcher = createDispatcher(virtualHostName, jobstore);
    return dispatcher;
  }

}
