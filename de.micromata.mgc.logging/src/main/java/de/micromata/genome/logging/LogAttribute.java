/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   03.07.2006
// Copyright Micromata 03.07.2006
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * Ein Log-Attribute. Der Type wird durch LogAttributeType festgelegt.
 * 
 * @author roger@micromata.de
 * 
 */
public class LogAttribute implements Serializable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 4447381062765214393L;

  /**
   * The type.
   */
  private LogAttributeType type;

  /**
   * The value.
   */
  private String value;

  /**
   * Instantiates a new log attribute.
   */
  protected LogAttribute()
  {
  }

  /**
   * Instantiates a new log attribute.
   *
   * @param type the type
   * @param value the value
   */
  public LogAttribute(LogAttributeType type, String value)
  {
    Validate.notNull(type);
    this.type = type;
    this.value = value;
  }

  public final boolean isSearchKey()
  {
    return type.isSearchKey();
  }

  /**
   * Shorten.
   *
   * @param val the val
   * @param size the size
   * @return the string
   */
  public static String shorten(String val, int size)
  {
    if (val == null) {
      return StringUtils.EMPTY;
    }
    return StringUtils.abbreviate(val, size);
  }

  public LogAttributeType getType()
  {
    return type;
  }

  @Override
  public int hashCode()
  {
    return ObjectUtils.hashCode(type);// don't because has to be equals * 17 + ObjectUtils.hashCode(value);
  }

  /**
   * Prueft nur ob type identisch ist.
   */
  @Override
  public boolean equals(Object other)
  {
    if ((other instanceof LogAttribute) == false) {
      return false;
    }
    LogAttribute lo = (LogAttribute) other;
    return type == lo.getType();
  }

  public final void setType(LogAttributeType type)
  {
    this.type = type;
  }

  public final String getTypeName()
  {
    return type.name();
  }

  public String getValue()
  {
    return value;
  }

  /**
   * Used to overwrite in dynamic values, in cases of logging more if level is error.
   * 
   * If orginal value is not empty, pass always value.
   *
   * @param lwe log to write.
   * @return the value to write
   */
  public String getValueToWrite(LogWriteEntry lwe)
  {
    return getValue();
  }

  public String getEscapedValue()
  {
    return StringEscapeUtils.escapeXml(value);
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  @Override
  public String toString()
  {
    return type.name() + "|" + value;
  }

  /**
   * helper to render a map to a string.
   *
   * @param reqMap the req map
   * @return the request dump
   */
  public static String getRequestDump(Map<String, String> reqMap)
  {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, String> e : reqMap.entrySet()) {
      sb.append("|").append(e.getKey()).append("=").append(e.getValue()).append("\n");
    }
    return sb.toString();
  }

}
