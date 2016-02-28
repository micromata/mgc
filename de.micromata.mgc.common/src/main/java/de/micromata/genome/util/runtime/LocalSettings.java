package de.micromata.genome.util.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

import org.apache.commons.lang.StringUtils;

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
public class LocalSettings implements LocalSettingsService
{

  public static String localSettingsPrefixName = "local-settings";
  /**
   * Factory to load LocalSettings
   */
  public static Supplier<LocalSettingsLoader> localSettingsLoaderFactory = () -> new StdLocalSettingsLoader();

  /**
   * The instance.
   */
  static LocalSettings INSTANCE;

  /**
   * Gets the.
   * 
   * @return the local settings
   * @deprecated use LocalSettingsService.get()
   */

  @Deprecated
  static public LocalSettings get()
  {
    return getImpl();
  }

  static protected LocalSettings getImpl()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = localSettingsLoaderFactory.get().loadSettings();
    return INSTANCE;
  }

  protected static void resetImpl()
  {
    INSTANCE = null;
  }

  /**
   * Check if local settings file exists.;
   * 
   * @return
   */
  public static boolean localSettingsExists()
  {
    return localSettingsLoaderFactory.get().localSettingsExists();
  }

  protected LocalSettingsLoader localSettingsLoader;

  /**
   * The map.
   */
  private Map<String, String> map = new HashMap<String, String>();

  /**
   * Default values for test envirments.
   */
  private static Map<String, String> defaultValues = new HashMap<String, String>();

  public LocalSettings(LocalSettingsLoader localSettingsLoader)
  {
    this.localSettingsLoader = localSettingsLoader;
  }

  @Override
  public LocalSettingsLoader getLocalSettingsLoader()
  {
    return localSettingsLoader;
  }

  // TODO RK do somewhere
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
   * Gets the.
   * 
   * @param key the key
   * @return null if not in map
   */
  @Override
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
  @Override
  public String get(String key, String defaultValue)
  {
    if (containsKey(key) == false) {
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
  @Override
  public int getIntValue(String key, int defaultValue)
  {
    if (containsKey(key) == false) {
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
  @Override
  public long getLongValue(String key, long defaultValue)
  {
    if (containsKey(key) == false) {
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
  @Override
  public boolean getBooleanValue(String key, boolean defaultValue)
  {
    if (containsKey(key) == false) {
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
  @Override
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
  @Override
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
  @Override
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
  @Override
  public String resolve(String value)
  {
    if (value == null) {
      return null;
    }
    return PlaceHolderReplacer.resolveRecursiveReplaceDollarVars(value, map);
  }

  /**
   * Dump into map.
   *
   * @param map the map
   */
  @Override
  public void copyInto(Map<String, String> map)
  {
    for (String key : getMap().keySet()) {
      map.put(key, get(key));
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

  @Override
  public Map<String, String> getMap()
  {
    return map;
  }

  public void setMap(Map<String, String> map)
  {
    this.map = map;
  }

  @Deprecated
  public String getGenomeHome()
  {
    return get("genome.home");
  }

  @Deprecated
  public String getApplicationEnvironment()
  {
    return get("env.ApplicationEnvironment");
  }

  @Deprecated
  public String getApplicationDevelopmentModus()
  {
    return get("env.ApplicationDevelopmentModus");
  }

  @Deprecated
  public String getShortApplicationName()
  {
    return get("env.ShortApplicationName");
  }

  @Deprecated
  public String getDatabaseProvider()
  {
    return get("database.databaseProvider");
  }

  @Deprecated
  public String getPublicUrl()
  {
    return get("cfg.public.url");

  }

  @Deprecated
  public String getTestApplicationContextXml()
  {
    return get("test.TestApplicationContextXml");
  }

  @Deprecated
  public String getTestLog4JProperties()
  {
    return get("test.log4jproperties");
  }

}
