package de.micromata.genome.util.runtime.config;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.util.runtime.InitWithCopyFromCpLocalSettingsClassLoader;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.LocalSettingsLoader;
import de.micromata.genome.util.runtime.StdLocalSettingsLoader;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class InitWithCopyFromCpLocalSettingsClassLoaderTest
{
  @Test
  public void testCopy()
  {
    File file = new File("target/test_fromcp.properties");
    if (file.exists() == true) {
      file.delete();
    }
    InitWithCopyFromCpLocalSettingsClassLoader ncp = new InitWithCopyFromCpLocalSettingsClassLoader(
        () -> {
          StdLocalSettingsLoader ret = new StdLocalSettingsLoader();
          ret.setWorkingDirectory(new File("target"));
          ret.setLocalSettingsPrefixName("test_fromcp");
          return ret;
        });
    LocalSettingsLoader loader = ncp.get();
    LocalSettings ls = loader.loadSettings();
    Assert.assertTrue(new File("target/test_fromcp.properties").exists());
    Assert.assertEquals("hello", ls.get("test_fromcp"));
  }
}
