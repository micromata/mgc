/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   04.07.2009
// Copyright Micromata 04.07.2009
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.matcher.string;

import de.micromata.genome.util.matcher.Matcher;

/**
 * Matcher builder for string based matcher.
 * 
 * @author roger@micromata.de
 * 
 */
public class StringMatcherBuilder
{

  /**
   * Contains.
   *
   * @param text the text
   * @return the matcher
   */
  public static Matcher<String> contains(String text)
  {
    return new ContainsMatcher<String>(text);
  }

  /**
   * Starts with.
   *
   * @param text the text
   * @return the matcher
   */
  public static Matcher<String> startsWith(String text)
  {
    return new StartWithMatcher<String>(text);
  }

  /**
   * Ends with.
   *
   * @param text the text
   * @return the matcher
   */
  public static Matcher<String> endsWith(String text)
  {
    return new EndsWithMatcher<String>(text);
  }

  /**
   * Wildcart.
   *
   * @param text the text
   * @return the matcher
   */
  public static Matcher<String> wildcart(String text)
  {
    return new WildcardMatcher<String>(text);
  }

  /**
   * Regexp.
   *
   * @param text the text
   * @return the matcher
   */
  public static Matcher<String> regexp(String text)
  {
    return new RegExpMatcher<String>(text);
  }
}
