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

import org.apache.commons.lang3.ArrayUtils;

/**
 * Exception, die strukturiert geloggt wird.
 *
 * @author roger
 */
public class LoggedRuntimeException extends LogAttributeRuntimeException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -8526594930837970168L;

  /**
   * Instantiates a new logged runtime exception.
   *
   * @param loglevel the loglevel
   * @param category the category
   * @param message the message
   * @param attributes the attributes
   */
  public LoggedRuntimeException(LogLevel loglevel, LogCategory category, String message, LogAttribute... attributes)
  {
    super(message, null, true, attributes);
    writeLogMsg(loglevel, category, message, null, attributes);
  }

  /**
   * Instantiates a new logged runtime exception.
   *
   * @param cause the cause
   * @param loglevel the loglevel
   * @param category the category
   * @param message the message
   * @param attributes the attributes
   */
  public LoggedRuntimeException(Throwable cause, LogLevel loglevel, LogCategory category, String message, LogAttribute... attributes)
  {
    super(message, cause, true, attributes);
    writeLogMsg(loglevel, category, message, cause, attributes);
  }

  /**
   * Write log msg.
   *
   * @param log the log
   * @param category the category
   * @param message the message
   * @param cause the cause
   * @param attributes the attributes
   */
  private void writeLogMsg(LogLevel log, LogCategory category, String message, Throwable cause, LogAttribute[] attributes)
  {
    if (cause != null) {
      attributes = (LogAttribute[]) ArrayUtils.add(attributes, new LogExceptionAttribute(cause));
    }
    GLog.doLog(log, category, message, attributes);
  }
}
