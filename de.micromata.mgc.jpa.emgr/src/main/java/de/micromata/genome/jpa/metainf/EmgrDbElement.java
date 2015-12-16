package de.micromata.genome.jpa.metainf;

/**
 * The Interface EmgrDbElement.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface EmgrDbElement
{

  /**
   * Gets the java type.
   *
   * @return the java type
   */
  Class<?> getJavaType();

  /**
   * Gets the database name.
   *
   * @return the database name
   */
  String getDatabaseName();
}
