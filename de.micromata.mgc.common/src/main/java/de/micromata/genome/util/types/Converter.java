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

package de.micromata.genome.util.types;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * Collections of methods to convert types.
 * 
 * @author roger@micromata.de
 * 
 */
public class Converter
{

  /**
   * Encode url param.
   *
   * @param value the value
   * @return the string
   */
  public static String encodeUrlParam(String value)
  {
    if (StringUtils.isEmpty(value) == true) {
      return "";
    }

    try {
      return URLEncoder.encode(value, CharEncoding.UTF_8);
    } catch (UnsupportedEncodingException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Kuerzt den Text auf angegebene UTF8 byte Laenge. Hierbei wird versucht die maximale StringlÃ¤nge zu ermitteln
   *
   * @param text the text
   * @param length the length
   * @return the string
   */
  public static String trimUtf8Exact(String text, int length)
  {
    if (text == null) {
      return "";
    }
    text = StringUtils.substring(text, 0, length);
    byte[] bytes = bytesFromString(text);
    while (bytes.length > length) {
      text = text.substring(0, text.length() - 1);
      bytes = bytesFromString(text);
    }
    return text;
  }

  /**
   * Kuerzt den Text auf angegebene UTF8 byte Laenge. Hierbei wird nicht genau gekuerzt, sondern die resultierenden
   * Bytes koennen noch kuerzer angegebene Laenge sein.
   *
   * @param text the text
   * @param length the length
   * @return the string
   */
  public static String trimUtf8(String text, int length)
  {
    if (text == null) {
      return "";
    }
    text = StringUtils.substring(text, 0, length);
    byte[] bytes = bytesFromString(text);
    if (bytes.length <= length) {
      return text;
    }
    int diff = bytes.length - length;
    int newl = text.length() - diff;
    if (newl <= 0) {
      return trimUtf8Exact(text, length);
    }
    return StringUtils.substring(text, 0, newl);
  }

  /**
   * This method ensures that the output String has only valid XML unicode characters as specified by the XML 1.0
   * standard. For reference, please see <a href="http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char">the standard</a>.
   * This method will return an empty String if the input is null or empty.
   * 
   * @param in The String whose non-valid characters we want to remove.
   * @return The in String, stripped of non-valid characters.
   */
  public static String stripNonValidXMLCharacters(String in)
  {
    StringBuilder out = new StringBuilder(); // Used to hold the output.
    char current; // Used to reference the current character.
    if (StringUtils.isEmpty(in)) {
      return in;
    }
    for (int i = 0; i < in.length(); i++) {
      current = in.charAt(i);
      if ((current == 0x9)
          || (current == 0xA)
          || (current == 0xD)
          || ((current >= 0x20) && (current <= 0xD7FF))
          || ((current >= 0xE000) && (current <= 0xFFFD))
          || ((current >= 0x10000) && (current <= 0x10FFFF))) {
        out.append(current);
      }
    }
    return out.toString();
  }

  /**
   * Konvertiert einen String von Utf8 bytes.
   *
   * @param bytes the bytes
   * @param encoding the encoding
   * @return the string
   */
  public static String stringFromBytes(byte[] bytes, String encoding)
  {
    if (bytes == null) {
      return "";
    }
    try {
      return new String(bytes, encoding);
    } catch (UnsupportedEncodingException ex) {
      return new String(bytes);
    }
  }

  /**
   * Konvertiert einen String von Utf8 bytes.
   *
   * @param bytes the bytes
   * @return the string
   */
  public static String stringFromBytes(byte[] bytes)
  {
    return stringFromBytes(bytes, CharEncoding.UTF_8);
  }

  /**
   * Konvertiert einen String nach Utf8 bytes.
   *
   * @param str the str
   * @return the byte[]
   */
  public static byte[] bytesFromString(String str)
  {
    if (str == null) {
      return null;
    }
    try {
      return str.getBytes(CharEncoding.UTF_8);
    } catch (UnsupportedEncodingException ex) {
      return str.getBytes();
    }
  }

  /**
   * Konvertiert String zu einem Integer. Liefert <code>null</code> falls die Konvertiertung nicht mÃ¶glich war.
   *
   * @param string the string
   * @return the integer
   */
  public static Integer stringToInteger(String string)
  {
    if (string == null) {
      return null;
    }
    try {
      Integer integer = Integer.parseInt(string);
      return integer;
    } catch (NumberFormatException nfex) {
      return null;
    }
  }

  /**
   * Convertiert String to int. If not possible (null, empty, not a parsable int) returns default
   *
   * @param str the str
   * @param defaultVal the default val
   * @return the int
   */
  public static int stringToInt(String str, int defaultVal)
  {
    if (str == null) {
      return defaultVal;
    }
    if (str.length() == 0) {
      return defaultVal;
    }
    try {
      Integer integer = Integer.parseInt(str);
      return integer;
    } catch (NumberFormatException nfex) {
      return defaultVal;
    }
  }

  /**
   * String to long.
   *
   * @param string the string
   * @return the long
   */
  public static Long stringToLong(String string)
  {
    if (string == null) {
      return null;
    }
    try {
      Long integer = Long.parseLong(string);
      return integer;
    } catch (NumberFormatException nfex) {
      return null;
    }
  }

  /**
   * Integer to string.
   *
   * @param i the i
   * @return the string
   */
  public static String integerToString(int i)
  {
    return Integer.toString(i);
  }

  /**
   * Integer to string.
   *
   * @param i the i
   * @return the string
   */
  public static String integerToString(Integer i)
  {
    if (i == null) {
      return null;
    }
    return Integer.toString(i);
  }

  /**
   * Copy convert int.
   *
   * @param source the source
   * @param sourceName the source name
   * @param target the target
   * @param targetName the target name
   * @return true, if successful
   */
  public static boolean copyConvertInt(Object source, String sourceName, Object target, String targetName)
  {

    try {
      Object so = PropertyUtils.getProperty(source, sourceName);
      Integer bd = null;
      if (so instanceof Integer) {
        bd = (Integer) so;
      } else {
        String s = (String) so;
        bd = Integer.parseInt(s);
      }
      PropertyUtils.setProperty(target, targetName, bd);
      return true;
    } catch (Throwable ex) { // NOSONAR "Illegal Catch" framework
      return false;
    }
  }

  /**
   * Konvertiert ein String in ein BigDecimal.
   *
   * @param source Stringified Bigdecimal
   * @return wenn source null oder leer <true, null> wenn source nicht leer <false, Wert> wenn konvertiert werden konnte
   *         ansonsten <false, null>
   */
  public static Pair<Boolean, BigDecimal> convertBigDecimal(String source)
  {
    if (StringUtils.isEmpty(source) == true) {
      return new Pair<Boolean, BigDecimal>(true, null);
    }
    source = source.replace(',', '.');
    try {
      return new Pair<Boolean, BigDecimal>(true, new BigDecimal(source));
    } catch (Exception ex) {
      return new Pair<Boolean, BigDecimal>(false, null);
    }
  }

  /**
   * Convert big decimal.
   *
   * @param source the source
   * @param defaultValue the default value
   * @return the big decimal
   */
  public static BigDecimal convertBigDecimal(String source, BigDecimal defaultValue)
  {
    Pair<Boolean, BigDecimal> p = convertBigDecimal(source);
    if (p.getSecond() == null) {
      return defaultValue;
    }
    return p.getSecond();
  }

  /**
   * Builds the normalized number string.
   *
   * @param nr the nr
   * @param scale the scale
   * @param decimalChar the decimal char
   * @param unit the unit
   * @return the string
   */
  public static String buildNormalizedNumberString(BigDecimal nr, int scale, char decimalChar, String unit)
  {
    return buildNormalizedNumberString(nr, 0, scale, decimalChar, unit);
  }

  /**
   * Builds the normalized number string.
   *
   * @param nr the nr
   * @param exp10 the exp10
   * @param scale the scale
   * @param decimalChar the decimal char
   * @param unit the unit
   * @return the string
   */
  public static String buildNormalizedNumberString(BigDecimal nr, int exp10, int scale, char decimalChar, String unit)
  {
    if (nr == null) {
      return "";
    }
    String str = nr.scaleByPowerOfTen(exp10).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString().replace('.',
        decimalChar);
    if (unit == null) {
      return str;
    }
    return str + " " + unit;
  }

  /**
   * Normalize number string.
   *
   * @param source the source
   * @param scale the scale
   * @param decimalChar the decimal char
   * @param unit the unit
   * @return the string
   */
  public static String normalizeNumberString(String source, int scale, char decimalChar, String unit)
  {
    source = StringUtils.trimToEmpty(source);
    if (StringUtils.isEmpty(source) == true) {
      return "";
    }
    return buildNormalizedNumberString(new BigDecimal(source.replace(',', '.')), 0, scale, decimalChar, unit);
  }

  /**
   * Normalize number string.
   *
   * @param source the source
   * @param exp10 the exp10
   * @param scale the scale
   * @param decimalChar the decimal char
   * @param unit the unit
   * @return the string
   */
  public static String normalizeNumberString(String source, int exp10, int scale, char decimalChar, String unit)
  {
    source = StringUtils.trimToEmpty(source);
    if (StringUtils.isEmpty(source) == true) {
      return "";
    }
    return buildNormalizedNumberString(new BigDecimal(source.replace(',', '.')), exp10, scale, decimalChar, unit);
  }

  /**
   * Convert big decimal or int.
   *
   * @param source the source
   * @return the pair
   */
  public static Pair<Boolean, BigDecimal> convertBigDecimalOrInt(String source)
  {
    Pair<Boolean, BigDecimal> ret = convertBigDecimal(source);
    if (ret.getFirst() == true) {
      return ret;
    }
    try {
      return new Pair<Boolean, BigDecimal>(true, new BigDecimal(Integer.parseInt(source))); // kann Integer parsen, wenn BigDecimal es nicht
      // kann ???
    } catch (Exception ex) {
      return new Pair<Boolean, BigDecimal>(false, null);
    }
  }

  /**
   * Copy convert big decimal.
   *
   * @param source the source
   * @param sourceName the source name
   * @param target the target
   * @param targetName the target name
   * @return true, if successful
   */
  public static boolean copyConvertBigDecimal(Object source, String sourceName, Object target, String targetName)
  {
    try {
      Object so = PropertyUtils.getProperty(source, sourceName);
      BigDecimal bd = null;
      if (so instanceof BigDecimal) {
        bd = (BigDecimal) so;
      } else {
        String s = (String) so;
        s = s.replace(',', '.');
        bd = new BigDecimal(s);
      }
      PropertyUtils.setProperty(target, targetName, bd);
      return true;
    } catch (Throwable ex) { // NOSONAR "Illegal Catch" framework
      return false;
    }
  }

  // Note: following is not threadsafe
  // public static final Locale DEFAULT_LOCALE = Locale.GERMANY;
  //
  // private static final DateFormat[] dateFormats = new DateFormat[] { new SimpleDateFormat("dd.MM.yyyy HH:mm", DEFAULT_LOCALE),
  // new SimpleDateFormat("yyyy-MM-dd HH:mm", DEFAULT_LOCALE)};
  //
  // /**
  // * @param source
  // * @param sourceName
  // * @param target
  // * @param targetName
  // * @return
  // */
  // public static boolean copyConvertSqlDate(Object source, String sourceName, Object target, String targetName)
  // {
  // try {
  // Object so = PropertyUtils.getProperty(source, sourceName);
  // Date date = null;
  // if (so instanceof Date) {
  // date = (Date) so;
  // } else {
  // String s = (String) so;
  // for (DateFormat df : dateFormats) {
  // try {
  // date = df.parse(s);
  // break;
  // } catch (Throwable ex) {
  //
  // }
  // }
  // }
  // if (date == null) {
  // return false;
  // }
  // PropertyUtils.setProperty(target, targetName, date);
  // return true;
  // } catch (Throwable ex) {
  // return false;
  // }
  // }

  /**
   * Encode base64.
   *
   * @param bytes the bytes
   * @return the string
   */
  public static String encodeBase64(byte[] bytes)
  {
    return stringFromBytes(Base64.encodeBase64(bytes));
  }

  /**
   * Decode base64.
   *
   * @param text the text
   * @return the byte[]
   */
  public static byte[] decodeBase64(String text)
  {
    return Base64.decodeBase64(bytesFromString(text));
  }

  /**
   * null save cast to String.
   *
   * @param o the o
   * @return String
   */
  public static String objectToString(Object o)
  {
    if (o instanceof String) {
      return (String) o;
    }
    return null;
  }

  /**
   * Object to int.
   *
   * @param o the o
   * @param defaultValue the default value
   * @return the int
   */
  public static int objectToInt(Object o, int defaultValue)
  {
    if (o == null) {
      return defaultValue;
    }
    if (o instanceof Number) {
      return ((Number) o).intValue();
    }
    if (o instanceof String) {
      try {
        return Integer.parseInt((String) o);
      } catch (NumberFormatException ex) {
      }
    }
    return defaultValue;
  }

  /**
   * null save cast to java.sql.Date from java.sql.Date or java.sql.Timestamp
   *
   * @param o the o
   * @return Date
   */
  public static Date objectToDate(Object o)
  {
    if (o instanceof Timestamp) {
      return new Date(((Timestamp) o).getTime());
    }
    if (o instanceof java.sql.Time) {
      return new Date(((java.sql.Time) o).getTime());
    }
    if (o instanceof java.sql.Date) {
      return new Date(((java.sql.Date) o).getTime());
    }
    if (o instanceof Date) {
      return (Date) o;
    }

    return null;
  }

  /**
   * The Constant isoDayFormat.
   */
  public static final ThreadLocal<SimpleDateFormat> isoDayFormat = new ThreadLocal<SimpleDateFormat>()
  {
    @Override
    protected SimpleDateFormat initialValue()
    {
      return new SimpleDateFormat("yyyy-MM-dd");
    }
  };

  /**
   * The Constant isoDateFormat.
   */
  public static final ThreadLocal<SimpleDateFormat> isoDateFormat = new ThreadLocal<SimpleDateFormat>()
  {
    @Override
    protected SimpleDateFormat initialValue()
    {
      return new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }
  };

  /**
   * The Constant isoTimestampFormat.
   */
  public static final ThreadLocal<SimpleDateFormat> isoTimestampFormat = new ThreadLocal<SimpleDateFormat>()
  {
    @Override
    protected SimpleDateFormat initialValue()
    {
      return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    }
  };

  /**
   * The Constant xsdDateTimeFormat.
   */
  public static final ThreadLocal<SimpleDateFormat> xsdDateTimeFormat = new ThreadLocal<SimpleDateFormat>()
  {
    @Override
    protected SimpleDateFormat initialValue()
    {
      return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    }
  };

  /**
   * Date to debug string.
   *
   * @param date the date
   * @return the string
   */
  public static String dateToDebugString(java.util.Date date)
  {
    if (date == null) {
      return "<null>";
    }
    return formatByIsoDateFormat(date);
  }

  /**
   * Object to debug string.
   *
   * @param obj the obj
   * @return the string
   */
  public static String objectToDebugString(Object obj)
  {
    if (obj == null) {
      return "";
    }
    if (obj instanceof java.util.Date) {
      return dateToDebugString((java.util.Date) obj);
    }
    return ObjectUtils.toString(obj);
  }

  /* !!!ACHTUNG !!! Die Formater Klassen sind nicht synchronisiert. Daher diese Methoden benutzen */
  /**
   * Synchonisierte Methode um das Datum ind das isoDateFormat String umzuwandeln.
   *
   * @param date the date
   * @return the string
   */

  public static String formatByIsoDateFormat(java.util.Date date)
  {
    if (date == null) {
      throw new RuntimeException("Error occurred.  Cause: Nullpointer at Date conversion ");
    }

    return isoDateFormat.get().format(date);

  }

  /**
   * Synchonisierte Methode um das Datum ind das isoDayFormat String umzuwandeln.
   *
   * @param date the date
   * @return the string
   */
  public static String formatByIsoDayFormat(java.util.Date date)
  {
    return isoDayFormat.get().format(date);
  }

  /**
   * Synchonisierte Methode um das Datum ind das isoTimeStampFormat String umzuwandeln.
   *
   * @param date the date
   * @return the string
   */
  public static String formatByIsoTimestampFormat(java.util.Date date)
  {
    return isoTimestampFormat.get().format(date);
  }

  /**
   * Synchonisierte Methode um das String in isoDateFormat in das Datum umzuwandeln.
   *
   * @param dateString the date string
   * @return the date
   * @throws ParseException the parse exception
   */
  public static Date parseByIsoDateFormat(String dateString) throws ParseException
  {
    return isoDateFormat.get().parse(dateString);
  }

  public static String getIsoDateFormatPattern()
  {
    return isoDateFormat.get().toPattern();
  }

  /**
   * Synchonisierte Methode um das String in isoDayFormat in das Datum umzuwandeln.
   *
   * @param dateString the date string
   * @return the date
   * @throws ParseException the parse exception
   */
  public static Date parseByIsoDayFormat(String dateString) throws ParseException
  {
    return isoDayFormat.get().parse(dateString);
  }

  /**
   * Synchonisierte Methode um das String in isoTimestampFormat in das Datum umzuwandeln.
   *
   * @param dateString the date string
   * @return the date
   * @throws ParseException the parse exception
   */
  public static Date parseByIsoTimestampFormat(String dateString) throws ParseException
  {
    return isoTimestampFormat.get().parse(dateString);
  }

  /**
   * Synchonisierte Methode um das die Methode toPattern von SimpleFormate aufzurufen.
   *
   * @return the string
   */
  public static String toPatternByIsoDateFormat()
  {
    return isoDateFormat.get().toPattern();
  }

  /**
   * Synchonisierte Methode um das die Methode parse von SimpleFormate aufzurufen.
   *
   * @param dateString the date string
   * @return the date
   * @throws ParseException the parse exception
   */
  public static Date parseByIsoXsdDateTimeFormat(String dateString) throws ParseException
  {
    return xsdDateTimeFormat.get().parse(dateString);
  }

  /**
   * Synchonisierte Methode um das Datum in das xsdDateTimeFormat String umzuwandeln.
   *
   * @param date the date
   * @return the string
   */
  public static String formatByXsdDateTimeFormat(java.util.Date date)
  {
    return xsdDateTimeFormat.get().format(date);
  }

  /**
   * Format date object to iso timestamp.
   *
   * @param obj the obj
   * @return the string
   */
  public static String formatDateObjectToIsoTimestamp(Object obj)
  {
    if (obj == null) {
      return null;
    }
    if (obj instanceof String) {
      return (String) obj;
    }
    Date d = objectToDate(obj);
    if (d == null) {
      return null;
    }
    return formatByIsoTimestampFormat(d);
  }

  /**
   * Parses the iso timestamp to date.
   *
   * @param dateString the date string
   * @return the date
   */
  public static Date parseIsoTimestampToDate(String dateString)
  {
    if (dateString == null) {
      return null;
    }
    try {
      // return isoTimestampFormat.parse(dateString);
      return parseByIsoTimestampFormat(dateString);
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Parses the iso day to date.
   *
   * @param dateString the date string
   * @return the date
   */
  public static Date parseIsoDayToDate(String dateString)
  {
    if (dateString == null) {
      return null;
    }
    try {
      // return isoDayFormat.parse(dateString);
      return parseByIsoDayFormat(dateString);
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Parses the iso date to date.
   *
   * @param dateString the date string
   * @return the date
   */
  public static Date parseIsoDateToDate(String dateString)
  {
    if (dateString == null) {
      return null;
    }
    try {
      if (dateString.length() == "yyyy-MM-dd HH:mm:ss:SSS".length()) {
        return isoTimestampFormat.get().parse(dateString);
      }
      if (dateString.length() == "yyyy-MM-dd HH:mm".length()) {
        return isoDateFormat.get().parse(dateString);
      }
      return isoDayFormat.get().parse(dateString);
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * replace or remove special character and converts to uppercase accepts iso8859-1 characters.
   *
   * @param input the input
   * @return the string
   */
  public static String canonifyName(String input)
  {
    String result;
    result = input.toUpperCase();
    result = result.replaceAll("Ä", "ae");
    result = result.replaceAll("Ö", "OE");
    result = result.replaceAll("Ü", "UE");
    result = result.replaceAll("ß", "SS");
    result = result.replaceAll("Ñ", "N");
    // result = result.replaceAll("?", "Z"); //in iso8859-1 nicht codierbar
    // result = result.replaceAll("[???]", "R");//in iso8859-1 nicht codierbar
    // result = result.replaceAll("[????]", "S");//in iso8859-1 nicht codierbar
    result = result.replaceAll("[Ç]", "C");
    result = result.replaceAll("[ÁÀÂÃ]", "A");
    result = result.replaceAll("[ÉÈÊË]", "E");
    result = result.replaceAll("[ÍÌÎÏ]", "I");
    result = result.replaceAll("[ÓÒÔÕ]", "O");
    result = result.replaceAll("[ÚÙÛ]", "U");
    result = result.replaceAll("[^A-Z0-9\\-_]", "");
    return result;
  }

  /**
   * replace or remove special character accepts iso8859-1 characters converts whitespace to _.
   *
   * @param input the input
   * @return the string
   */
  public static String canonifyNamePreserveCase(String input)
  {
    if (input == null) {
      return null;
    }

    String result = input;
    result = result.replaceAll("Ä", "Ae");
    result = result.replaceAll("Ö", "Oe");
    result = result.replaceAll("Ü", "Ue");
    result = result.replaceAll("ß", "ss");
    result = result.replaceAll("Ñ", "N");
    // result = result.replaceAll("?", "Z"); //in iso8859-1 nicht codierbar
    // result = result.replaceAll("[???]", "R");//in iso8859-1 nicht codierbar
    // result = result.replaceAll("[????]", "S");//in iso8859-1 nicht codierbar
    result = result.replaceAll("[Ç]", "C");
    result = result.replaceAll("[ÁÀÂÃ]", "A");
    result = result.replaceAll("[ÉÈÊË]", "E");
    result = result.replaceAll("[ÍÌÎÏ]", "I");
    result = result.replaceAll("[ÓÒÔÕ]", "O");
    result = result.replaceAll("[ÚÙÛ]", "U");

    result = result.replaceAll("ä", "ae");
    result = result.replaceAll("ö", "oe");
    result = result.replaceAll("ü", "ue");
    result = result.replaceAll("ñ", "n");
    result = result.replaceAll("[ç]", "c");
    result = result.replaceAll("[áàâã]", "a");
    result = result.replaceAll("[éèêë]", "e");
    result = result.replaceAll("[íìîï]", "i");
    result = result.replaceAll("[óòôõ]", "o");
    result = result.replaceAll("[úùû]", "u");
    result = result.replaceAll(" ", "_");

    result = result.replaceAll("[^A-Za-z0-9\\-_]", "");
    return result;
  }

  /**
   * Render html input.
   *
   * @param key the key
   * @param value the value
   * @return the string
   */
  public static String renderHtmlInput(String key, String value)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("<input type=\"hidden\" name=\"");
    sb.append(key);
    sb.append("\" value=\"");
    sb.append(value);
    sb.append("\">\n");
    return sb.toString();
  }

  /**
   * First to upper case.
   *
   * @param str the str
   * @return the string
   */
  public static String firstToUpperCase(String str)
  {
    if (StringUtils.isBlank(str) == true) {
      return str;
    }
    return StringUtils.capitalize(str.trim());
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

  /**
   * convert delimited string to map.
   *
   * @param str String to Convert
   * @param d1 Delimiter zwischen den EintrÃ¤gen
   * @param d2 Delimiter zwischen den key=value Paaren
   * @return the map
   */
  public static Map<String, String> delimitedStringToMap(String str, String d1, String d2)
  {
    String[] fields = StringUtils.split(str, d1);
    Map<String, String> map = new HashMap<String, String>();
    for (String fld : fields) {
      String key = StringUtils.substringBefore(fld, d2);
      String val = StringUtils.substringAfter(fld, d2);
      map.put(key, val);
    }
    return map;
  }

  /**
   * convert map to delimited string.
   *
   * @param map Map<String, String> to Convert
   * @param d1 Delimiter zwischen den EintrÃ¤gen
   * @param d2 elimiter zwischen den key=value Paaren
   * @return the string
   */
  public static String mapToDelimitedString(Map<String, String> map, String d1, String d2)
  {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (String key : map.keySet()) {
      if (first != true) {
        sb.append(d1);
      }
      sb.append(key);
      sb.append(d2);
      sb.append(map.get(key));
      first = false;
    }
    return sb.toString();
  }

  /**
   * String to array.
   *
   * @param str the str
   * @return the string[]
   */
  public static String[] stringToArray(String str)
  {
    return StringUtils.split(StringUtils.remove(str, ' '), ',');
  }

  /**
   * Analog zu Stringutils.abbreviate() einen String kÃ¼rzen, aber "UTF8-aware".
   * 
   * @param str Kann <code>null</code> sein.
   * @param maxWidth Maximale Breite, aber mindestens 4.
   * @return Nie <code>null</code>
   */
  public static String abbreviate(String str, int maxWidth)
  {
    Validate.isTrue(maxWidth >= 4, "maxWidth too small: ", maxWidth);

    String s = trimUtf8Exact(str, maxWidth - 3);
    if (StringUtils.equals(s, str) == false) {
      s = s + "...";
    }
    return s;
  }

  /**
   * Creates the amount.
   *
   * @param amount the amount
   * @return the big decimal
   */
  public static BigDecimal createAmount(double amount)
  {
    BigDecimal bd = new BigDecimal(amount);
    bd = bd.setScale(2, RoundingMode.HALF_UP);
    return bd;
  }

  /**
   * Convert an even list of arguments to a HashMap.
   *
   * @param <T> the generic type
   * @param ts must be even
   * @return the map
   */
  public static <T> Map<T, T> convertToMap(T... ts)
  {
    if (ts.length % 2 != 0) {
      throw new IllegalArgumentException("Converting args array to a map must be even");
    }
    Map<T, T> m = new HashMap<T, T>();
    for (int i = 0; i < ts.length; ++i) {
      T k = ts[i];
      T v = ts[++i];
      m.put(k, v);
    }
    return m;
  }
}
