package de.micromata.genome.util.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.micromata.genome.util.collections.OrderedProperties;

/**
 * Standard implementation to load local-settings.properties.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class StdLocalSettingsLoader implements LocalSettingsLoader
{
  /**
   * Factory method to create and initialize local settings.
   */
  public static Function<LocalSettingsLoader, LocalSettings> defaultLocalSettingsFactory = (
      loader) -> new LocalSettings(loader);
  /**
   * The Constant log.
   */
  private static final Logger LOG = Logger.getLogger(LocalSettings.class);

  private List<String> warns = new ArrayList<>();
  private List<File> loadedFiles = new ArrayList<>();

  protected String localSettingsPrefixName = "local-settings";
  protected String localSettingsFile;
  Function<LocalSettingsLoader, LocalSettings> localSettingsFactory;

  public StdLocalSettingsLoader()
  {

  }

  public StdLocalSettingsLoader(String localSettingsFile,
      Function<LocalSettingsLoader, LocalSettings> localSettingsFactory, String prefix)
  {
    this.localSettingsFile = localSettingsFile;
    this.localSettingsFactory = localSettingsFactory;
    if (prefix != null) {
      this.localSettingsPrefixName = prefix;
    }
  }

  @Override
  public String getLocalSettingsFile()
  {
    if (StringUtils.isEmpty(localSettingsFile) == false) {
      return localSettingsFile;
    }
    localSettingsFile = System.getProperty("localsettings");
    if (StringUtils.isEmpty(localSettingsFile) == true) {
      localSettingsFile = localSettingsPrefixName + ".properties";
    }
    return localSettingsFile;
  }

  @Override
  public boolean localSettingsExists()
  {
    String localSettingsFile = System.getProperty("localsettings");
    if (StringUtils.isEmpty(localSettingsFile) == true) {
      localSettingsFile = localSettingsPrefixName + ".properties";
    }
    return new File(localSettingsFile).exists();
  }

  @Override
  public LocalSettings loadSettings()
  {
    LocalSettings ls = newLocalSettings();
    getLocalSettingsFile();
    loadSettingsImpl(ls);
    return ls;
  }

  protected LocalSettings newLocalSettings()
  {
    if (localSettingsFactory != null) {
      return localSettingsFactory.apply(this);
    }
    return defaultLocalSettingsFactory.apply(this);
  }

  public void loadSettingsImpl(LocalSettings ls)
  {
    //log.info("Loading localSettingsfile: " + new File(localSettingsFile).getAbsolutePath());
    loadSettings(ls, localSettingsFile, true, true);
    loadOptionalDev(ls);
    loadSystemEnv(ls);
    loadSystemProperties(ls);
  }

  protected void loadOptionalDev(LocalSettings ls)
  {
    loadSettings(ls, localSettingsPrefixName + "-dev.properties", false, false);
  }

  protected void loadSystemEnv(LocalSettings ls)
  {
    Map<String, String> envmap = System.getenv();

    for (Map.Entry<String, String> me : envmap.entrySet()) {
      ls.getMap().put(me.getKey(), me.getValue());
    }
  }

  protected void loadSystemProperties(LocalSettings ls)
  {
    Properties props = System.getProperties();
    for (Object k : props.keySet()) {
      String key = (String) k;
      ls.getMap().put(key, props.getProperty(key));
    }
  }

  @Override
  public boolean loadSettings(LocalSettings ls, String localSettingsFile, boolean originalLocalSettingsFile,
      boolean warn)
  {
    File f = new File(localSettingsFile);
    if (f.exists() == false) {
      if (warn == true) {
        warns.add("Cannot find localsettings file: " + f.getAbsolutePath());
      }
      return false;
    }
    if (LOG.isDebugEnabled() == true) {
      LOG.debug("Load localsettings: " + f.getAbsolutePath());
    }
    FileInputStream fin = null;
    try {
      OrderedProperties props = newProperties(originalLocalSettingsFile);
      fin = new FileInputStream(f);
      props.load(fin, new LocalSettingsIncludeReplacer(ls, f.getAbsoluteFile().getParentFile()));
      for (String k : props.keySet()) {
        ls.getMap().put(k, props.get(k));
      }
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    } finally {
      IOUtils.closeQuietly(fin);
    }
    loadedFiles.add(f);
    return true;
  }

  protected OrderedProperties newProperties(boolean originalLocalSettingsFile)
  {
    return new OrderedProperties();
  }

  @Override
  public List<String> getWarns()
  {
    return warns;
  }

  @Override
  public List<File> getLoadedFiles()
  {
    return loadedFiles;
  }

  public String getLocalSettingsPrefixName()
  {
    return localSettingsPrefixName;
  }

}
