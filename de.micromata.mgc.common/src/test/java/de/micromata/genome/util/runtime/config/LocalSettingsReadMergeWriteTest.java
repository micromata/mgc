package de.micromata.genome.util.runtime.config;

import java.io.File;

import org.junit.Test;

import de.micromata.genome.util.collections.OrderedProperties;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.StdLocalSettingsLoader;

public class LocalSettingsReadMergeWriteTest
{
  static class ExtLocalSettingsLoader extends StdLocalSettingsLoader
  {
    OrderedPropertiesWithComments origProps = new OrderedPropertiesWithComments();

    public ExtLocalSettingsLoader(String localSettingsFile)
    {
      super(localSettingsFile, null, null);
    }

    @Override
    protected OrderedProperties newProperties(boolean originalLocalSettingsFile)
    {
      if (originalLocalSettingsFile == true) {
        return origProps;
      }
      return new OrderedPropertiesWithComments();
    }

  }

  @Test
  public void testIt()
  {
    ExtLocalSettingsLoader loader = new ExtLocalSettingsLoader(
        "./dev/extrc/test/properties/ls_with_comments1.properties");
    LocalSettings localSettings = loader.loadSettings();

    MergingLocalSettingsWriter lswout = new MergingLocalSettingsWriter(loader.origProps);
    LocalSettingsWriter sec = lswout.newSection("Coded");
    sec.put("a", "c");
    sec.put("c", "3", "New Comment");
    lswout.store(new File("./target/merged.properties"));

  }
}
