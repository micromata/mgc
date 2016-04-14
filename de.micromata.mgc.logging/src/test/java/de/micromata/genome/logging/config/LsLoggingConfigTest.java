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
