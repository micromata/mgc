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

package de.micromata.genome.logging.events;

import java.util.Map;

import de.micromata.genome.logging.LogAttributeType;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.util.event.MgcFilterEventImpl;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LogRegisteredLogAttributesChangedEvent extends MgcFilterEventImpl<LogWriteEntry> implements LoggingEvent
{

  Map<String, LogAttributeType> logAttributes;

  public LogRegisteredLogAttributesChangedEvent(Map<String, LogAttributeType> logAttributes)
  {
    this.logAttributes = logAttributes;
  }

  public Map<String, LogAttributeType> getLogAttributes()
  {
    return logAttributes;
  }

}
