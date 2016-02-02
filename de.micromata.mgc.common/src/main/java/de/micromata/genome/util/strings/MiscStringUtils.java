package de.micromata.genome.util.strings;

import org.apache.commons.lang.StringUtils;

/**
 * Collection of String utilities
 * 
 * @author roger
 * 
 */
public class MiscStringUtils
{
  /**
   * Shorten a String from the left side
   * 
   * @param s
   * @param maxLength
   * @return
   */
  public static String cutLeft(String s, int maxLength)
  {
    if (s == null)
      return "";
    if (s.length() <= maxLength)
      return s;
    return s.substring(s.length() - maxLength);
  }

  /**
   * Shorten a String from the right side
   * 
   * @param s
   * @param maxLength
   * @return
   */
  public static String cutRight(String s, int maxLength)
  {
    return StringUtils.substring(s, 0, maxLength);
  }

}
