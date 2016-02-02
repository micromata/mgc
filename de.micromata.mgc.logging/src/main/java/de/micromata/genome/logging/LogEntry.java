/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   09.07.2006
// Copyright Micromata 09.07.2006
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.micromata.genome.util.types.Converter;

/**
 * only used in admin web.
 *
 * @author roger
 */
public class LogEntry
{
  /**
   * After inserting an LogWriteEntry this value should be updated.
   * <p/>
   * 
   * For database this will be probably the primary Key as Long
   */
  private Object logEntryIndex;

  /**
   * The timestamp.
   */
  private long timestamp;

  /**
   * The log level.
   */
  private LogLevel logLevel;

  /**
   * The category.
   */
  private String category;

  /**
   * The message.
   */
  private String message;

  /**
   * The doc log entry.
   */
  DocLogEntry docLogEntry = null;

  /**
   * The attributes.
   */
  private List<LogAttribute> attributes = new ArrayList<LogAttribute>();

  /**
   * Instantiates a new log entry.
   */
  public LogEntry()
  {

  }

  /**
   * Instantiates a new log entry.
   *
   * @param other the other
   */
  public LogEntry(LogEntry other)
  {
    this.logEntryIndex = other.logEntryIndex;
    this.timestamp = other.timestamp;
    this.logLevel = other.logLevel;
    this.category = other.category;
    this.message = other.message;
    this.docLogEntry = other.docLogEntry;
    this.attributes = other.attributes;
  }

  /**
   * Instantiates a new log entry.
   *
   * @param logLevel the log level
   * @param category the category
   * @param msg the msg
   * @param attributes the attributes
   */
  public LogEntry(LogLevel logLevel, String category, String msg, LogAttribute... attributes)
  {
    this.logLevel = logLevel;
    this.category = category;
    this.message = msg;
    for (LogAttribute a : attributes) {
      this.attributes.add(a);
    }
  }

  public List<LogAttribute> getAttributes()
  {
    return attributes;
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

  public LogLevel getLogLevel()
  {
    return logLevel;
  }

  public void setLogLevel(LogLevel logLevel)
  {
    this.logLevel = logLevel;
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

  public String getTimestampString()
  {
    return Converter.formatByIsoTimestampFormat(new Date(timestamp));

  }

  public void setTimestamp(long timestamp)
  {
    this.timestamp = timestamp;
  }

  public String getLevelName()
  {
    return logLevel.name();
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
    for (LogAttribute la : attributes) {
      if (la.getType().name().equals(type.name())) {
        return la;
      }
    }
    return null;
  }

  /**
   * Gets the attribute by type.
   *
   * @param type the type
   * @return the attribute by type
   */
  public LogAttribute getAttributeByType(String type)
  {
    if (attributes == null) {
      return null;
    }
    for (LogAttribute la : attributes) {
      if (la.getType().name().equals(type) == true) {
        return la;
      }
    }
    return null;
  }

  public DocLogEntry getDocLogEntry()
  {
    return docLogEntry;
  }

  public void setDocLogEntry(DocLogEntry docLogEntry)
  {
    this.docLogEntry = docLogEntry;
  }

  public Object getLogEntryIndex()
  {
    return logEntryIndex;
  }

  public void setLogEntryIndex(Object logEntryIndex)
  {
    this.logEntryIndex = logEntryIndex;
  }

}
