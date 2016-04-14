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

package de.micromata.genome.util.event;

/**
 * Standard registry.
 * 
 * Before using this registry, consider to create own registry instances for your domain specific usage.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MgcEventRegistries
{
  private static SimpleEventClassRegistry eventClassRegistry = new SimpleEventClassRegistry("mgcclassregistry");
  private static SimpleEventInstanceRegistry eventInstanceRegistry = new SimpleEventInstanceRegistry(
      "mgcclassregistry");

  public static MgcEventClassRegistry getEventClassRegistry()
  {
    return eventClassRegistry;
  }

  public static McEventInstanceRegistry getEventInstanceRegistry()
  {
    return eventInstanceRegistry;
  }
}
