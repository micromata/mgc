package de.micromata.genome.util.bean;

/**
 * A getter which gets the content of the simple typed bean itself.
 * 
 * @author roger
 * 
 */
public class SimpleTypeGetter<BEAN>implements AttrGetter<BEAN, BEAN>
{
  /**
   * Get the bean itself.
   * 
   * {@inheritDoc}
   *
   */
  @Override
  public BEAN get(BEAN bean)
  {
    return bean;
  }

}
