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

import de.micromata.genome.util.collections.OrderedProperties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

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
  protected String localSettingsFileName;

  protected File workingDirectory = new File(".");

  Function<LocalSettingsLoader, LocalSettings> localSettingsFactory;

  public StdLocalSettingsLoader()
  {

  }

  public StdLocalSettingsLoader(String localSettingsFileName,
      Function<LocalSettingsLoader, LocalSettings> localSettingsFactory, String prefix)
  {
    this.localSettingsFileName = localSettingsFileName;
    this.localSettingsFactory = localSettingsFactory;
    if (prefix != null) {
      this.localSettingsPrefixName = prefix;
    }
  }

  @Override
  public File getLocalSettingsFile()
  {
    String fileName = getLocalSettingsFileName();
    if (fileName.contains("/") == true || fileName.contains("\\") == true) {
      return new File(fileName);
    }
    return new File(getWorkingDirectory(), fileName);
  }

  @Override
  public String getLocalSettingsFileName()
  {
    if (StringUtils.isEmpty(localSettingsFileName) == false) {
      return localSettingsFileName;
    }
    localSettingsFileName = System.getProperty("localsettings");
    if (StringUtils.isEmpty(localSettingsFileName) == true) {
      localSettingsFileName = getLocalSettingsPrefixName() + ".properties";
    }
    return localSettingsFileName;
  }

  @Override
  public boolean localSettingsExists()
  {
    File lsFile = getLocalSettingsFile();
    return lsFile.exists();
  }

  @Override
  public LocalSettings loadSettings()
  {
    LocalSettings ls = newLocalSettings();
    getLocalSettingsFileName();
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
    HashMap<String, String> map = new HashMap<>();
    loadSettings(ls, getLocalSettingsFile(), map, true, true);
    ls.getMap().putAll(map);
    ls.getFromFile().putAll(map);
    loadOptionalDev(ls);
    loadSystemEnv(ls);
    loadSystemProperties(ls);
  }

  protected void loadOptionalDev(LocalSettings ls)
  {
    HashMap<String, String> map = new HashMap<>();
    loadSettings(ls, new File(getLocalSettingsFile().getParentFile(), localSettingsPrefixName + "-dev.properties"),
        map, false, false);
    ls.getMap().putAll(map);
    ls.getFromFile().putAll(map);
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
  public boolean loadSettings(LocalSettings ls, File localSettingsFile, Map<String, String> target,
      boolean originalLocalSettingsFile,
      boolean warn)
  {
    if (localSettingsFile.exists() == false) {
      if (warn == true) {
        warns.add("Cannot find localsettings file: " + localSettingsFile.getAbsolutePath());
      }
      return false;
    }
    if (LOG.isDebugEnabled() == true) {
      LOG.debug("Load localsettings: " + localSettingsFile.getAbsolutePath());
    }
    FileInputStream fin = null;
    try {
      OrderedProperties props = newProperties(originalLocalSettingsFile);
      fin = new FileInputStream(localSettingsFile);
      props.load(fin, new LocalSettingsIncludeReplacer(ls, localSettingsFile.getAbsoluteFile().getParentFile()));
      for (String k : props.keySet()) {
        target.put(k, props.get(k));
      }
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    } finally {
      IOUtils.closeQuietly(fin);
    }
    loadedFiles.add(localSettingsFile);
    return true;
  }

  protected OrderedProperties newProperties(boolean originalLocalSettingsFile)
  {
    return new OrderedProperties();
  }

  @Override
  public File getWorkingDirectory()
  {
    return workingDirectory;
  }

  public void setWorkingDirectory(File woringDirectory)
  {
    this.workingDirectory = woringDirectory;
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

  public void setLocalSettingsPrefixName(String localSettingsPrefixName)
  {
    this.localSettingsPrefixName = localSettingsPrefixName;
  }

  public void setLocalSettingsFileName(String localSettingsFileName)
  {
    this.localSettingsFileName = localSettingsFileName;
  }

}
