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

package de.micromata.genome.util.strings.converter;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test of the StandardStringConverter.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class StandardStringConverterTest
{
  private void internalTestConvert(String s, Object obj)
  {
    StringConverter sc = StandardStringConverter.get();
    Assert.assertEquals(sc.cast(s, obj.getClass()), obj);
  }

  private void internalTestConvertArray(String s, Object[] obj)
  {
    StringConverter sc = StandardStringConverter.get();
    Assert.assertArrayEquals((Object[]) sc.cast(s, obj.getClass()), obj);
  }

  @Test
  public void testIt()
  {
    internalTestConvertArray("1,2", new Long[] { 1L, 2L});
    internalTestConvert("asdf", "asdf");
    internalTestConvert("42", new Integer(42));
    internalTestConvert("42.5", new Double(42.5));
    internalTestConvert("42.5", new BigDecimal("42.5"));
    internalTestConvertArray("a,b", new String[] { "a", "b"});

  }
}
