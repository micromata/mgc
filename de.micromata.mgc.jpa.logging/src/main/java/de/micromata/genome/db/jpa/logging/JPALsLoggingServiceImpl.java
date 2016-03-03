package de.micromata.genome.db.jpa.logging;

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.logging.FallbackLogging;
import de.micromata.genome.logging.config.LoggingWithFallbackLocalSettingsConfigModel;
import de.micromata.genome.logging.config.LsLoggingService;

/**
 * Loader service for base logging implementations.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JPALsLoggingServiceImpl implements LsLoggingService
{

  @Override
  public List<LsLoggingDescription> getLsLoggingImpls()
  {
    List<LsLoggingDescription> ret = new ArrayList<>();
    ret.add(createJpaLogging());
    return ret;
  }

  LsLoggingDescription createJpaLogging()
  {
    return new LsLoggingDescription()
    {

      @Override
      public String typeId()
      {
        return "jpa";
      }

      @Override
      public String name()
      {
        return "Genome Database Logging via JPA";
      }

      @Override
      public String description()
      {
        return "Need to declare datasource under JNDI java:/comp/env/genome/jdbc/dsLogging";
      }

      @Override
      public LoggingWithFallbackLocalSettingsConfigModel getConfigModel()
      {
        return new LoggingWithFallbackLocalSettingsConfigModel()
        {

          @Override
          protected FallbackLogging createFallbackLogging()
          {
            return new GenomeJpaLoggingImpl();
          }

        };
      }

    };
  }

}
