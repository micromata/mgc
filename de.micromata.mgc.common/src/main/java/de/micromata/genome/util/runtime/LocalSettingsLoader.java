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
import java.util.List;
import java.util.Map;

/**
 * A loader for loading one instance of a LocalSettings.
 * 
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface LocalSettingsLoader
{
  /**
   * loads a new local settings.
   * 
   * @return the new {@link LocalSettings}
   */
  LocalSettings loadSettings();

  /**
   * Used internally to load nested localsettings
   * 
   * @param ls the local settings
   * @param localSettingsFile the file of the local settings
   * @param originalLocalSettingsFile the original local settings
   * @param warn warn when something went wrong
   * @return true when the settings could be loaded
   */
  boolean loadSettings(LocalSettings ls, File localSettingsFile, Map<String, String> target,
      boolean originalLocalSettingsFile,
      boolean warn);

  /**
   * check if localsettings file exists.
   * 
   * @return true when the localsettings exists
   */
  boolean localSettingsExists();

  /**
   * 
   * @return the name of the local settings
   */
  String getLocalSettingsFileName();

  /**
   * After loadSettings() filled with warnings
   * 
   * @return a {@link List} of warnings
   */
  List<String> getWarns();

  /**
   * After loadSettings() a list of files loaded.
   * 
   * @return a {@link List} of loaded files
   */
  List<File> getLoadedFiles();

  /**
   * Must return a valid directory used to find local settings files.
   * 
   * @return the current working directory
   */
  File getWorkingDirectory();

  /**
   * Get local settings file.
   * 
   * @return the file of the localsettings
   */
  File getLocalSettingsFile();

}
