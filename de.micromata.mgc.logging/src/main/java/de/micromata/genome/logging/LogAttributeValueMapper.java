/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   31.07.2006
// Copyright Micromata 31.07.2006
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.types.Pair;

/**
 * fuer DB-Logger schreibt Value in short oder long value.
 *
 * @author roger
 */
public interface LogAttributeValueMapper
{

  /**
   * The Constant SHORT_VALUE_MAXSIZE.
   */
  public static final short SHORT_VALUE_MAXSIZE = 40 - 1;

  /**
   * The Constant LONG_VALUE_MAXSIZE.
   */
  public static final short LONG_VALUE_MAXSIZE = 4000 - 1;

  /**
   * return value for short (first) and long (second) db field.
   *
   * @param value the value
   * @return the pair
   */
  public Pair<String, String> mapAttributeValue(String value);

  /**
   * The Class ShortValueMapper.
   */
  public static class ShortValueMapper implements LogAttributeValueMapper
  {

    @Override
    public Pair<String, String> mapAttributeValue(String value)
    {
      return new Pair<String, String>(LogAttribute.shorten(value, SHORT_VALUE_MAXSIZE), null);
    }
  }

  /**
   * The Class LongValueMapper.
   */
  public static class LongValueMapper implements LogAttributeValueMapper
  {
    @Override
    public Pair<String, String> mapAttributeValue(String value)
    {
      // return new Pair<String, String>(null, StringUtils.substring(value, 0, LONG_VALUE_MAXSIZE));
      return new Pair<String, String>(null, value);
    }
  }

  /**
   * The Class BothValueMapper.
   */
  public static class BothValueMapper implements LogAttributeValueMapper
  {
    @Override
    public Pair<String, String> mapAttributeValue(String value)
    {
      String sval = LogAttribute.shorten(value, SHORT_VALUE_MAXSIZE);
      if (StringUtils.defaultString(value).length() <= SHORT_VALUE_MAXSIZE) {
        value = null;
      }
      return new Pair<String, String>(sval, value);
    }
  }

  /**
   * The Constant shortValueMapper.
   */
  public static final LogAttributeValueMapper shortValueMapper = new ShortValueMapper();

  /**
   * The Constant longValueMapper.
   */
  public static final LogAttributeValueMapper longValueMapper = new LongValueMapper();

  /**
   * The Constant bothValueMapper.
   */
  public static final LogAttributeValueMapper bothValueMapper = new BothValueMapper();
}
