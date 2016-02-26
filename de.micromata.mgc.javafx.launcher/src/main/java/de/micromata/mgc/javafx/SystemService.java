package de.micromata.mgc.javafx;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.util.runtime.config.jdbc.JdbProviderService;
import de.micromata.genome.util.runtime.config.jdbc.JdbProviderServices;

/**
 * Some system services.
 * 
 * @author Roger Kommer (roger.kommer.extern@micromata.de)
 * 
 */
public class SystemService
{
  protected static SystemService INSTANCE = new SystemService();

  public static SystemService get()
  {
    return INSTANCE;
  }

  public boolean classExists(String name)
  {
    try {
      Class.forName(name, false, getClass().getClassLoader());
      return true;
    } catch (ClassNotFoundException ex) {
      return false;
    }
  }

  public List<JdbProviderService> getJdbcDrivers()
  {
    return JdbProviderServices.getAvailableJdbcServices();
  }

  public void openUrlInBrowser(String url)
  {
    Desktop desktop = null;
    if (Desktop.isDesktopSupported()) {
      desktop = Desktop.getDesktop();
    } else {
      GLog.warn(GenomeLogCategory.System, "Launching Browser not supported");
      return;
    }

    if (desktop != null) {
      try {
        desktop.browse(new URI(url));
      } catch (final IOException ex) {
        GLog.error(GenomeLogCategory.System, "Can't launch browser: " + ex.getMessage(), new LogExceptionAttribute(ex));
      } catch (final URISyntaxException ex) {
        GLog.error(GenomeLogCategory.System, "Can't launch browser: " + ex.getMessage(), new LogExceptionAttribute(ex));
      }
    }
  }
}