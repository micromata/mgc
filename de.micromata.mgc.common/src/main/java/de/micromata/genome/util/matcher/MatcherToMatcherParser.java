package de.micromata.genome.util.matcher;

import java.util.List;

import de.micromata.genome.util.text.TextSplitterUtils;
import de.micromata.genome.util.types.Pair;

/**
 * Utility to parse matcher to matcher.
 * 
 * @author roger
 * 
 */
public class MatcherToMatcherParser
{

  /**
   * Parses the.
   *
   * @param <LT> the generic type
   * @param <RT> the generic type
   * @param text the text
   * @param left the left
   * @param right the right
   * @param divider the divider
   * @param retVal the ret val
   */
  static public <LT, RT> void parse(String text, MatcherFactory<LT> left, MatcherFactory<RT> right, String divider,
      Pair<Matcher<LT>, Matcher<RT>> retVal)
  {
    List<String> parts = TextSplitterUtils.parseStringTokens(text, new String[] { divider, "\\" }, '\\', false, true);
    if (parts.size() != 2) {
      throw new RuntimeException("Expect " + divider + " in rule string. rule: " + text);
    }
    Matcher<LT> leftM = left.createMatcher(parts.get(0));
    Matcher<RT> rightM = right.createMatcher(parts.get(1));
    retVal.setFirst(leftM);
    retVal.setSecond(rightM);

  }

}
