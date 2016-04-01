package de.micromata.genome.logging.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

import de.micromata.genome.logging.LogConfigurationDAO;
import de.micromata.genome.logging.config.LsLoggingService;
import de.micromata.genome.logging.config.LsLoggingService.LsLogConfigurationDescription;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.LocalSettingsWriter;

/**
 * Configure LogConfiguration by LocalSettings.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LsLogConfigurationLocalSettingsConfigModel extends BaseLogConfigurationLocalSettingsConfigModel
{
  private BaseLogConfigurationLocalSettingsConfigModel nested;

  public static List<LsLogConfigurationDescription> getAvailableServices()
  {
    List<LsLogConfigurationDescription> ret = new ArrayList<>();
    ServiceLoader<LsLoggingService> loader = ServiceLoader.load(LsLoggingService.class);
    for (LsLoggingService lgs : loader) {
      ret.addAll(lgs.getLsLogConfigurationImpls());
    }
    return ret;
  }

  public static LsLogConfigurationDescription findByTypeId(String typeId)
  {
    List<LsLogConfigurationDescription> services = getAvailableServices();
    Optional<LsLogConfigurationDescription> optional = services.stream().filter(e -> e.typeId().equals(typeId))
        .findFirst();
    if (optional.isPresent() == false) {
      return null;
    }
    return optional.get();
  }

  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    super.fromLocalSettings(localSettings);
    LsLogConfigurationDescription desc = findByTypeId(getTypeId());
    if (desc == null) {
      return;
    }
    nested = desc.getConfigModel();
    nested.fromLocalSettings(localSettings);
  }

  @Override
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    if (nested != null) {
      nested.setPrefix(getKeyPrefix());
      return nested.toProperties(writer);
    }
    return writer;
  }

  @Override
  public LogConfigurationDAO createLogConfigurationDAO()
  {
    return nested.createLogConfigurationDAO();
  }

}
