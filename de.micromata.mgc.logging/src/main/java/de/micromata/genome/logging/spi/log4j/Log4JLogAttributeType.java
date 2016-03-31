package de.micromata.genome.logging.spi.log4j;

import de.micromata.genome.logging.AttributeTypeDefaultFiller;
import de.micromata.genome.logging.BaseLogging;
import de.micromata.genome.logging.LogAttributeRenderer;
import de.micromata.genome.logging.LogAttributeType;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public enum Log4JLogAttributeType implements LogAttributeType
{
  Log4JCategory,

  Log4JMDC,

  Log4JLocation;
  static {
    BaseLogging.registerLogAttributeType(values());
  }

  @Override
  public String columnName()
  {
    return null;
  }

  @Override
  public int maxValueSize()
  {
    return 0;
  }

  @Override
  public boolean isSearchKey()
  {
    return false;
  }

  @Override
  public AttributeTypeDefaultFiller getAttributeDefaultFiller()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public LogAttributeRenderer getRenderer()
  {
    return null;
  }

}
