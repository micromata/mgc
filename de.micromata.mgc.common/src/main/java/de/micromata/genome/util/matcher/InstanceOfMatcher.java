package de.micromata.genome.util.matcher;

/**
 * Matches if class is matching instance of.
 * 
 * @author roger
 * 
 */
public class InstanceOfMatcher extends MatcherBase<Object>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 1346403525135515211L;

  /**
   * The clazz.
   */
  private Class<?> clazz;

  /**
   * Instantiates a new instance of matcher.
   */
  public InstanceOfMatcher()
  {

  }

  /**
   * Instantiates a new instance of matcher.
   *
   * @param clazz the clazz
   */
  public InstanceOfMatcher(String clazz)
  {
    try {
      this.clazz = Class.forName(clazz);
    } catch (ClassNotFoundException cne) {
      /**
       * @logging
       * @reason Kann die angegebene Klasse nicht laden
       * @action Konfiguration überprüfen
       */
      throw new RuntimeException("Could not load class: " + clazz + "; " + cne.getMessage(), cne);
    }
  }

  @Override
  public boolean match(Object object)
  {
    return clazz.isAssignableFrom(object.getClass());
  }

  @Override
  public String toString()
  {
    return "<EXPR> instanceof " + clazz.getCanonicalName();
  }

  public Class<?> getClazz()
  {
    return clazz;
  }

  public void setClazz(Class<?> clazz)
  {
    this.clazz = clazz;
  }

}
