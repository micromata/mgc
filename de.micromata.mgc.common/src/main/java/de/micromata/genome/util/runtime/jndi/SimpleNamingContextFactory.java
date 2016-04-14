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

package de.micromata.genome.util.runtime.jndi;

import java.util.Hashtable;

import javax.naming.Context;

import org.apache.log4j.Logger;

public class SimpleNamingContextFactory implements javax.naming.spi.InitialContextFactory
{
  public static final Logger LOG = Logger.getLogger(SimpleJndiContext.class);

  public static void register()
  {
    System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
        SimpleNamingContextFactory.class.getName());
    LOG.info("LsEnv; Registered Jndi Default ContextFactory: " + SimpleNamingContextFactory.class.getName());
  }

  static JndiMockupNamingContextBuilder jndiMod = new JndiMockupNamingContextBuilder();

  @Override
  public Context getInitialContext(Hashtable<?, ?> environment)
  {
    LOG.info("LsEnv; Create new JndiContext");
    return new SimpleNamingContext("",
        jndiMod.getBoundObjects(),
        (Hashtable<String, Object>) environment);
  }

}
