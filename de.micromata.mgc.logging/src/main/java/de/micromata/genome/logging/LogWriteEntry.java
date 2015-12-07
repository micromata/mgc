/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   08.03.2007
// Copyright Micromata 08.03.2007
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper fuer einen Log-Eintrag.
 * 
 * @author roger@micromata.de
 */
public class LogWriteEntry implements Serializable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -5234050492871751919L;

  /**
   * if 0, use the db time to set timestamp.
   */
  private long timestamp;

  /**
   * The level.
   */
  private LogLevel level;

  /**
   * The category.
   */
  private String category;

  /**
   * The message.
   */
  private String message;

  /**
   * The attributes.
   */
  private List<LogAttribute> attributes;

  /**
   * After inserting an LogWriteEntry this value should be updated.
   * <p/>
   * 
   * For database this will be probably the primary Key as Long
   */
  private Object logEntryIndex;

  public Object getLogEntryIndex()
  {
    return logEntryIndex;
  }

  public void setLogEntryIndex(Object logEntryIndex)
  {
    this.logEntryIndex = logEntryIndex;
  }

  /**
   * Instantiates a new log write entry.
   */
  public LogWriteEntry()
  {

  }

  /**
   * Instantiates a new log write entry.
   *
   * @param level the level
   * @param category the category
   * @param message the message
   * @param attributes the attributes
   */
  public LogWriteEntry(LogLevel level, String category, String message, List<LogAttribute> attributes)
  {
    this.level = level;
    this.category = category;
    this.message = message;
    this.attributes = attributes;
  }

  /**
   * As list.
   *
   * @param attributes the attributes
   * @return the list
   */
  private static List<LogAttribute> asList(LogAttribute[] attributes)
  {
    List<LogAttribute> ret = new ArrayList<LogAttribute>(attributes.length);
    for (int i = 0; i < attributes.length; ++i) {
      ret.add(attributes[i]);
    }
    return ret;
  }

  /**
   * Instantiates a new log write entry.
   *
   * @param level the level
   * @param category the category
   * @param message the message
   * @param attributes the attributes
   */
  public LogWriteEntry(LogLevel level, String category, String message, LogAttribute[] attributes)
  {
    this(level, category, message, asList(attributes));
  }

  /**
   * Push attribute.
   *
   * @param le the le
   */
  public void pushAttribute(LogAttribute le)
  {
    if (le == null) {
      return;
    }
    for (LogAttribute a : attributes) {
      if (a.getType().name().equals(le.getType().name()) == true) {
        return;
      }
    }
    attributes.add(le);
  }

  /**
   * Ensure unique attributes.
   */
  public void ensureUniqueAttributes()
  {
    Map<String, LogAttribute> tmp = new HashMap<String, LogAttribute>(attributes.size());
    for (LogAttribute la : attributes) {
      tmp.put(la.getTypeName(), la);
    }
    attributes.clear();
    attributes.addAll(tmp.values());
  }

  public List<LogAttribute> getAttributes()
  {
    return attributes;
  }

  /**
   * Gets the attribute by type.
   *
   * @param type the type
   * @return the attribute by type
   */
  public LogAttribute getAttributeByType(LogAttributeType type)
  {
    if (attributes == null) {
      return null;
    }
    if (type == null) {
      return null;
    }

    for (LogAttribute la : attributes) {
      if (la.getType().name().equals(type.name()) == true) {
        return la;
      }
    }
    return null;
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(timestamp).append("|").append(level).append("|").append(message);
    if (attributes != null) {
      for (LogAttribute la : attributes) {
        sb.append(";").append(la.getTypeName()).append(": ").append(la.getValueToWrite(this));
      }
    }
    return sb.toString();
  }

  public void setAttributes(List<LogAttribute> attributes)
  {
    this.attributes = attributes;
  }

  public String getCategory()
  {
    return category;
  }

  public void setCategory(String category)
  {
    this.category = category;
  }

  public LogLevel getLevel()
  {
    return level;
  }

  public void setLevel(LogLevel level)
  {
    this.level = level;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage(String message)
  {
    this.message = message;
  }

  public long getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(long timestamp)
  {
    this.timestamp = timestamp;
  }

}
