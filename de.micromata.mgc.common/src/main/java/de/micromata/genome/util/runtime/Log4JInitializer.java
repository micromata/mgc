package de.micromata.genome.util.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Initializes with LocalSettings the log4j.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class Log4JInitializer
{
  private static Logger LOG = Logger.getLogger(Log4JInitializer.class);
  private static final String LOG4J_PROPERTY_FILE = "log4j.properties";

  private static boolean log4jInitialized = false;

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

  private static boolean initializeLog4JIntern()
  {
    LocalSettings ls = LocalSettings.get();
    File log4jfile = new File(ls.get(LOG4J_PROPERTY_FILE, LOG4J_PROPERTY_FILE));
    if (log4jfile.exists() == true && log4jfile.canRead() == true) {
      return initViaFile(log4jfile);
    }

    return initViaCp();

  }

  private static boolean initViaFile(File log4jfile)
  {
    Properties props = new Properties();
    try {
      try (InputStream is = new FileInputStream(log4jfile)) {
        props.load(is);
      }
      File devFile = findDevFile(log4jfile);
      if (devFile != null) {
        try (InputStream is = new FileInputStream(devFile)) {
          props.load(is);
        }
      }
    } catch (IOException ex) {
      LOG.error("Cannot read log4jfiles: " + ex.getMessage(), ex);
      return false;
    }
    PropertyConfigurator.configure(props);
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
