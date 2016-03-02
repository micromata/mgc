package de.micromata.genome.logging.spi;

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.spi.log4j.Log4JLogging;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;

/**
 * Loader service for base logging implementations.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class StdLsLoggingServiceImpl implements LsLoggingService
{

  @Override
  public List<LsLoggingDescription> getLsLoggingImpls()
  {
    List<LsLoggingDescription> ret = new ArrayList<>();
    ret.add(createLog4JLogging());
    return ret;
  }

  LsLoggingDescription createLog4JLogging()
  {
    return new LsLoggingDescription()
    {

      @Override
      public String id()
      {
        return "log4j";
      }

      @Override
      public String name()
      {
        return "Log4J Logging";
      }

      @Override
      public String description()
      {
        return "Writes the logging into Log4J. You need to configure log4j";
      }

      @Override
      public Logging createLogging()
      {
        return new Log4JLogging();
      }

      @Override
      public LocalSettingsConfigModel getConfigModel()
      {
        return new BaseLoggingLocalSettingsConfigModel();
      }

    };
  }

}
