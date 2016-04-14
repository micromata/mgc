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
   * @return
   */
  LocalSettings loadSettings();

  /**
   * Used internally to load nested localsettings
   * 
   * @param ls
   * @param localSettingsFile
   * @param originalLocalSettingsFile
   * @param warn
   * @return
   */
  boolean loadSettings(LocalSettings ls, File localSettingsFile, Map<String, String> target,
      boolean originalLocalSettingsFile,
      boolean warn);

  /**
   * check if localsettings file exists.
   * 
   * @return
   */
  boolean localSettingsExists();

  /**
   * 
   * @return
   */
  String getLocalSettingsFileName();

  /**
   * After loadSettings() filled with warnings
   * 
   * @return
   */
  List<String> getWarns();

  /**
   * After loadSettings() a list of files loaded.
   * 
   * @return
   */
  List<File> getLoadedFiles();

  /**
   * Must return a valid directory used to find local settings files.
   * 
   * @return
   */
  File getWorkingDirectory();

  /**
   * Get local settings file.
   * 
   * @return
   */
  File getLocalSettingsFile();

}
