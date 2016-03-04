package de.micromata.genome.logging.config;

import org.junit.Test;

import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.StdLocalSettingsLoader;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LsLoggingConfigTest
{
  @Test
  public void testLoadDefault()
  {
    LsLoggingImpl lsimpl = new LsLoggingImpl();
    lsimpl.info(GenomeLogCategory.UnitTest, "Hello");
  }

  //  @Test
  // TODO RK fix this
  public void testExliciteLog4J()
  {
    StdLocalSettingsLoader loader = new StdLocalSettingsLoader()
    {

      @Override
      public void loadSettingsImpl(LocalSettings ls)
      {
        ls.getMap().put("genome.logging.typeId", "log4J");

      }

    };
    LocalSettings lls = loader.loadSettings();
    LsLoggingImpl lsimpl = new LsLoggingImpl(lls);
    lsimpl.info(GenomeLogCategory.UnitTest, "Hello");

  }
}
