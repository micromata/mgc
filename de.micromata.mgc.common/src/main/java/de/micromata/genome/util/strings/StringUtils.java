package de.micromata.genome.util.strings;

/**
 * Adapter class for StringUtils
 * 
 * @author roger
 * 
 */
@Deprecated
public class StringUtils extends org.apache.commons.lang.StringUtils
{
  /**
   * This was removed in later commons.lang
   * 
   * @param text
   * @param start
   * @return
   */
  public static boolean startsWith(String text, String start)
  {
    if (text == null) {
      return false;
    }
    return text.startsWith(start);
  }
}
