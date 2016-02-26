package de.micromata.genome.util.runtime.config.jdbc;

import de.micromata.genome.util.runtime.config.JdbcLocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

/**
 * Wrapps support of a JDBC connection.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface JdbProviderService
{
  /**
   * Name for Display in dropdowns.
   * 
   * @return
   */
  String getName();

  /**
   * Internal id.
   * 
   * @return
   */
  default String getId()
  {
    return getClass().getSimpleName();
  }

  /**
   * Class name of the jdbc driver.
   * 
   * @return
   */
  String getJdbcDriver();

  /**
   * Build a sample url by given appname.
   * 
   * @param appName
   * @return
   */
  String getSampleUrl(String appName);

  boolean requiredUser();

  boolean requiresPass();

  boolean tryConnect(JdbcLocalSettingsConfigModel model, ValContext ctx);
}
