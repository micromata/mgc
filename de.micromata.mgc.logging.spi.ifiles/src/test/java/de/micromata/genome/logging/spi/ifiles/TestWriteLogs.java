package de.micromata.genome.logging.spi.ifiles;

import java.io.File;

import org.junit.Test;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.LoggingServiceManager;

public class TestWriteLogs
{
  private void runWithLogging(Logging newLogging, Runnable runable)
  {
    Logging logging = LoggingServiceManager.get().getLogging();
    try {

      LoggingServiceManager.get().setLogging(newLogging);
      runable.run();
    } finally {
      LoggingServiceManager.get().setLogging(logging);
    }
  }

  @Test
  public void testWrite()
  {
    IndexFileLoggingImpl logger = new IndexFileLoggingImpl(true);
    logger.setLogDir(new File("./target"));
    logger.setBaseFileName("TestLog");
    logger.initialize();
    runWithLogging(logger, () -> {
      int count = 1000;
      for (int i = 0; i < count; ++i) {
        if ((i % 10) == 0) {
          GLog.error(GenomeLogCategory.UnitTest,
              "Da ist was schlimmes passiert, sollte man nachschauen, besser waers" + i,
              new LogExceptionAttribute(new RuntimeException("Ojeoje")));
        } else {
          GLog.info(GenomeLogCategory.UnitTest, "Hallo nachricht " + i);
        }
      }
    });
  }
}
