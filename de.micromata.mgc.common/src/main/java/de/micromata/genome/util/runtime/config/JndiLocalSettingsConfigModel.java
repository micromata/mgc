package de.micromata.genome.util.runtime.config;

import de.micromata.genome.util.validation.ValContext;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JndiLocalSettingsConfigModel extends AbstractLocalSettingsConfigModel
{
  public static enum DataType
  {
    DataSource, MailSession, String, Boolean
  }

  /**
   * Internal id inside the condif
   */
  @ALocalSettingsPath(comment = "Intern name of of the jndi")
  private String name;

  @ALocalSettingsPath(comment = "type of the jndi target value")
  private String type;
  @ALocalSettingsPath(comment = "reference to the source of the jndi target value")
  private String source;
  /**
   * Target JDNI name
   */
  @ALocalSettingsPath(comment = "JNDI name published the jndi value")
  private String target;

  public JndiLocalSettingsConfigModel(String name)
  {
    this.name = name;
  }

  public JndiLocalSettingsConfigModel(String name, DataType type, String target)
  {
    this.name = name;
    this.source = name;
    this.type = type.name();
    this.target = target;
  }

  @Override
  public String getKeyPrefix()
  {
    return "jndi.bind." + name;
  }

  @Override
  public void validate(ValContext ctx)
  {

  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getSource()
  {
    return source;
  }

  public void setSource(String source)
  {
    this.source = source;
  }

  public String getTarget()
  {
    return target;
  }

  public void setTarget(String target)
  {
    this.target = target;
  }

}
