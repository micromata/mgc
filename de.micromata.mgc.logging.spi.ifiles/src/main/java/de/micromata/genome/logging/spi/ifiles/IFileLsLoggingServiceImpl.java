package de.micromata.genome.logging.spi.ifiles;

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.logging.config.LsLoggingService;
import de.micromata.genome.logging.spi.BaseLoggingLocalSettingsConfigModel;

/**
 * Loader service for base logging implementations.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class IFileLsLoggingServiceImpl implements LsLoggingService
{

  @Override
  public List<LsLoggingDescription> getLsLoggingImpls()
  {
    List<LsLoggingDescription> ret = new ArrayList<>();
    ret.add(createIFileLogging());
    return ret;
  }

  LsLoggingDescription createIFileLogging()
  {
    return new LsLoggingDescription()
    {

      @Override
      public String typeId()
      {
        return "ifile";
      }

      @Override
      public String name()
      {
        return "Indexed File Logging";
      }

      @Override
      public String description()
      {
        return "Writes the logging into logfiles with addiationally indece for searching";
      }

      @Override
      public BaseLoggingLocalSettingsConfigModel getConfigModel()
      {
        return new IFileLoggingLocalSettingsConfigModel();
      }

    };
  }

}
