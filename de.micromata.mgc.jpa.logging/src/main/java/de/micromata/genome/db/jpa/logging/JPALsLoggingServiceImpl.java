package de.micromata.genome.db.jpa.logging;

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.spi.BaseLoggingLocalSettingsConfigModel;
import de.micromata.genome.logging.spi.LsLoggingService;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;

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
      public String id()
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
      public Logging createLogging()
      {
        return new GenomeJpaLoggingImpl();
      }

      @Override
      public LocalSettingsConfigModel getConfigModel()
      {
        // TODO RK later hier datasource oder wanders?
        return new BaseLoggingLocalSettingsConfigModel();
      }

    };
  }

}
