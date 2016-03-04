package de.micromata.genome.db.jpa.logging;

import de.micromata.genome.logging.LoggingWithFallback;
import de.micromata.genome.logging.config.LoggingWithFallbackLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.JdbcLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.JndiLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.LocalSettingsWriter;
import de.micromata.genome.util.validation.ValContext;

/**
 * JPA Logging with datasource and jdni.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JpaLoggingLocalSettingsConfigModel extends LoggingWithFallbackLocalSettingsConfigModel
{
  JdbcLocalSettingsConfigModel jdbcConfig;

  public JpaLoggingLocalSettingsConfigModel()
  {
    jdbcConfig = new JdbcLocalSettingsConfigModel("genomelog", "Logging Database",
        new JndiLocalSettingsConfigModel("genomelog", JndiLocalSettingsConfigModel.DataType.DataSource,
            "java:/comp/env/genome/jdbc/dsLogging"));

  }

  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    super.fromLocalSettings(localSettings);
    jdbcConfig.fromLocalSettings(localSettings);
  }

  @Override
  public void validate(ValContext ctx)
  {
    super.validate(ctx);
    jdbcConfig.validate(ctx);
  }

  @Override
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    super.toProperties(writer);
    jdbcConfig.toProperties(writer);
    return writer;
  }

  @Override
  protected LoggingWithFallback createFallbackLogging()
  {
    return new GenomeJpaLoggingImpl();
  }

  @Override
  public JdbcLocalSettingsConfigModel getJdbcConfig()
  {
    return jdbcConfig;
  }

  public void setJdbcConfig(JdbcLocalSettingsConfigModel jdbcConfig)
  {
    this.jdbcConfig = jdbcConfig;
  }

}
