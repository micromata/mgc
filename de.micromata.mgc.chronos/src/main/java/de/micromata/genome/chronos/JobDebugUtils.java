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

package de.micromata.genome.chronos;

import java.util.Date;

import de.micromata.genome.util.types.Converter;

/**
 * Debug utils.
 *
 * @author roger
 */
public class JobDebugUtils
{

  /**
   * Date to string.
   *
   * @param d date
   * @return "null" if d is null, otherwise ISO timestamp
   */
  public static String dateToString(Date d)
  {
    if (d == null) {
      return "null";
    }
    return Converter.formatByIsoTimestampFormat(d);
  }

  /**
   * Date to string.
   *
   * @param d date
   * @return "null" if d is null, otherwise ISO timestamp
   */
  public static String dateToString(long d)
  {
    if (d < 10000) {
      return "InvalidLongDate(" + Long.toString(d) + ")";
    }
    return Converter.formatByIsoTimestampFormat(new Date(d));
  }

  /**
   * Trigger to string.
   *
   * @param trigger the trigger
   * @return the string
   */
  public static String triggerToString(Trigger trigger)
  {
    if (trigger == null) {
      return "NullTrigger";
    }
    return trigger.getClass().getSimpleName()
        + "["
        + trigger.getTriggerDefinition()
        + "] firing at "
        + dateToString(trigger.getInternalNextFireTime());
  }
}
