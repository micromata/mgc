package de.micromata.genome.logging.spi.ifiles;

import java.io.File;

import org.junit.After;
import org.junit.Before;

import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.LoggingServiceManager;

public class IFileTestBase
{
  protected int sizeLimit = -1;
  Logging prevLogging;

  protected IndexFileLoggingImpl logger;

  @Before
  public void setup()
  {
    prevLogging = LoggingServiceManager.get().getLogging();
    logger = new IndexFileLoggingImpl(true);
    if (sizeLimit != -1) {
      logger.setSizeLimit(sizeLimit);
    }
    File target = new File("./target/" + getClass().getSimpleName());
    if (target.exists() == false) {
      target.mkdirs();
    }
    for (File listf : target.listFiles()) {
      listf.delete();
    }
    logger.setLogDir(target);
    logger.setBaseFileName("TestLog");
    logger.initialize();
    LoggingServiceManager.get().setLogging(logger);
  }

  @After
  public void tearDown()
  {
    LoggingServiceManager.get().setLogging(prevLogging);
  }

}
