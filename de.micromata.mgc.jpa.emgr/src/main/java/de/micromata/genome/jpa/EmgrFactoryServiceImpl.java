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

package de.micromata.genome.jpa;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections15.list.SynchronizedList;

/**
 * The Class EmgrFactoryServiceImpl.
 */
public class EmgrFactoryServiceImpl implements EmgrFactoryService
{

  /** The factories. */
  private List<Reference<EmgrFactory<?>>> factories = SynchronizedList.decorate(new ArrayList<>());

  /**
   * Gets the factories.
   *
   * @return the factories
   */
  public List<EmgrFactory<?>> getFactories()
  {

    Iterator<Reference<EmgrFactory<?>>> iterator = factories.iterator();

    List<EmgrFactory<?>> list = new ArrayList<>();
    while (iterator.hasNext() == true) {
      Reference<EmgrFactory<?>> next = iterator.next();
      EmgrFactory<?> emgrFactory = next.get();
      if (emgrFactory == null) {
        iterator.remove();
      } else {
        list.add(emgrFactory);
      }
    }

    return list;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void register(EmgrFactory<?> emgrFac)
  {
    factories.add(new WeakReference<>(emgrFac));
  }
}
