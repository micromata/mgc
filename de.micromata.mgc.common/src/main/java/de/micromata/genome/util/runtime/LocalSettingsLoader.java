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

  default File getLocalSettingsFile()
  {
    return new File(getLocalSettingsFileName());
  }

}
