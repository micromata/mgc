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

package de.micromata.mgc.javafx.launcher.gui.lf5;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.lf5.Log4JLogRecord;
import org.apache.log4j.lf5.LogLevel;
import org.apache.log4j.lf5.LogLevelFormatException;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MgcLf5Appender extends AppenderSkeleton
{
  public static boolean initialized = false;

  public MgcLf5Appender()
  {
    initialized = true;
  }

  @Override
  public void close()
  {
    initialized = false;
  }

  @Override
  public boolean requiresLayout()
  {
    return false;
  }

  @Override
  protected void append(LoggingEvent event)
  {
    // Retrieve the information from the log4j LoggingEvent.
    String category = event.getLoggerName();
    String logMessage = event.getRenderedMessage();
    String nestedDiagnosticContext = event.getNDC();
    String threadDescription = event.getThreadName();
    String level = event.getLevel().toString();
    long time = event.timeStamp;
    LocationInfo locationInfo = event.getLocationInformation();

    // Add the logging event information to a LogRecord
    Log4JLogRecord record = new Log4JLogRecord();

    record.setCategory(category);
    record.setMessage(logMessage);
    record.setLocation(locationInfo.fullInfo);
    record.setMillis(time);
    record.setThreadDescription(threadDescription);

    if (nestedDiagnosticContext != null) {
      record.setNDC(nestedDiagnosticContext);
    } else {
      record.setNDC("");
    }

    if (event.getThrowableInformation() != null) {
      record.setThrownStackTrace(event.getThrowableInformation());
    }

    try {
      record.setLevel(LogLevel.valueOf(level));
    } catch (LogLevelFormatException e) {
      // If the priority level doesn't match one of the predefined
      // log levels, then set the level to warning.
      record.setLevel(LogLevel.WARN);
    }
    MgcLogBrokerMonitor monitor = Lf5MainWindowController.getMonitor();
    if (monitor != null) {
      monitor.addMessage(record);
    }
  }

}
