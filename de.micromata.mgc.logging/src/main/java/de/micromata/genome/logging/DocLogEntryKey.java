package de.micromata.genome.logging;

import org.apache.commons.lang.StringUtils;

/**
 * The Class DocLogEntryKey.
 *
 * @author roger
 */
public class DocLogEntryKey
{

  /**
   * The key.
   */
  private String key;

  /**
   * The message.
   */
  private String message;

  /**
   * Strip const message.
   *
   * @param msg the msg
   * @return the string
   */
  private String stripConstMessage(String msg)
  {
    String smsg = StringUtils.substring(msg, 0, 10);
    int idx = StringUtils.indexOfAny(smsg, ",:.(");
    if (idx == -1) {
      return smsg;
    }
    return smsg.substring(0, idx);
  }

  /**
   * Instantiates a new doc log entry key.
   *
   * @param entry the entry
   */
  public DocLogEntryKey(DocLogEntry entry)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(entry.getLevel());
    if (entry.getDomain() != null) {
      sb.append(".").append(entry.getDomain());
    }
    sb.append(".").append(entry.getCategory());
    String msg = entry.getConstMessage();
    message = msg;
    String smsg = stripConstMessage(msg);
    sb.append(".").append(smsg);
    key = sb.toString();
  }

  /**
   * Instantiates a new doc log entry key.
   *
   * @param entry the entry
   */
  public DocLogEntryKey(LogEntry entry)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(entry.getLogLevel().name());
    sb.append(".").append(entry.getCategory());
    message = entry.getMessage();
    String smsg = stripConstMessage(entry.getMessage());
    sb.append(".").append(smsg);
    key = sb.toString();
  }

  @Override
  public boolean equals(Object obj)
  {
    if ((obj instanceof DocLogEntryKey) == false) {
      return false;
    }
    return key.equals(((DocLogEntryKey) obj).getKey());
  }

  @Override
  public int hashCode()
  {
    return key.hashCode();
  }

  @Override
  public String toString()
  {
    return key;
  }

  public String getKey()
  {
    return key;
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage(String message)
  {
    this.message = message;
  }

}
