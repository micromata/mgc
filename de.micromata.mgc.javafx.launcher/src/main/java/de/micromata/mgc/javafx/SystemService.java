package de.micromata.mgc.javafx;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;

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

  public List<JdbcDriverDescription> getJdbcDrivers()
  {
    List<JdbcDriverDescription> ret = new ArrayList<>();
    for (JdbcDriverDescription r : StandardJdbcDriverDescriptions.values()) {
      String classname = r.getDriverClassName();
      if (classExists(classname) == true) {
        ret.add(r);
      }

    }
    return ret;
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