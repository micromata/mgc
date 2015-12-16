package de.micromata.genome.jpa.metainf;

/**
 * The Class EmgrDbElementBean.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class EmgrDbElementBean implements EmgrDbElement
{

  /**
   * The java type.
   */
  private Class<?> javaType;

  /**
   * The database name.
   */
  private String databaseName;

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Class<?> getJavaType()
  {
    return javaType;
  }

  /**
   * Sets the java type.
   *
   * @param javaType the new java type
   */
  public void setJavaType(Class<?> javaType)
  {
    this.javaType = javaType;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public String getDatabaseName()
  {
    return databaseName;
  }

  /**
   * Sets the database name.
   *
   * @param databaseName the new database name
   */
  public void setDatabaseName(String databaseName)
  {
    this.databaseName = databaseName;
  }

}
