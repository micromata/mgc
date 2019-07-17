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

package de.micromata.mgc.common.test;

import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.LocalSettingsEnv;
import de.micromata.genome.util.runtime.Log4JInitializer;
import de.micromata.genome.util.runtime.jndi.JndiMockupNamingContextBuilder;

import org.junit.jupiter.api.BeforeAll;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.util.Hashtable;

/**
 * Base class for MGC tests with initializing localsettings.
 * This class is Junit Jupiter 5 compatible.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MgcTestCase5
{
  protected static LocalSettingsEnv localSettingsEnv;

  @BeforeAll
  public static void initLs()
  {
    LocalSettings ls = LocalSettings.get();
    Log4JInitializer.initializeLog4J();
    LocalSettingsEnv.get();
  }

  protected static void prepareJndi()
  {
    JndiMockupNamingContextBuilder contextBuilder = new JndiMockupNamingContextBuilder();
    Hashtable<String, Object> env = new Hashtable<String, Object>();
    InitialContextFactory initialContextFactory = contextBuilder.createInitialContextFactory(env);
    try {
      Context initialContext = initialContextFactory.getInitialContext(env);
      localSettingsEnv = new LocalSettingsEnv(initialContext);
      contextBuilder.activate();

    } catch (NamingException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static void sleep(long milliseconds)
  {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
