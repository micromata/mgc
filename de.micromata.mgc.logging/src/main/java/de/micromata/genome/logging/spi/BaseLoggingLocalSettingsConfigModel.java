package de.micromata.genome.logging.spi;

import de.micromata.genome.util.runtime.config.ALocalSettingsPath;
import de.micromata.genome.util.runtime.config.AbstractLocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

public class BaseLoggingLocalSettingsConfigModel extends AbstractLocalSettingsConfigModel
{
  protected String prefix = "genome.logging.";
  @ALocalSettingsPath()
  private String id;

  @Override
  public String getKeyPrefix()
  {
    return "genome.logging.";
  }

  @Override
  public void validate(ValContext ctx)
  {
  }

}
