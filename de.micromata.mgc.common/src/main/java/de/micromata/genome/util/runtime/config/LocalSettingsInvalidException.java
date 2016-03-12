package de.micromata.genome.util.runtime.config;

import java.util.List;

import de.micromata.genome.util.validation.ValMessage;
import de.micromata.genome.util.validation.ValMessagesException;

/**
 * Thrown if a configuration is not valid
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LocalSettingsInvalidException extends ValMessagesException
{
  private LocalSettingsConfigModel model;

  public LocalSettingsInvalidException(LocalSettingsConfigModel model, List<ValMessage> valMessages)
  {
    super(valMessages);
    this.model = model;
  }

  public LocalSettingsInvalidException(LocalSettingsConfigModel model, String message, List<ValMessage> valMessages)
  {
    super(message, valMessages);
    this.model = model;
  }

  public LocalSettingsConfigModel getModel()
  {
    return model;
  }

  public void setModel(LocalSettingsConfigModel model)
  {
    this.model = model;
  }

}
