/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   24.01.2008
// Copyright Micromata 24.01.2008
//
/////////////////////////////////////////////////////////////////////////////
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
