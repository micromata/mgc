package de.micromata.genome.db.jpa.history.api;

/**
 * Description of a History Property.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class HistProp
{

  /**
   * The name.
   */
  private String name;

  /**
   * The type.
   */
  private String type;

  /**
   * The value.
   */
  private String value;

  public HistProp()
  {

  }

  public HistProp(String name, String type, String value)
  {
    super();
    this.name = name;
    this.type = type;
    this.value = value;
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

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

}
