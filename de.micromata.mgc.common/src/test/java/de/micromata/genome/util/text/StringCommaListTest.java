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

package de.micromata.genome.util.text;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests the StringCommaList.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class StringCommaListTest
{

  @Ignore
  private void checkEquals(String[] array)
  {
    String s = StringCommaList.encodeStringArray(array);
    String[] ret = StringCommaList.decodeStringArray(s);
    Assert.assertArrayEquals(array, ret);
  }

  @Ignore
  private void checkEquals(Long[] array)
  {
    String s = StringCommaList.encodeLongArray(array);
    Long[] ret = StringCommaList.decodeLongArray(s);
    Assert.assertArrayEquals(array, ret);
  }

  @Test
  public void testStringCommaList()
  {
    checkEquals(new String[] { "a,b", "", "", "sadf"});
    checkEquals(new String[] { "a\\b", "", "", "sadf"});
    checkEquals(new String[] { "a\\,b", "", "", "sadf"});
    checkEquals(new String[] {});
    checkEquals(new String[] { "asdf"});
    checkEquals(new String[] { "asdf", "", "", "sadf"});
  }

  @Test
  public void testLongCommaList()
  {
    checkEquals(new Long[] {});
    checkEquals(new Long[] { 14L});
    checkEquals(new Long[] { 14L, 0L});
  }
}
