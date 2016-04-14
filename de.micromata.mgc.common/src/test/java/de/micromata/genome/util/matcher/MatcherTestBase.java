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

/**
 * Test base for matcher tests.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class MatcherTestBase
{

  protected MatcherFactory<String> createMatcherFactory()
  {
    return new BooleanListRulesFactory<String>();
  }

  protected Matcher<String> createMatcher(String pattern)
  {
    return createMatcherFactory().createMatcher(pattern);
  }

  protected void checkMatches(String pattern, String input)
  {
    Matcher<String> matcher = createMatcher(pattern);
    boolean match = matcher.match(input);
    Assert.assertTrue("Matcher with pattern: " + pattern + " does not match to: " + input, match);
  }

  protected void checkNotMatches(String pattern, String input)
  {
    Matcher<String> matcher = createMatcher(pattern);
    Assert.assertTrue("Matcher with pattern: " + pattern + " does match to: " + input, matcher.match(input) == false);
  }
}
