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

package de.micromata.genome.util.matcher;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.util.matcher.string.SimpleWildcardMatcherFactory;

public class MatcherTest
{
  // @Test
  public void testGroovy()
  {
    SimpleWildcardMatcherFactory<String> mf = new SimpleWildcardMatcherFactory<String>();
    Matcher<String> m = mf.createMatcher("${return arg.equalsIgnoreCase('TEST')}");
    Assert.assertTrue(m.match("test"));
    m = mf.createMatcher("${return arg.equalsIgnoreCase('aTEST')}");
    Assert.assertFalse(m.match("test"));
  }

  @Test
  public void testEscaping()
  {
    String pattern = "\\*a";
    SimpleWildcardMatcherFactory<String> mf = new SimpleWildcardMatcherFactory<String>();
    Matcher<String> m = mf.createMatcher(pattern);
    Assert.assertTrue(m.match("*a"));
    String rp = mf.getRuleString(m);
    m = mf.createMatcher("*a");
    Assert.assertTrue(m.match("*a"));
  }
}
