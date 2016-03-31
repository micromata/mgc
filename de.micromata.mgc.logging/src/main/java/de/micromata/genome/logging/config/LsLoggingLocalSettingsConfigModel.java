package de.micromata.genome.logging.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.config.LsLoggingService.LsLoggingDescription;
import de.micromata.genome.logging.spi.BaseLoggingLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.ALocalSettingsPath;
import de.micromata.genome.util.runtime.config.LocalSettingsWriter;
import de.micromata.genome.util.validation.ValContext;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LsLoggingLocalSettingsConfigModel extends BaseLoggingLocalSettingsConfigModel
{

  /**
   * Created logging
   */
  private BaseLoggingLocalSettingsConfigModel nested;

  @ALocalSettingsPath(defaultValue = "false", comment = "Write Log4J Logs into Genome Logging")
  private String log4JToGenomeLogging;

  public LsLoggingLocalSettingsConfigModel()
  {

  }

  public LsLoggingLocalSettingsConfigModel(String lsPrefix)
  {
    super(lsPrefix);
  }

  @Override
  public void validate(ValContext ctx)
  {
    if (nested == null) {
      ctx.directError("typeId", "No logging selected");
      return;
    }
    nested.validate(ctx);
  }

  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    super.fromLocalSettings(localSettings);
    LsLoggingDescription desc = findByTypeId(getTypeId());
    if (desc == null) {
      return;
    }
    nested = desc.getConfigModel();
    nested.fromLocalSettings(localSettings);
  }

  @Override
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    writer.put(getKeyPrefix() + ".log4JToGenomeLogging", log4JToGenomeLogging);
    if (nested != null) {
      nested.setPrefix(getKeyPrefix());
      return nested.toProperties(writer);
    }
    return writer;
  }

  public static LsLoggingDescription findByTypeId(String typeId)
  {
    List<LsLoggingDescription> services = getAvailableServices();
    Optional<LsLoggingDescription> optional = services.stream().filter(e -> e.typeId().equals(typeId)).findFirst();
    if (optional.isPresent() == false) {
      return null;
    }
    return optional.get();

  }

  @Override
  public Logging createLogging()
  {
    return nested.createLogging();
  }

  public static List<LsLoggingDescription> getAvailableServices()
  {
    List<LsLoggingDescription> ret = new ArrayList<>();
    ServiceLoader<LsLoggingService> loader = ServiceLoader.load(LsLoggingService.class);
    for (LsLoggingService lgs : loader) {
      ret.addAll(lgs.getLsLoggingImpls());
    }
    return ret;
  }

  public BaseLoggingLocalSettingsConfigModel getNested()
  {
    return nested;
  }

  public void setNested(BaseLoggingLocalSettingsConfigModel nested)
  {
    this.nested = nested;
  }

  public boolean isLog4JToGenomeLogging()
  {
    return Boolean.valueOf(log4JToGenomeLogging);
  }

  public String getLog4JToGenomeLogging()
  {
    return log4JToGenomeLogging;
  }

  public void setLog4JToGenomeLogging(String log4jToGenomeLogging)
  {
    log4JToGenomeLogging = log4jToGenomeLogging;
  }

}
