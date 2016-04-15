package de.micromata.genome.db.jpa.genomecore.chronos;

import de.micromata.genome.chronos.spi.AbstractFutureJob;

/**
 * Just a dummy job.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class DummyJob extends AbstractFutureJob
{

  @Override
  public Object call(Object argument) throws Exception
  {
    return null;
  }

}
