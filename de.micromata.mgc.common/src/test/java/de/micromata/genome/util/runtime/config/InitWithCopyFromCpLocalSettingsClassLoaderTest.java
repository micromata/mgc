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
    LOG.info("Try to copy from cp: " + file.getAbsolutePath());
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
