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
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LsLoggingLocalSettingsConfigModel extends BaseLoggingLocalSettingsConfigModel
{
  @ALocalSettingsPath(defaultValue = "log4j", comment = "Type of the used logging")
  private String typeId;
  /**
   * Created logging
   */
  private LocalSettingsConfigModel nested;

  public LsLoggingLocalSettingsConfigModel()
  {

  }

  public LsLoggingLocalSettingsConfigModel(String lsPrefix)
  {
    super(lsPrefix);
  }

  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    super.fromLocalSettings(localSettings);
    LsLoggingDescription desc = findByTypeId(typeId);
    nested = desc.getConfigModel();
    nested.fromLocalSettings(localSettings);

  }

  @Override
  public void validate(ValContext ctx)
  {
    nested.validate(ctx);
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
    return ((BaseLoggingLocalSettingsConfigModel) nested).createLogging();
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
}
