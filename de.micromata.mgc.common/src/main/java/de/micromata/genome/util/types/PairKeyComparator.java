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

package de.micromata.genome.util.types;

import java.util.Comparator;

/**
 * Comparator to compare keys
 * 
 * @author roger@micromata.de
 * 
 */
public class PairKeyComparator<K extends Comparable<K>, V> implements Comparator<Pair<K, V>>
{

  @Override
  public int compare(Pair<K, V> o1, Pair<K, V> o2)
  {
    K k1 = o1.getFirst();
    K k2 = o2.getFirst();
    if (k1 == null) {
      return 1;
    }
    if (k2 == null) {
      return -1;
    }
    return k1.compareTo(k2);
  }

}