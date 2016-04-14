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

package de.micromata.genome.util.collection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.util.collections.MergeUtils;
import de.micromata.genome.util.types.Pair;
import de.micromata.genome.util.types.Triple;

public class MergeUtilsTest
{
  @Test
  public void testMerge()
  {
    List<String> oldList = new ArrayList<String>();
    oldList.add("A");
    oldList.add("B");
    oldList.add("D");
    List<String> newList = new ArrayList<String>();
    newList.add("B");
    newList.add("C");
    Comparator<String> comp = new Comparator<String>()
    {

      @Override
      public int compare(String o1, String o2)
      {
        return o1.compareTo(o2);
      }
    };
    Triple<List<String>, List<Pair<String, String>>, List<String>> t = MergeUtils
        .mergeLists(oldList, newList, comp);
    Assert.assertEquals(1, t.getLeft().size());
    Assert.assertEquals("C", t.getLeft().get(0));
    Assert.assertEquals(1, t.getMiddle().size());
    Assert.assertEquals("B", t.getMiddle().get(0).getFirst());
    Assert.assertEquals(2, t.getRight().size());
    Assert.assertEquals("A", t.getRight().get(0));
  }
}
