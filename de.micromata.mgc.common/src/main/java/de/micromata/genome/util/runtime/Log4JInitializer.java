package de.micromata.genome.util.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.FileWatchdog;

/**
 * Initializes with LocalSettings the log4j.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class Log4JInitializer
{
  private static Logger LOG = Logger.getLogger(Log4JInitializer.class);

  public static final String LOG4J_PROPERTY_FILE = "log4j.properties";
  public static long log4jMonitorTimeInMs = 60000;
  public static long log4jDevMonitorTimeInMs = 10000;

  private static boolean log4jInitialized = false;

  public static void reinit()
  {
    log4jInitialized = false;
    initializeLog4J();
  }

  /**
   * If not already intialized log4j, try to read log4j-dev.properties, log4j.properties or log43
   * 
   * @return
   */
  public static boolean initializeLog4J()
  {
    if (log4jInitialized == true) {
      return true;
    }
    log4jInitialized = initializeLog4JIntern();
    return log4jInitialized;
  }

  /**
   * Checks, if Log4J is already initialized.
   *
   * @return true, if is log4 j initialized
   */
  public static boolean isLog4JInitialized()
  {
    return log4jInitialized;
  }

  /**
   * Copies the log4j.properties load from cp into file defined in localsettings if not exists
   */
  public static void copyLogConfigFileFromCp()
  {
    LocalSettings ls = LocalSettings.get();
    File log4jfile = new File(ls.get(LOG4J_PROPERTY_FILE, LOG4J_PROPERTY_FILE));
    if (log4jfile.exists() == true) {
      return;
    }
    try (InputStream is = Log4JInitializer.class.getClassLoader().getResourceAsStream(LOG4J_PROPERTY_FILE)) {
      if (is != null) {
        try (OutputStream os = new FileOutputStream(log4jfile)) {
          IOUtils.copy(is, os);
        }
      }
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }

  }

  private static boolean initializeLog4JIntern()
  {
    LocalSettings ls = LocalSettings.get();
    File log4jfile = new File(ls.get(LOG4J_PROPERTY_FILE, LOG4J_PROPERTY_FILE));
    if (log4jfile.exists() == true && log4jfile.canRead() == true) {
      return initViaFile(log4jfile);
    }

    return initViaCp();

  }

  public static class PropertyWatchdog extends FileWatchdog
  {

    public PropertyWatchdog(String filename, long delay)
    {
      super(filename);
      setDelay(delay);
    }

    /**
     * Call {@link PropertyConfigurator#configure(String)} with the <code>filename</code> to reconfigure log4j.
     */
    @Override
    public void doOnChange()
    {
      new PropertyConfigurator().doConfigure(filename,
          LogManager.getLoggerRepository());
    }
  }

  static PropertyWatchdog propWatchDoc = null;
  static PropertyWatchdog propDevWatchDoc = null;

  private static boolean initViaFile(File log4jfile)
  {
    File devFile = findDevFile(log4jfile);

    Properties props = new Properties();
    try {
      try (InputStream is = new FileInputStream(log4jfile)) {
        props.load(is);
      }

      if (devFile != null) {
        try (InputStream is = new FileInputStream(devFile)) {
          props.load(is);
        }
      }
    } catch (IOException ex) {
      LOG.error("Cannot read log4jfiles: " + ex.getMessage(), ex);
      return false;
    }
    new PropertyConfigurator().doConfigure(props,
        LogManager.getLoggerRepository());

    if ((propWatchDoc == null || propWatchDoc.isAlive() == false)) {
      propWatchDoc = new PropertyWatchdog(log4jfile.getAbsolutePath(), log4jMonitorTimeInMs);
      propWatchDoc.start();
    }
    if (devFile != null && (propDevWatchDoc == null || propDevWatchDoc.isAlive() == false)) {
      propDevWatchDoc = new PropertyWatchdog(log4jfile.getAbsolutePath(), log4jMonitorTimeInMs);
      propDevWatchDoc.start();
    }
    return true;
  }

  /**
   * Try to find a log4j-dev.properies file.
   * 
   * @param log4jfile
   * @return
   */
  private static File findDevFile(File log4jfile)
  {
    File dir = log4jfile.getParentFile();
    String name = log4jfile.getName();
    int lidx = name.lastIndexOf('.');
    if (lidx == -1) {
      return null;
    }
    String nname = name.substring(0, lidx) + "-dev" + name.substring(lidx);
    File nf = new File(dir, nname);
    if (nf.exists() == true && nf.canRead() == true) {
      return nf;
    }
    return null;
  }

  private static boolean initViaCp()
  {
    final ClassLoader cLoader = Log4JInitializer.class.getClassLoader();
    final InputStream is = cLoader.getResourceAsStream(LOG4J_PROPERTY_FILE);
    if (is == null) {
      return false;
    }
    PropertyConfigurator.configure(is);
    return true;
  }
}
