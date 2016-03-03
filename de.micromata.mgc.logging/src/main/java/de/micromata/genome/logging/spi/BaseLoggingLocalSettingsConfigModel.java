package de.micromata.genome.logging.spi;

import de.micromata.genome.logging.Logging;
import de.micromata.genome.util.runtime.config.ALocalSettingsPath;
import de.micromata.genome.util.runtime.config.AbstractLocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

public abstract class BaseLoggingLocalSettingsConfigModel extends AbstractLocalSettingsConfigModel
{
  protected String prefix = "genome.logging.";
  @ALocalSettingsPath()
  private String id;

  public BaseLoggingLocalSettingsConfigModel()
  {
    this("genome.logging.");
  }

  public BaseLoggingLocalSettingsConfigModel(String prefix)
  {
    this.prefix = prefix;
  }

  public abstract Logging createLogging();

  @Override
  public String getKeyPrefix()
  {
    return prefix;
  }

  @Override
  public void validate(ValContext ctx)
  {
  }

}
