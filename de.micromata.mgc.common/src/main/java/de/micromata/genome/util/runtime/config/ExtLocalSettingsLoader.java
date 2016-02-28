package de.micromata.genome.util.runtime.config;

import de.micromata.genome.util.collections.OrderedProperties;
import de.micromata.genome.util.runtime.StdLocalSettingsLoader;

/**
 * Uses another porperty parser, which also hold the orginal structure including comments.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ExtLocalSettingsLoader extends StdLocalSettingsLoader
{
  OrderedPropertiesWithComments origProps = new OrderedPropertiesWithComments();

  public ExtLocalSettingsLoader()
  {
  }

  @Override
  protected OrderedProperties newProperties(boolean originalLocalSettingsFile)
  {
    if (originalLocalSettingsFile == true) {
      return origProps;
    }
    return new OrderedPropertiesWithComments();
  }

}