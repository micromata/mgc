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

import org.junit.Test;

/**
 * Tests groovy matching.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class GroovyMatcherTest extends MatcherTestBase
{
  public static class MyClassProvider
  {
    String matchAgainst;

    public MyClassProvider(String matchAgainst)
    {
      this.matchAgainst = matchAgainst;
    }

    public int myLength(String l)
    {
      return l.length();
    }

    public int thisLength()
    {
      return matchAgainst.length();
    }
  }

  @Override
  protected MatcherFactory<String> createMatcherFactory()
  {
    BooleanListRulesFactory<String> ret = new BooleanListRulesFactory<String>();
    ret.getTokenFactories().add(0, new GroovyMatcherFactory<String>(MyClassProvider.class));
    return ret;
  }

  @Test
  public void testGroovyWithFunction()
  {
    checkMatches("${myLength('a') == 1 || arg.length() == 3}", "a");
    checkMatches("${myLength('a') == 2 || arg.length() == 3}", "abc");
    checkMatches("${thisLength() == 3}", "abc");
    checkNotMatches("${thisLength() == 3}", "cabc");
  }

  @Test
  public void testGroovy()
  {
    //checkMatches("a || b", "a");
    checkMatches("a || ${arg.length() == 3}", "a");
    checkMatches("a || ${arg.length() == 3}", "abc");
    checkMatches("a || ${arg.length() == 3 or arg.length() == 2}", "bc");
    checkMatches("a || ${arg.length() == 3 or arg.length() == 2}", "cbc");
  }

}
