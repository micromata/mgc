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

  @Test
  public void testWithDefault()
  {

    StdLocalSettingsLoader loader = new StdLocalSettingsLoader();
    loader.setWorkingDirectory(new File("dev/extrc/test/properties"));
    LocalSettings ls = loader.loadSettings();
    String val = ls.get("mgc.common.test.mydefaultKey1", "ShouldNot");
    Assert.assertEquals("ValueDefault1", val);
    val = ls.get("mgc.common.test.mydefaultKey2", "ShouldNot");
    Assert.assertEquals("ExlicitValue2", val);

    System.setProperty("mgc.common.test.mydefaultKey3", "ViaProps");
    loader = new StdLocalSettingsLoader();
    loader.setWorkingDirectory(new File("dev/extrc/test/properties"));
    ls = loader.loadSettings();
    val = ls.get("mgc.common.test.mydefaultKey3", "ShouldNot");
    Assert.assertEquals("ViaProps", val);

  }

}
