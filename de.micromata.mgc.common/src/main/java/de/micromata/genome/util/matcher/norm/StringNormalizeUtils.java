package de.micromata.genome.util.matcher.norm;

import java.text.Normalizer;
import java.util.regex.Pattern;

import org.apache.commons.codec.language.ColognePhonetic;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.lang.StringUtils;

/**
 * Normalization of a String.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class StringNormalizeUtils
{
  /**
   * Convert the string into upper case.
   */
  public static final char UPPERCASE = 'u';
  /**
   * throw away all non ascii letter/numbers.
   */
  public static final char ASCIILETTERNUMBERSONLY = 'a';
  /**
   * throw away all whitespace characters.
   */
  public static final char NOWHITESPACE = 'w';

  /**
   * remove umlauts and accents.
   */
  public static final char REPLACEUMLAUTS = 'x';

  /**
   * convert to soundex.
   */
  public static final char SOUNDEX = 'e';

  /**
   * convert to german Koelnische Phonetik.
   */
  public static final char COLOGNE = 'c';

  /**
   * The deaccent pattern.
   */
  private static ThreadLocal<Pattern> DEACCENT_PATTERN = new ThreadLocal<Pattern>()
  {
    @Override
    protected Pattern initialValue()
    {
      return Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    }
  };

  /**
   * Replace specifc composits.
   * 
   * @param text the text
   * @return the string
   */
  public static String replaceSpecifcComposits(String text)
  {
    text = StringUtils.replace(text, "\u00df", "ss");
    return text;
  }

  /**
   * Remove umlauts and accents. Also converts scharfes s to ss.
   * 
   * @param str the input
   * @return the converted string
   */
  public static String deAccent(String str)
  {

    // makes from one composit character to two characters. Second is the accent or umlaut sign
    String norm = Normalizer.normalize(str, Normalizer.Form.NFD);
    // throw away accent/umlaut character
    norm = DEACCENT_PATTERN.get().matcher(norm).replaceAll("");
    norm = replaceSpecifcComposits(norm);
    return norm;
  }

  /**
   * The non alphanum pattern.
   */
  private static ThreadLocal<Pattern> NON_ALPHANUM_PATTERN = new ThreadLocal<Pattern>()
  {
    @Override
    protected Pattern initialValue()
    {
      return Pattern.compile("[^A-Za-z0-9]");
    }
  };

  /**
   * The white chars.
   */
  private static String WHITE_CHARS = "" /* dummy empty string for homogeneity */
      + "\\u0009" // CHARACTER TABULATION
      + "\\u000A" // LINE FEED (LF)
      + "\\u000B" // LINE TABULATION
      + "\\u000C" // FORM FEED (FF)
      + "\\u000D" // CARRIAGE RETURN (CR)
      + "\\u0020" // SPACE
      + "\\u0085" // NEXT LINE (NEL) 
      + "\\u00A0" // NO-BREAK SPACE
      + "\\u1680" // OGHAM SPACE MARK
      + "\\u180E" // MONGOLIAN VOWEL SEPARATOR
      + "\\u2000" // EN QUAD 
      + "\\u2001" // EM QUAD 
      + "\\u2002" // EN SPACE
      + "\\u2003" // EM SPACE
      + "\\u2004" // THREE-PER-EM SPACE
      + "\\u2005" // FOUR-PER-EM SPACE
      + "\\u2006" // SIX-PER-EM SPACE
      + "\\u2007" // FIGURE SPACE
      + "\\u2008" // PUNCTUATION SPACE
      + "\\u2009" // THIN SPACE
      + "\\u200A" // HAIR SPACE
      + "\\u2028" // LINE SEPARATOR
      + "\\u2029" // PARAGRAPH SEPARATOR
      + "\\u202F" // NARROW NO-BREAK SPACE
      + "\\u205F" // MEDIUM MATHEMATICAL SPACE
      + "\\u3000" // IDEOGRAPHIC SPACE
      ;

  /* A \S that actually works for Java’s native character set: Unicode */
  /**
   * The non whitespace pattern.
   */
  private static ThreadLocal<Pattern> NON_WHITESPACE_PATTERN = new ThreadLocal<Pattern>()
  {
    @Override
    protected Pattern initialValue()
    {
      return Pattern.compile("[^" + WHITE_CHARS + "]");
    }
  };

  /**
   * normalize a string.
   * 
   * @param text the text
   * @param flags a collection of characters. See public static finals in this class.
   * @return the string
   */
  public static String normalize(String text, String flags)
  {
    if (text == null) {
      return text;
    }
    if (flags.indexOf(REPLACEUMLAUTS) != -1) {
      text = deAccent(text); //replaceUmlauts(text);
    }
    if (flags.indexOf(UPPERCASE) != -1) {
      text = text.toUpperCase();
    }
    if (flags.indexOf(NOWHITESPACE) != -1) {
      text = NON_WHITESPACE_PATTERN.get().matcher(text).replaceAll("");
    }
    if (flags.indexOf(ASCIILETTERNUMBERSONLY) != -1) {
      text = NON_ALPHANUM_PATTERN.get().matcher(text).replaceAll("");
    }
    if (flags.indexOf(SOUNDEX) != -1) {
      text = Soundex.US_ENGLISH.encode(text);
    }
    if (flags.indexOf(COLOGNE) != -1) {
      text = new ColognePhonetic().encode(text);
    }
    return text;
  }

}
