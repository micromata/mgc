package de.micromata.genome.db.jpa.logging;

import org.junit.Before;
import org.junit.Ignore;

import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.mgc.common.test.MgcTestCase;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
@Ignore
public class LogTestBase extends MgcTestCase
{
  @Before
  public void initLogger()
  {

    LoggingServiceManager.get().setLogging(new GenomeJpaLoggingImpl());

  }
}
