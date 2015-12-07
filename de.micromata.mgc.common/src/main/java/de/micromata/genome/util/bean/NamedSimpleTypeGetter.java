package de.micromata.genome.util.bean;

/**
 * Simple name getter, which has a name.
 *
 * @author roger
 * @param <BEAN> the generic type
 */
public class NamedSimpleTypeGetter<BEAN>extends SimpleTypeGetter<BEAN>implements NamedAttrGetter<BEAN, BEAN>
{

  /**
   * The name.
   */
  private String name;

  /**
   * Instantiates a new named simple type getter.
   */
  public NamedSimpleTypeGetter()
  {

  }

  /**
   * Instantiates a new named simple type getter.
   *
   * @param name the name
   */
  public NamedSimpleTypeGetter(String name)
  {
    this.name = name;
  }

  @Override
  public String getName()
  {
    return name;
  }
}
