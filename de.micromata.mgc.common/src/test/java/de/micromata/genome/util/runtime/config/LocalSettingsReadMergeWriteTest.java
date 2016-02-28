package de.micromata.genome.util.runtime.config;

import java.io.File;

import org.junit.Test;

import de.micromata.genome.util.runtime.LocalSettings;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LocalSettingsReadMergeWriteTest
{
  @Test
  public void testIt()
  {
    ExtLocalSettingsLoader loader = new ExtLocalSettingsLoader();
    loader.setLocalSettingsFileName("./dev/extrc/test/properties/ls_with_comments1.properties");
    LocalSettings localSettings = loader.loadSettings();

    MergingLocalSettingsWriter lswout = new MergingLocalSettingsWriter(loader.origProps);
    LocalSettingsWriter sec = lswout.newSection("Coded");
    sec.put("a", "c");
    sec.put("c", "3", "New Comment");
    lswout.store(new File("./target/merged.properties"));
    // check manual written file.

  }
}
