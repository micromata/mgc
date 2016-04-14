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
