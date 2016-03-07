package de.micromata.genome.util.runtime.config;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.util.runtime.InitWithCopyFromCpLocalSettingsClassLoader;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.LocalSettingsLoader;
import de.micromata.genome.util.runtime.Log4JInitializer;
import de.micromata.genome.util.runtime.StdLocalSettingsLoader;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class InitWithCopyFromCpLocalSettingsClassLoaderTest
{
  private static final Logger LOG = Logger.getLogger(InitWithCopyFromCpLocalSettingsClassLoaderTest.class);

  @Test
  public void testCopy()
  {
    Log4JInitializer.copyLogConfigFileFromCp();
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

    if (file.exists() == false) {
      LOG.warn("File doesn't exists: " + file.getAbsolutePath());
    }
    Assert.assertEquals("hello", ls.get("test_fromcp"));
  }
}
