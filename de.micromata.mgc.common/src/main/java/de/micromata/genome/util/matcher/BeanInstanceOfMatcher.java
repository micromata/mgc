package de.micromata.genome.util.matcher;

/**
 * Matches if given Object is instanceof class.
 *
 * @author roger@micromata.de
 */
public class BeanInstanceOfMatcher extends MatcherBase<Object>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 7753923065375842215L;

  /**
   * The of class.
   */
  private Class<?> ofClass;

  /**
   * Instantiates a new bean instance of matcher.
   */
  public BeanInstanceOfMatcher()
  {

  }

  /**
   * Instantiates a new bean instance of matcher.
   *
   * @param ofClass the of class
   */
  public BeanInstanceOfMatcher(Class<?> ofClass)
  {
    this.ofClass = ofClass;
  }

  @Override
  public boolean match(Object object)
  {
    if (object == null) {
      return false;
    }
    return ofClass.isAssignableFrom(object.getClass());
  }

  public Class<?> getOfClass()
  {
    return ofClass;
  }

  public void setOfClass(Class<?> ofClass)
  {
    this.ofClass = ofClass;
  }
}
