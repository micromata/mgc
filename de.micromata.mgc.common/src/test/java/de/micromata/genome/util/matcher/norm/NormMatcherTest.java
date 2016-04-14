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

import de.micromata.genome.util.matcher.MatcherFactory;
import de.micromata.genome.util.matcher.MatcherTestBase;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class NormMatcherTest extends MatcherTestBase
{
  @Override
  protected MatcherFactory<String> createMatcherFactory()
  {
    return new NormBooleanMatcherFactory<String>();
  }

  /**
   * Testet einige basisoperationen des Matchers
   */
  @Test
  public void testBasicStringMatcher()
  {

    checkMatches("a or b", "a");
    checkMatches("a or b", "b");
    checkNotMatches("a or b", "c");
    checkMatches("a* and *b", "axb");
    checkNotMatches("a* and *c", "axb");
    ;
  }

  @Test
  public void testNormSearchUpperCaseUmlautStringMatcher()
  {
    checkMatches("^ux^a or b", "a");
    checkMatches("^ux^a or b", "A");
    checkMatches("^ux^ä", "A");
    checkMatches("^ux^*ä", "sdfA");
  }

  @Test
  public void testNormSearchUmlautStringMatcher()
  {
    checkMatches("^x^a or b", "a");
    checkMatches("^x^a or b", "a");
    checkNotMatches("^x^a or b", "A");
    checkMatches("^ux^ä", "A");
    checkMatches("^x^*Ä", "sdfA");
    checkNotMatches("^x^*Ä", "sdfa");
    checkMatches("^x^a or b", "a");
  }

  @Test
  public void testNormOnlyAscii()
  {
    checkMatches("^a^\\ a", "a");
    checkNotMatches("^a^\\ a", "A");
    checkMatches("^au^\\ a", "A");
  }

  @Test
  public void testNormEscapes()
  {
    checkMatches("^ux^\\ a", " A");
    checkNotMatches("^ux^\\ a", "A");
    checkMatches("^ux^\\^a", "^A");
  }

  @Test
  public void testSoundEx()
  {
    checkMatches("^e^\\aufa", "Aufa");
    checkMatches("^e^\\aufa", "Aupha");
    checkMatches("^e^\\aufa", "Auphe");
    checkMatches("^e^\\eifa", "Euphe");
  }

  @Test
  public void testCologne()
  {
    checkMatches("^c^\\aufa", "Aufa");
    checkMatches("^c^\\aufa", "Aupha");
    checkMatches("^c^\\aufa", "Auphe");
    checkMatches("^c^\\eifa", "Euphe");
  }

  @Test
  public void testCologne2()
  {
    checkMatches("^nu^*aufa*", "xAufax");
  }
}
