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
