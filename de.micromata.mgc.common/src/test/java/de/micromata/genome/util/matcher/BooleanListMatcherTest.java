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

public class BooleanListMatcherTest
{
  @Test
  public void testReqExprWithPlus()
  {
    String pattern = "a, ~[Xy].\\+";
    BooleanListRulesFactory<String> blf = new BooleanListRulesFactory<String>();
    Matcher<String> m = blf.createMatcher(pattern);
    m.match("a");
  }

  private void testParse(String pattern, String[] matching, String[] notmatching)
  {
    BooleanListRulesFactory<String> blf = new BooleanListRulesFactory<String>();
    Matcher<String> m = blf.createMatcher(pattern);
    String dc = blf.getRuleString(m);
    System.out.println("p: " + pattern + "; decp: " + dc);
    for (String t : matching) {
      boolean match = m.match(t);
      Assert.assertTrue("Should match. pattern:" + pattern + "; test: " + t, match);
    }
    for (String t : notmatching) {
      boolean match = m.match(t);
      Assert.assertFalse("Should NOT match. pattern:" + pattern + "; test: " + t, match);
    }
  }

  private static String[] sa(String... strings)
  {
    return strings;
  }

  @Test
  public void testParse()
  {
    testParse("(*a && b*)", sa("ba"), sa("c", "ab"));
    testParse("a,b", sa("a", "b"), sa("c", "ab"));
    testParse("a || b", sa("a", "b"), sa("c", "ab"));

    testParse("((a) || (b))", sa("a", "b"), sa("c", "ab"));

    testParse("*a && !b*", sa("xa", "asdfa"), sa("ba", "xx", "bx"));

    testParse("(*a && b*) || (*c && d*)", sa("ba", "dxc"), sa("xx", "xx", "bx"));
  }

  protected void testFailure(String pattern)
  {
    try {
      testParse(pattern, sa(), sa());
      Assert.fail("Expect exception");
    } catch (InvalidMatcherGrammar ex) {
      Assert.assertTrue("Expected ex: " + ex.getMessage(), true);
    }
  }

  @Test
  public void testFailures()
  {
    testFailure("a(");
    testFailure("a &&");
    testFailure(")a");
  }

  @Test
  public void testEscaping()
  {
    testParse("\\,ab", sa(",ab"), sa("ab"));
  }
}
