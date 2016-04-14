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

package de.micromata.genome.util.matcher.norm;

import org.junit.Test;

import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.matcher.MatcherTestBase;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class NormMatcherGroovyTest extends MatcherTestBase
{
  protected Matcher<String> createMatcher(String pattern)
  {
    return new NormBooleanMatcherFactory<String>().createMatcher(pattern);
  }

  @Test
  public void testQuotesinGroovy()
  {
    checkMatches("a or ${arg.contains('{')}", "b{");
  }

  @Test
  public void testGroovyNestedBrackets()
  {
    checkMatches("a or ${arg.grep { c -> c == 'b' }.size() == 2}", "abb");
  }

  @Test
  public void testGroovy()
  {
    checkMatches("a or ${arg.length() == 3}", "a");
    checkMatches("a or ${arg.length() == 3}", "abc");
    checkMatches("a or ${arg.length() == 3 or arg.length() == 2}", "bc");
    checkMatches("a or ${arg.length() == 3 or arg.length() == 2}", "cbc");
  }
}
