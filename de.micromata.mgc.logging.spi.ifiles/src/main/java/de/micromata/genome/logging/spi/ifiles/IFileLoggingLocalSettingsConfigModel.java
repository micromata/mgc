package de.micromata.genome.logging.spi.ifiles;

import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.spi.BaseLoggingLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.ALocalSettingsPath;
import de.micromata.genome.util.validation.ValContext;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class IFileLoggingLocalSettingsConfigModel extends BaseLoggingLocalSettingsConfigModel
{

  @ALocalSettingsPath(defaultValue = "100", comment = "Size limit of the log files in MB")
  private String sizeLimit;
  @ALocalSettingsPath(defaultValue = "logs", comment = "Directory where to store log files")
  private String logPath;
  @ALocalSettingsPath(defaultValue = "Genome", comment = "Base file name of the log files")
  private String baseFileName;

  @Override
  public void validate(ValContext ctx)
  {
    // TODO Auto-generated method stub
    super.validate(ctx);
  }

  @Override
  public Logging createLogging()
  {
    IndexFileLoggingImpl ret = new IndexFileLoggingImpl();
    // TODO RK initialize by cfg;
    return ret;
  }
}
