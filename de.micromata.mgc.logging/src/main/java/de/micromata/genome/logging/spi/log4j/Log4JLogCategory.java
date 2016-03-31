package de.micromata.genome.logging.spi.log4j;

import de.micromata.genome.logging.BaseLogging;
import de.micromata.genome.logging.LogCategory;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public enum Log4JLogCategory implements LogCategory
{
  Log4J;
  static {
    BaseLogging.registerLogCategories(values());
  }
  private String fqName;

  private Log4JLogCategory()
  {
    fqName = getPrefix() + "." + name();
  }

  @Override
  public String getFqName()
  {
    return fqName;
  }

  @Override
  public String getPrefix()
  {
    return "L4J";
  }

}
