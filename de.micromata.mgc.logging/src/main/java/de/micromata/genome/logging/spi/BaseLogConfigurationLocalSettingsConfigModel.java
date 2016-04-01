package de.micromata.genome.logging.spi;

import de.micromata.genome.logging.LogConfigurationDAO;
import de.micromata.genome.util.runtime.config.ALocalSettingsPath;
import de.micromata.genome.util.runtime.config.AbstractLocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class BaseLogConfigurationLocalSettingsConfigModel extends AbstractLocalSettingsConfigModel
{
  protected String prefix = "genome.logging.config";

  @ALocalSettingsPath(defaultValue = "log4j", comment = "Type of the used logging")
  private String typeId;

  public BaseLogConfigurationLocalSettingsConfigModel()
  {
    this("genome.logging.config");
  }

  public BaseLogConfigurationLocalSettingsConfigModel(String prefix)
  {
    this.prefix = prefix;
  }

  public abstract LogConfigurationDAO createLogConfigurationDAO();

  @Override
  public String getKeyPrefix()
  {
    return prefix;
  }

  public String getPrefix()
  {
    return prefix;
  }

  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }

  @Override
  public void validate(ValContext ctx)
  {
  }

  public String getTypeId()
  {
    return typeId;
  }

  public void setTypeId(String typeId)
  {
    this.typeId = typeId;
  }

}
