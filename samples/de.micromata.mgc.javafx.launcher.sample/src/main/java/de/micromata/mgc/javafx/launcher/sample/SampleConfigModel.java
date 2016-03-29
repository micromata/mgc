package de.micromata.mgc.javafx.launcher.sample;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.runtime.config.ALocalSettingsPath;
import de.micromata.genome.util.runtime.config.AbstractLocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

/**
 * A simple configuration model
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SampleConfigModel extends AbstractLocalSettingsConfigModel
{
  @ALocalSettingsPath(comment = "A Sample configuration value")
  private String myValue;

  /**
   * Build property names with given prefix.
   * 
   * In this case sample.launcher.myValue.
   * 
   * {@inheritDoc}
   *
   */
  @Override
  public String getKeyPrefix()
  {

    return "sample.launcher";
  }

  /**
   * Check if configuration is valid.
   * 
   * If an error will be added to ValContext, the application will not be started.
   * 
   * {@inheritDoc}
   *
   */
  @Override
  public void validate(ValContext ctx)
  {
    if (StringUtils.isBlank(myValue) == true) {
      ctx.directError("myValue", "Please give a value");
    }
  }
}
