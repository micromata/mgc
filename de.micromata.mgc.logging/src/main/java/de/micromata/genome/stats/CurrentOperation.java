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

package de.micromata.genome.stats;

import de.micromata.genome.logging.LogCategory;
import de.micromata.genome.logging.LoggingServiceManager;

/**
 * Wrapper for current operations.
 *
 * @author roger
 */
public class CurrentOperation
{

  /**
   * The user.
   */
  private String user;

  /**
   * The start time.
   */
  private long startTime;

  /**
   * The category.
   */
  private LogCategory category;

  /**
   * The point name.
   */
  private String pointName;

  /**
   * If not null called toString() to create a display object.
   */
  private Object displayObject;

  /**
   * Instantiates a new current operation.
   *
   * @param user the user
   * @param category the category
   * @param pointName the point name
   * @param displayObject the display object
   */
  public CurrentOperation(String user, LogCategory category, String pointName, Object displayObject)
  {

    this.user = user;
    this.startTime = System.currentTimeMillis();
    this.category = category;
    this.pointName = pointName;
    this.displayObject = displayObject;
  }

  /**
   * Instantiates a new current operation.
   *
   * @param category the category
   * @param pointName the point name
   * @param displayObject the display object
   */
  public CurrentOperation(LogCategory category, String pointName, Object displayObject)
  {

    this.user = LoggingServiceManager.get().getLoggingContextService().getCurrentUserName();
    this.startTime = System.currentTimeMillis();
    this.category = category;
    this.pointName = pointName;
    this.displayObject = displayObject;
  }

  public String getUser()
  {
    return user;
  }

  public void setUser(String user)
  {
    this.user = user;
  }

  public long getStartTime()
  {
    return startTime;
  }

  public void setStartTime(long startTime)
  {
    this.startTime = startTime;
  }

  public LogCategory getCategory()
  {
    return category;
  }

  public void setCategory(LogCategory category)
  {
    this.category = category;
  }

  public String getPointName()
  {
    return pointName;
  }

  public void setPointName(String pointName)
  {
    this.pointName = pointName;
  }

  public Object getDisplayObject()
  {
    return displayObject;
  }

  public void setDisplayObject(Object displayObject)
  {
    this.displayObject = displayObject;
  }

}
