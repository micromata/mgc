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

package de.micromata.genome.chronos.util;

import de.micromata.genome.chronos.Trigger;
import de.micromata.genome.chronos.spi.jdbc.SerializationUtil;

/**
 * Collection of utils to create Trigger.
 * 
 * @author roger@micromata.de
 * 
 */
public class TriggerJobUtils
{

  /**
   * Create a trigger on given trigger string representation.
   *
   * @param definition the definition
   * @return the trigger
   */
  public static Trigger createTriggerDefinition(final String definition)
  {
    if (definition.startsWith("+") == true) {
      return new DelayTrigger(definition);
    } else
      if (definition.startsWith("p") == true) {
      return new PeriodicalTrigger(definition);
    } else
        if (definition.startsWith("!") == true) {
      return new FixedTrigger(definition);
    } else
          if (definition.startsWith("<")) {
      return SerializationUtil.deserialize(definition, Trigger.class);
    } else {
      return new CronTrigger(definition);
    }
  }
}
