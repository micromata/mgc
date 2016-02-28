package de.micromata.genome.util.runtime.config;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.StdLocalSettingsLoader;

/**
 * Tests of LocalSettings.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LocalSettingsBaseTest
{
  @Test
  public void testLoadIncluded()
  {
    StdLocalSettingsLoader loader = new StdLocalSettingsLoader();
    loader.setWorkingDirectory(new File("dev/extrc/test/properties"));
    LocalSettings ls = loader.loadSettings();
    Assert.assertEquals("valuex", ls.get("key1"));
    Assert.assertEquals("outvalue", ls.get("keyininclude"));
    Assert.assertEquals("IsOverwritten", ls.get("overwrite1"));

    Assert.assertEquals("notoverwritten", ls.get("overwrite2"));

  }

  @Test
  public void testLoadOtherBaseName()
  {
    StdLocalSettingsLoader loader = new StdLocalSettingsLoader();
    loader.setLocalSettingsPrefixName("only_for_test");
    LocalSettings ls = loader.loadSettings();
    Assert.assertEquals("testvalue2", ls.get("test.entry.two"));
    Assert.assertEquals("testvaluedev", ls.get("test.entry.one"));

  }
}
