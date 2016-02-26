package de.micromata.genome.util.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.micromata.genome.util.collections.OrderedProperties;
import de.micromata.genome.util.text.PlaceHolderReplacer;
import de.micromata.genome.util.types.Pair;

/**
 * Wrapper for local-settings.properties
 * 
 * Simply use LocalSettings.get() to read it.
 * 
 * @author roger
 * 
 */
public class LocalSettings
{

  /**
   * The Constant log.
   */
  private static final Logger log = Logger.getLogger(LocalSettings.class);

  public static String localSettingsPrefixName = "local-settings";

  /**
   * The instance.
   */
  static LocalSettings INSTANCE;

  /**
   * Gets the.
   * 
   * @return the local settings
   */
  static public LocalSettings get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new LocalSettings();
    INSTANCE.init();
    return INSTANCE;
  }

  public static void reset()
  {
    INSTANCE = null;
  }

  /**
   * The local settings file.
   */
  private String localSettingsFile;

  private List<File> loadedFiles = new ArrayList<>();

  private List<String> warns = new ArrayList<>();
  /**
   * The map.
   */
  private Map<String, String> map = new HashMap<String, String>();

  /**
   * Default values for test envirments.
   */
  private static Map<String, String> defaultValues = new HashMap<String, String>();

  static {
    defaultValues.put("env.ApplicationEnvironment", "SERVER");
    defaultValues.put("env.ApplicationDevelopmentModus", "DEV");
    defaultValues.put("env.ShortApplicationName", "GNM");
    defaultValues.put("database.databaseProvider", "NONE");
    defaultValues.put("cfg.public.url", "http://localhost:8080/genome");
    defaultValues.put("test.TestApplicationContextXml", "src/test/resources/testApplicationContext.xml");
    defaultValues.put("test.log4jproperties", "src/test/resources/log4j.properties");
    defaultValues.put("test.email", "devnull@micromata.de");
    defaultValues.put("ProjectRoot", "${cfg.projectroot.path}");
  }

  /**
   * Instantiates a new local settings.
   */
  public LocalSettings()
  {

  }

  /**
   * alias to get().
   * 
   * @param key the key
   * @return the property
   */
  public String getProperty(String key)
  {
    return get(key);
  }

  /**
   * Alias to get(...)
   * 
   * @param key the key
   * @param defaultValue the default value
   * @return the property
   */
  public String getProperty(String key, String defaultValue)
  {
    return get(key, defaultValue);
  }

  /**
   * Gets the.
   * 
   * @param key the key
   * @return null if not in map
   */
  public String get(String key)
  {
    String val = map.get(key);
    if (val == null) {
      val = defaultValues.get(key);
    }
    if (val == null) {
      return null;
    }
    return resolve(val);
  }

  /**
   * Gets the.
   * 
   * @param key the key
   * @param defaultValue the default value
   * @return the string
   */
  public String get(String key, String defaultValue)
  {
    if (map.containsKey(key) == false) {
      return defaultValue;
    }
    return get(key);
  }

  /**
   * Gets the int value.
   * 
   * @param key the key
   * @param defaultValue the default value
   * @return the int value
   */
  public int getIntValue(String key, int defaultValue)
  {
    if (map.containsKey(key) == false) {
      return defaultValue;
    }
    String v = get(key);
    return Integer.parseInt(v);
  }

  /**
   * Gets the Long value.
   * 
   * @param key the key
   * @param defaultValue the default value
   * @return the long value
   */
  public long getLongValue(String key, long defaultValue)
  {
    if (map.containsKey(key) == false) {
      return defaultValue;
    }
    String v = get(key);
    return Long.parseLong(v);
  }

  /**
   * Gets the boolean value.
   * 
   * @param key the key
   * @param defaultValue the default value
   * @return the boolean value
   */
  public boolean getBooleanValue(String key, boolean defaultValue)
  {
    if (map.containsKey(key) == false) {
      return defaultValue;
    }
    String v = get(key);
    return StringUtils.equalsIgnoreCase(v, "true");
  }

  /**
   * Contains key.
   * 
   * @param key the key
   * @return true, if successful
   */
  public boolean containsKey(String key)
  {
    return map.containsKey(key);
  }

  /**
   * Gets the entries with prefix.
   * 
   * @param prefix the prefix
   * @return rest of key -> value
   */
  public List<Pair<String, String>> getEntriesWithPrefix(String prefix)
  {
    List<Pair<String, String>> ret = new ArrayList<Pair<String, String>>();
    for (String k : map.keySet()) {
      if (k.startsWith(prefix) == true) {
        ret.add(Pair.make(k.substring(prefix.length()), resolve(map.get(k))));
      }
    }
    return ret;
  }

  /**
   * Gets the keys prefix with infix.
   * 
   * @param start a.b
   * @param end c
   * @return finds a.b.x when a.b.x.c exists
   */
  public List<String> getKeysPrefixWithInfix(String start, String end)
  {
    List<String> ret = new ArrayList<String>();
    String starts = start + ".";
    String ends = "." + end;
    for (String k : map.keySet()) {
      if (k.startsWith(starts) == true && k.endsWith(ends) == true) {
        ret.add(k.substring(0, k.length() - ends.length()));
      }
    }
    return ret;
  }

  /**
   * Resolve.
   * 
   * @param value the value
   * @return the string
   */
  public String resolve(String value)
  {
    if (value == null) {
      return null;
    }
    return PlaceHolderReplacer.resolveRecursiveReplaceDollarVars(value, map);
  }

  public String getGenomeHome()
  {
    return get("genome.home");
  }

  public String getApplicationEnvironment()
  {
    return get("env.ApplicationEnvironment");
  }

  public String getApplicationDevelopmentModus()
  {
    return get("env.ApplicationDevelopmentModus");
  }

  public String getShortApplicationName()
  {
    return get("env.ShortApplicationName");
  }

  public String getDatabaseProvider()
  {
    return get("database.databaseProvider");
  }

  public String getPublicUrl()
  {
    return get("cfg.public.url");

  }

  public String getTestApplicationContextXml()
  {
    return get("test.TestApplicationContextXml");
  }

  public String getTestLog4JProperties()
  {
    return get("test.log4jproperties");
  }

  /**
   * Inits the.
   */
  public void init()
  {
    localSettingsFile = System.getProperty("localsettings");
    if (StringUtils.isEmpty(localSettingsFile) == true) {
      localSettingsFile = localSettingsPrefixName + ".properties";
    }
    loadSettings();
  }

  /**
   * Load settings.
   */
  public void loadSettings()
  {
    //log.info("Loading localSettingsfile: " + new File(localSettingsFile).getAbsolutePath());
    loadSettings(localSettingsFile, true);

    loadSettings(localSettingsPrefixName + "-dev.properties", false);
    Map<String, String> envmap = System.getenv();

    for (Map.Entry<String, String> me : envmap.entrySet()) {
      map.put(me.getKey(), me.getValue());
    }
    Properties props = System.getProperties();
    for (Object k : props.keySet()) {
      String key = (String) k;
      map.put(key, props.getProperty(key));
      //      if (log.isDebugEnabled() == true) {
      //        log.debug("LC set from env: " + key + "=" + props.getProperty(key));
      //      }
    }
  }

  /**
   * Check if local settings file exists.;
   * 
   * @return
   */
  public static boolean localSettingsExists()
  {
    String localSettingsFile = System.getProperty("localsettings");
    if (StringUtils.isEmpty(localSettingsFile) == true) {
      localSettingsFile = localSettingsPrefixName + ".properties";
    }
    return new File(localSettingsFile).exists();
  }

  /**
   * Load settings.
   *
   * @param localSettingsFile the local settings file
   * @param warn the warn
   * @return true, if successful
   */
  protected boolean loadSettings(String localSettingsFile, boolean warn)
  {
    File f = new File(localSettingsFile);
    if (f.exists() == false) {
      if (warn == true) {
        warns.add("Cannot find localsettings file: " + f.getAbsolutePath());
      }
      return false;
    }
    if (log.isDebugEnabled() == true) {
      log.debug("Load localsettings: " + f.getAbsolutePath());
    }
    FileInputStream fin = null;
    try {
      OrderedProperties props = new OrderedProperties();
      fin = new FileInputStream(f);
      props.load(fin, new IncludeReplacer(f.getAbsoluteFile().getParentFile()));
      for (String k : props.keySet()) {
        map.put(k, props.get(k));
      }
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    } finally {
      IOUtils.closeQuietly(fin);
    }
    loadedFiles.add(f);
    return true;
  }

  /**
   * Dump into map.
   *
   * @param map the map
   */
  public void dumpIntoMap(Map<String, String> map)
  {
    for (String key : getMap().keySet()) {
      map.put(key, get(key));
    }
  }

  /**
   * The Class IncludeReplacer.
   */
  private class IncludeReplacer implements OrderedProperties.KeyValueReplacer
  {

    /**
     * The load dir.
     */
    File loadDir;

    /**
     * Instantiates a new include replacer.
     * 
     * @param loadDir the load dir
     */
    public IncludeReplacer(File loadDir)
    {
      this.loadDir = loadDir;
    }

    /**
     * {@inheritDoc}
     * 
     */

    @Override
    public Pair<String, String> replace(Pair<String, String> keyValue, OrderedProperties properties)
    {
      if (keyValue.getKey().equals("include") == true) {
        File tf = new File(loadDir, keyValue.getSecond());
        loadSettings(tf.getAbsolutePath(), true);
        return null;
      }
      if (keyValue.getValue().contains("${LOCALSETTINGSDIR}") == true) {
        String value = StringUtils.replace(keyValue.getValue(), "${LOCALSETTINGSDIR}", loadDir.getAbsolutePath());
        keyValue.setValue(value);
      }
      return keyValue;
    }

  }

  /**
   * Creates a copy of all settings and return the copy as Properties object.
   * 
   * @return the settings as properties
   */
  public Properties getSettingsAsProperties()
  {
    Properties ret = new Properties();
    for (Map.Entry<String, String> me : map.entrySet()) {
      ret.put(me.getKey(), me.getValue());
    }
    return ret;
  }

  /**
   * Call this after loading local settings and initialized logging.
   */
  public void logloadedFiles()
  {
    for (String warn : warns) {
      log.warn(warn);
    }
    if (log.isInfoEnabled() == false) {
      return;
    }
    StringBuilder sb = new StringBuilder();
    sb.append("Loaded localsettings: ");
    for (File lf : loadedFiles) {
      sb.append(lf.getAbsolutePath()).append(", ");
    }
    log.info(sb.toString());

  }

  public String getLocalSettingsFile()
  {
    return localSettingsFile;
  }

  public void setLocalSettingsFile(String localSettingsFile)
  {
    this.localSettingsFile = localSettingsFile;
  }

  public Map<String, String> getMap()
  {
    return map;
  }

  public void setMap(Map<String, String> map)
  {
    this.map = map;
  }

}
