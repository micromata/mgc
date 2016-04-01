package de.micromata.genome.logging.config;

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.logging.LogConfigurationDAO;
import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.spi.BaseLogConfigurationLocalSettingsConfigModel;
import de.micromata.genome.logging.spi.BaseLoggingLocalSettingsConfigModel;
import de.micromata.genome.logging.spi.FileLogConfigurationDAOImpl;
import de.micromata.genome.logging.spi.log4j.Log4JLogConfigurationDAOImpl;
import de.micromata.genome.logging.spi.log4j.Log4JLogging;

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
      public String typeId()
      {
        return "log4j";
      }

      @Override
      public String toString()
      {
        return "Log4J Logging";
      }

      @Override
      public String description()
      {
        return "Writes the logging into Log4J. You need to configure log4j";
      }

      @Override
      public BaseLoggingLocalSettingsConfigModel getConfigModel()
      {
        return new BaseLoggingLocalSettingsConfigModel()
        {

          @Override
          public Logging createLogging()
          {
            return new Log4JLogging();
          }

        };
      }

    };
  }

  @Override
  public List<LsLogConfigurationDescription> getLsLogConfigurationImpls()
  {
    List<LsLogConfigurationDescription> ret = new ArrayList<>();
    ret.add(createLogPropFileConfigDescription());
    ret.add(createLog4JConfigDescription());

    return ret;
  }

  protected LsLogConfigurationDescription createLog4JConfigDescription()
  {
    return new LsLogConfigurationDescription()
    {

      @Override
      public String typeId()
      {
        return "log4jconfig";
      }

      @Override
      public String description()
      {
        return "Uses the log4j Configuration";
      }

      @Override
      public BaseLogConfigurationLocalSettingsConfigModel getConfigModel()
      {
        return new BaseLogConfigurationLocalSettingsConfigModel()
        {

          @Override
          public LogConfigurationDAO createLogConfigurationDAO()
          {
            return new Log4JLogConfigurationDAOImpl();
          }

        };
      }

    };
  }

  protected LsLogConfigurationDescription createLogPropFileConfigDescription()
  {
    return new LsLogConfigurationDescription()
    {

      @Override
      public String typeId()
      {
        return "propfile";
      }

      @Override
      public String description()
      {
        return "Uses property file Configuration";
      }

      @Override
      public BaseLogConfigurationLocalSettingsConfigModel getConfigModel()
      {
        return new BaseLogConfigurationLocalSettingsConfigModel()
        {

          @Override
          public LogConfigurationDAO createLogConfigurationDAO()
          {
            FileLogConfigurationDAOImpl ret = new FileLogConfigurationDAOImpl();
            ret.setFqFileName("./GenomeLogConfig.properties");
            return ret;
          }

        };
      }

    };
  }

}
