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
import javax.naming.spi.InitialContextFactory;

import de.micromata.genome.util.bean.PrivateBeanUtils;

public class JndiMockupNamingContextBuilder extends SimpleNamingContextBuilder
{

  public JndiMockupNamingContextBuilder()
  {
    super();
  }

  @Override
  public InitialContextFactory createInitialContextFactory(Hashtable<?, ?> environment)
  {
    // if (environment != null && environment.get(Context.INITIAL_CONTEXT_FACTORY) != null) {
    // return super.createInitialContextFactory(environment);
    // }
    // Default case...
    return new InitialContextFactory()
    {
      @Override
      @SuppressWarnings("unchecked")
      public Context getInitialContext(Hashtable<?, ?> environment)
      {
        return new SimpleJndiContext("",
            PrivateBeanUtils.readField(JndiMockupNamingContextBuilder.this, "boundObjects", Hashtable.class),
            (Hashtable<String, Object>) environment);
      }
    };
  }
}
