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

package de.micromata.genome.util.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Add items to a ArrayList, but discarges duplicated elements.
 *
 * @author roger
 * @param <T> the generic type
 */
public class ArraySet<T>extends ArrayList<T> implements Set<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -1762503806954764180L;

  /**
   * Instantiates a new array set.
   */
  public ArraySet()
  {
    super();
  }

  /**
   * Instantiates a new array set.
   *
   * @param c the c
   */
  public ArraySet(Collection<? extends T> c)
  {
    super(c.size());
    for (T e : c) {
      add(e);
    }
  }

  /**
   * Instantiates a new array set.
   *
   * @param initialCapacity the initial capacity
   */
  public ArraySet(int initialCapacity)
  {
    super(initialCapacity);
  }

  @Override
  public boolean add(T e)
  {
    if (contains(e) == true) {
      return false;
    }
    return super.add(e);
  }

}
