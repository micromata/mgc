package de.micromata.genome.util.text;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * The Class TextSplitterUtils.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class TextSplitterUtils
{

  /**
   * The Class TKInput.
   */
  public static class TKInput
  {

    /**
     * The text.
     */
    private String text;

    /**
     * The escape char.
     */
    private char escapeChar;

    /**
     * The return unescaped.
     */
    private boolean returnUnescaped;

    /**
     * The position.
     */
    private int position;

    /**
     * The length.
     */
    public int length;

    /**
     * Instantiates a new TK input.
     *
     * @param text the text
     * @param escapeChar the escape char
     * @param returnUnescaped the return unescaped
     */
    public TKInput(String text, char escapeChar, boolean returnUnescaped)
    {
      this.text = text;
      this.position = 0;
      this.length = text.length();
      this.escapeChar = escapeChar;
      this.returnUnescaped = returnUnescaped;
    }

    /**
     * Eof.
     *
     * @return true, if successful
     */
    final public boolean eof()
    {
      return position >= length;
    }

    /**
     * Rest.
     *
     * @return the string
     */
    public String rest()
    {
      return text.substring(position);
    }

    /**
     * Lookup.
     *
     * @param tk the tk
     * @return true, if successful
     */
    public boolean lookup(Token tk)
    {
      // if (escapeChar == 0)
      return tk.match(rest());

      // int tki = 0;
      // int tkil = tk.length();
      // for (int i = position; i < length && tki < tkil; ++i, ++tki) {
      // char c = text.charAt(i);
      // if (c == escapeChar) {
      // if (i + 1 >= length)
      // return false;
      // c = text.charAt(i);
      // }
      // if (c != tk.charAt(tki))
      // return false;
      // }
      // return tki == tkil;
    }

    /**
     * Checks if is token.
     *
     * @param tokens the tokens
     * @return the token
     */
    public Token isToken(Token[] tokens)
    {
      for (Token tk : tokens) {
        if (lookup(tk) == true) {
          return tk;
        }
      }
      return null;
    }

    /**
     * Read.
     *
     * @param tokens the tokens
     * @return the token result
     */
    public TokenResult read(Token[] tokens)
    {
      Token tk = isToken(tokens);
      if (tk != null) {
        TokenResult tkn = tk.consume(rest(), escapeChar);
        position += tkn.getConsumedLength();
        return tkn;
      }
      StringBuilder sb = null;
      int lastSafedPos = position;
      for (; position < length; ++position) {
        char c = text.charAt(position);
        if (c == escapeChar) {
          if (position + 1 >= length) {
            throw new RuntimeException("Escape character at end of input. escpe=" + escapeChar + "; text: " + text);
          }
          if (returnUnescaped == false && sb == null) {
            sb = new StringBuilder();
            sb.append(text.substring(lastSafedPos, position));
            lastSafedPos = position;
          }
          ++position;
          c = text.charAt(position);

        } else if (isToken(tokens) != null) {
          break;
        }
        if (sb != null) {
          sb.append(c);
        }
      }
      if (sb != null) {
        return new UnmatchedToken(sb.toString());
      }
      return new UnmatchedToken(text.substring(lastSafedPos, position));
    }
  }

  /**
   * Parses the string tokens.
   *
   * @param text the text
   * @param tokens the tokens
   * @param escapeChar the escape char
   * @param returnDelimiter the return delimiter
   * @return the list
   */
  public static List<String> parseStringTokens(String text, String[] tokens, char escapeChar, boolean returnDelimiter)
  {
    return parseStringTokens(text, tokens, escapeChar, returnDelimiter, false);
  }

  /**
   * Parses the string tokens.
   *
   * @param text the text
   * @param tokens used as RegExp
   * @param escapeChar the escape char
   * @param returnDelimiter the return delimiter
   * @param returnUnescaped the return unescaped
   * @return the list
   */
  public static List<String> parseStringTokens(String text, String[] tokens, char escapeChar, boolean returnDelimiter,
      boolean returnUnescaped)
  {
    Token[] tks = new Token[tokens.length];
    for (int i = 0; i < tokens.length; ++i) {
      String p = tokens[i];
      p = Pattern.quote(p);
      p = "^(" + p + ")(.*)";
      tks[i] = new RegExpToken(i + 1, p);
    }
    List<TokenResult> tksr = parseStringTokens(text, tks, escapeChar, returnDelimiter, returnUnescaped);
    List<String> ret = new ArrayList<String>();
    for (TokenResult tkr : tksr) {
      ret.add(tkr.getConsumed());
    }
    return ret;
  }

  /**
   * Parses the string tokens.
   *
   * @param text the text
   * @param tokens the tokens
   * @param escapeChar the escape char
   * @param returnDelimiter the return delimiter
   * @return the list
   */
  public static List<String> parseStringTokens(String text, char[] tokens, char escapeChar, boolean returnDelimiter)
  {
    return parseStringTokens(text, tokens, escapeChar, returnDelimiter, false);
  }

  /**
   * Parses the string tokens.
   *
   * @param text the text
   * @param tokens the tokens
   * @param escapeChar the escape char
   * @param returnDelimiter the return delimiter
   * @param returnUnescaped the return unescaped
   * @return the list
   */
  public static List<String> parseStringTokens(String text, char[] tokens, char escapeChar, boolean returnDelimiter,
      boolean returnUnescaped)
  {
    Token[] tks = new Token[tokens.length];
    for (int i = 0; i < tokens.length; ++i) {
      tks[i] = new CharToken(i + 1, tokens[i]);
    }
    List<TokenResult> tksr = parseStringTokens(text, tks, escapeChar, returnDelimiter, returnUnescaped);
    List<String> ret = new ArrayList<String>();
    for (TokenResult tkr : tksr) {
      ret.add(tkr.getConsumed());
    }
    return ret;
  }

  /**
   * Parses the string tokens.
   *
   * @param text the text
   * @param tokens the tokens
   * @param escapeChar the escape char
   * @param returnDelimiter the return delimiter
   * @return the list
   */
  public static List<TokenResult> parseStringTokens(String text, Token[] tokens, char escapeChar,
      boolean returnDelimiter)
  {
    return parseStringTokens(text, tokens, escapeChar, returnDelimiter, false);
  }

  /**
   * Unescape.
   *
   * @param text the text
   * @param escapeChar the escape char
   * @return the string
   */
  public static String unescape(String text, char escapeChar)
  {
    if (text.indexOf(escapeChar) == -1) {
      return text;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < text.length(); ++i) {
      char c = text.charAt(i);
      if (c == escapeChar) {
        ++i;
        if (i >= text.length()) {
          throw new RuntimeException("Escape character '" + escapeChar + "' at end of text: " + text);
        }
        sb.append(text.charAt(i));
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * Parses the string tokens.
   *
   * @param text the text
   * @param tokens the tokens
   * @param escapeChar the escape char
   * @param returnDelimiter the return delimiter
   * @param returnUnescaped the return unescaped
   * @return the list
   */
  public static List<TokenResult> parseStringTokens(String text, Token[] tokens, char escapeChar,
      boolean returnDelimiter,
      boolean returnUnescaped)
  {
    List<TokenResult> result = new ArrayList<TokenResult>();
    if (StringUtils.isEmpty(text) == true) {
      return result;
    }
    TKInput tkinp = new TKInput(text, escapeChar, returnUnescaped);
    while (tkinp.eof() == false) {
      TokenResult tk = tkinp.read(tokens);
      if (tk == null) {
        break;
      }
      if ((tk instanceof UnmatchedToken) == false && returnDelimiter == false) {
        continue;
      }

      result.add(tk);
    }
    return result;
  }

  // public static List<String> parseStringTokens(String text, char[] tokens, char escapeChar, boolean returnDelimiter)
  // {
  // String[] stk = new String[tokens.length];
  // for (int i = 0; i < tokens.length; ++i) {
  // stk[i] = Character.toString(tokens[i]);
  // }
  // return parseStringTokens(text, stk, escapeChar, returnDelimiter);
  // }

  /**
   * Verwendet einen StringTokenizer und liefert das Ergebnis als Liste.
   *
   * @param text the text
   * @param tokens the tokens
   * @return the list
   */
  public static List<String> parseStringTokenWOD(String text, char... tokens)
  {
    return parseStringTokens(text, tokens, false);
  }

  /**
   * Parses the string token wd.
   *
   * @param text the text
   * @param tokens the tokens
   * @return the list
   */
  public static List<String> parseStringTokenWD(String text, char... tokens)
  {
    return parseStringTokens(text, tokens, true);
  }

  /**
   * Parses the string tokens.
   *
   * @param text the text
   * @param tokens the tokens
   * @param returnDelimiter the return delimiter
   * @return the list
   */
  public static List<String> parseStringTokens(String text, char[] tokens, boolean returnDelimiter)
  {
    List<String> result = new ArrayList<String>();
    String t = new String(tokens);
    StringTokenizer st = new StringTokenizer(text, t, returnDelimiter);
    while (st.hasMoreTokens() == true) {
      result.add(st.nextToken());
    }
    return result;
  }

  /**
   * return first found index of search which is not escaped.
   *
   * @param text the text
   * @param search the search
   * @param escapeChar the escape char
   * @return the unescaped index of
   */
  public static int getUnescapedIndexOf(String text, char search, char escapeChar)
  {
    for (int i = 0; i < text.length(); ++i) {
      char c = text.charAt(i);
      if (c == escapeChar) {
        ++i;
      } else if (c == search) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Gets the unescaped index of.
   *
   * @param text the text
   * @param search the search
   * @return the unescaped index of
   */
  public static int getUnescapedIndexOf(String text, char search)
  {
    return getUnescapedIndexOf(text, search, '\\');
  }

  /**
   * Find token pos.
   *
   * @param text the text
   * @param search the search
   * @param escapeChar the escape char
   * @return the list
   */
  public static List<Integer> findTokenPos(String text, char search, char escapeChar)
  {
    List<Integer> ret = new ArrayList<Integer>();
    for (int i = 0; i < text.length(); ++i) {
      char c = text.charAt(i);
      if (c == escapeChar) {
        ++i;
      } else if (c == search) {
        ret.add(i);
      }
    }
    return ret;
  }

  /**
   * Unescape.
   *
   * @param text the text
   * @param escapeChar the escape char
   * @param potentialEscaped the potential escaped
   * @return the string
   */
  public static String unescape(String text, char escapeChar, char... potentialEscaped)
  {
    if (text.indexOf(escapeChar) == -1) {
      return text;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < text.length(); ++i) {
      char c = text.charAt(i);
      if (c == escapeChar) {
        ++i;
        if (text.length() > i && ArrayUtils.contains(potentialEscaped, text.charAt(i)) == true) {
          sb.append(text.charAt(i));
        } else {
          sb.append(escapeChar).append(text.charAt(i));
        }
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * Escape.
   *
   * @param text the text
   * @param escapeChar the escape char
   * @param toEscaped the to escaped
   * @return the string
   */
  public static String escape(String text, char escapeChar, char... toEscaped)
  {
    if (StringUtils.indexOfAny(text, toEscaped) == -1) {
      return text;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < text.length(); ++i) {
      char c = text.charAt(i);
      if (ArrayUtils.contains(toEscaped, c) == true) {
        sb.append(escapeChar);
      }
      sb.append(c);
    }
    return sb.toString();
  }

  /**
   * Parse tokens, but also ignore tokenizing inside quotes. The quote sign itself can be quoted with \\.
   * 
   * Inside the quoted only the quote sign will be unescaped, all other \\ will be remain.
   *
   * @param text the text
   * @param tokens the tokens
   * @param returnDelimiter the return delimiter
   * @param quoteChar the quote char
   * @return list of tokens
   * @see TextSplitterUtilsQuotedTest
   * @since 1.2.1
   */
  public static List<String> parseQuotedStringTokens(String text, String[] tokens, boolean returnDelimiter,
      char quoteChar)
  {
    Token[] tks = new Token[tokens.length + 1];
    int i = 0;
    int quoteTk = -5;

    for (; i < tokens.length; ++i) {
      String p = tokens[i];
      if (p.length() == 1) {
        tks[i] = new CharToken(i + 1, p.charAt(0));
      } else {
        tks[i] = new StringToken(i + 1, p);
      }
    }
    char escapeChar = '\\';
    tks[tks.length - 1] = new CharToken(quoteTk, quoteChar);
    List<TokenResult> tksr = TextSplitterUtils.parseStringTokens(text, tks, escapeChar, true, true);
    List<String> ret = new ArrayList<String>();
    TokenResult tk;
    String ecapeQuote = "" + escapeChar + quoteChar;
    boolean previousWasSplitter = true;
    nextMainLoop: for (i = 0; i < tksr.size(); ++i) {
      tk = tksr.get(i);
      if (tk.getTokenType() == quoteTk) {
        StringBuilder sb = new StringBuilder();
        ++i;
        for (; i < tksr.size(); ++i) {
          tk = tksr.get(i);
          if (tk.getTokenType() == quoteTk) {
            if (previousWasSplitter == true) {
              ret.add(sb.toString());
            } else {
              ret.set(ret.size() - 1, ret.get(ret.size() - 1) + sb.toString());
            }
            previousWasSplitter = false;
            continue nextMainLoop;
          }
          String s = tk.getConsumed();
          s = StringUtils.replace(s, ecapeQuote, "" + quoteChar);
          sb.append(s);
        }
        throw new IllegalArgumentException("Missing end Quote in " + text);
      }
      if (tk.getTokenType() == 0) {
        if (previousWasSplitter == false) {
          ret.set(ret.size() - 1, ret.get(ret.size() - 1) + unescape(tk.getConsumed(), escapeChar));
        } else {
          ret.add(unescape(tk.getConsumed(), escapeChar));
        }

        previousWasSplitter = false;
      } else {
        previousWasSplitter = true;
        if (returnDelimiter == true) {
          ret.add(tk.getConsumed());
        }
      }
    }
    return ret;
  }

  /**
   * Verwendet einen StringTokenizer und liefert das Ergebnis als Liste.
   *
   * @param text the text
   * @param delimiter the delimiter
   * @param returnDelimiter the return delimiter
   * @return the list
   */
  public static List<String> parseStringTokens(String text, String delimiter, boolean returnDelimiter)
  {
    List<String> result = new ArrayList<String>();
    StringTokenizer st = new StringTokenizer(text != null ? text : "", delimiter, returnDelimiter);
    while (st.hasMoreTokens() == true) {
      result.add(st.nextToken());
    }
    return result;
  }
}
