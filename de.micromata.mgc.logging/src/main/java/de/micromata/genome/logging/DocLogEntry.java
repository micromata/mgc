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

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Java representation for a documented log entry.
 *
 * @author roger@micromata.de
 */
public class DocLogEntry
{

  /**
   * The reason.
   */
  private String reason;

  /**
   * The action.
   */
  private String action;

  /**
   * The domain.
   */
  private String domain;

  /**
   * The category.
   */
  private String category;

  /**
   * The level.
   */
  private String level;

  /**
   * The const message.
   */
  private String constMessage;

  /**
   * The message.
   */
  private String message;

  /**
   * The class name.
   */
  private String className;

  /**
   * The line.
   */
  private int line;

  /**
   * The no doc.
   */
  private boolean noDoc = false;

  /**
   * Instantiates a new doc log entry.
   */
  public DocLogEntry()
  {
  }

  /**
   * Instantiates a new doc log entry.
   *
   * @param level the level
   * @param category the category
   */
  public DocLogEntry(String level, String category)
  {
    this.level = level;
    this.category = category;
  }

  public boolean isValid()
  {
    return StringUtils.isNotBlank(level) && StringUtils.isNotBlank(category) && StringUtils.isNotBlank(constMessage);

  }

  public boolean isDocumented()
  {
    return noDoc == false && isValid();
  }

  @Override
  public int hashCode()
  {
    return level.hashCode() + (getFqCat().hashCode() * 31);
  }

  public String getFqCat()
  {
    if (domain == null || domain.length() == 0) {
      return category;
    }
    return domain + "." + category;
  }

  @Override
  public boolean equals(Object other)
  {
    if ((other instanceof DocLogEntry) == false) {
      return false;
    }
    DocLogEntry dle = (DocLogEntry) other;
    return level.equals(dle.level) && ObjectUtils.equals(domain, dle.domain) && category.equals(dle.category);
  }

  @Override
  public String toString()
  {
    return className + ": " + line + "; " + level + "; " + domain + "." + category + "; " + constMessage;
  }

  public int getLine()
  {
    return line;
  }

  public void setLine(int line)
  {
    this.line = line;
  }

  public String getCategory()
  {
    return category;
  }

  public void setCategory(String category)
  {
    this.category = category;
  }

  public String getClassName()
  {
    return className;
  }

  public void setClassName(String className)
  {
    this.className = className;
  }

  public String getLevel()
  {
    return level;
  }

  public void setLevel(String level)
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

  public String getAction()
  {
    return action;
  }

  public void setAction(String action)
  {
    this.action = action;
  }

  public String getReason()
  {
    return reason;
  }

  public void setReason(String reason)
  {
    this.reason = reason;
  }

  public String getConstMessage()
  {
    return constMessage;
  }

  public void setConstMessage(String constMessage)
  {
    this.constMessage = constMessage;
  }

  public String getDomain()
  {
    return domain;
  }

  public void setDomain(String domain)
  {
    this.domain = domain;
  }

  public boolean isNoDoc()
  {
    return noDoc;
  }

  public void setNoDoc(boolean noDoc)
  {
    this.noDoc = noDoc;
  }

}
