package de.micromata.genome.util.runtime;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.collections.OrderedProperties;
import de.micromata.genome.util.types.Pair;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LocalSettingsIncludeReplacer implements OrderedProperties.KeyValueReplacer
{

  /**
   *
  */

  private final LocalSettings localSettings;
  /**
   * The load dir.
   */
  File loadDir;

  /**
   * Instantiates a new include replacer.
   * 
   * @param loadDir the load dir
   * @param localSettings TODO
   */
  public LocalSettingsIncludeReplacer(LocalSettings localSettings, File loadDir)
  {
    this.localSettings = localSettings;
    this.loadDir = loadDir;
  }

  /**
   * {@inheritDoc}
   * 
   */

  @Override
  public Pair<String, String> replace(Pair<String, String> keyValue, Map<String, String> target)
  {
    if (keyValue.getKey().equals("include") == true) {
      File tf = new File(loadDir, keyValue.getSecond());
      this.localSettings.getLocalSettingsLoader().loadSettings(this.localSettings, tf, target, false, true);
      return null;
    }
    if (keyValue.getValue().contains("${LOCALSETTINGSDIR}") == true) {
      String value = StringUtils.replace(keyValue.getValue(), "${LOCALSETTINGSDIR}", loadDir.getAbsolutePath());
      keyValue.setValue(value);
    }
    return keyValue;
  }

}