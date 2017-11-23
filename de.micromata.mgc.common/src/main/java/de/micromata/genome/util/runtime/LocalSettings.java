//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.util.runtime;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

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

  private static final Logger LOG = Logger.getLogger(LocalSettings.class);

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
   */

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

  static public LocalSettings resetAndGet()
  {
    resetImpl();
    return getImpl();
  }

  public static void resetImpl()
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

  private Map<String, String> fromFile = new HashMap<>();

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
   * @return rest of key to value
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

  /**
   * Gets only LocalSettings from LocalSettings.properties-File, without the System's environment
   * 
   * @return
   */
  public Map<String, String> getFromFile()
  {
    return fromFile;
  }

  public void setFromFile(Map<String, String> fromFile)
  {
    this.fromFile = fromFile;
  }

  public List<String> getLoadedFilesPath()
  {
    ArrayList<String> list = new ArrayList<>();
    for (File loadedFile : localSettingsLoader.getLoadedFiles()) {
      list.add(loadedFile.getAbsolutePath());
    }
    return list;
  }

  /**
   * Call this after loading local settings and initialized logging.
   */
  public void logloadedFiles()
  {
    for (String warn : localSettingsLoader.getWarns()) {
      LOG.warn(warn);
    }
    if (LOG.isInfoEnabled() == false) {
      return;
    }
    StringBuilder sb = new StringBuilder();
    sb.append("Loaded localsettings: ");
    for (File lf : localSettingsLoader.getLoadedFiles()) {
      sb.append(lf.getAbsolutePath()).append(", ");
    }
    LOG.info(sb.toString());

  }

  public String getGenomeHome()
  {
    return get("genome.home");
  }

  public String getPublicUrl()
  {
    return get("cfg.public.url");

  }
}
