/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   29.06.2007
// Copyright Micromata 29.06.2007
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.util.types.Pair;

/**
 * Fuer einen Scope pushed ein LogAttribute in den LoggingContext Wird vor einem try / finally block initialisiert und innerhalb des
 * finally-Blocks restore() aufgerufen.
 * 
 * ACHTUNG besser NICHT im finally restore aufrufen, sondern im Falle einer Exception drinnen lassen. damit ggf
 *
 * @author roger@micromata.de
 * @since 1.2.1 supports multiple pushes.
 */
public class ScopedLogContextAttribute
{

  /**
   * The last log.
   */
  private LogAttribute lastLog;

  /**
   * The last type.
   */
  private LogAttributeType lastType;

  /**
   * The more pushed.
   */
  private List<Pair<LogAttribute, LogAttributeType>> morePushed;

  /**
   * Instantiates a new scoped log context attribute.
   */
  public ScopedLogContextAttribute()
  {

  }

  /**
   * Instantiates a new scoped log context attribute.
   *
   * @param type the type
   * @param value the value
   */
  public ScopedLogContextAttribute(LogAttributeType type, String value)
  {
    this(new LogAttribute(type, value));
  }

  /**
   * Instantiates a new scoped log context attribute.
   *
   * @param attribute the attribute
   */
  public ScopedLogContextAttribute(LogAttribute attribute)
  {
    lastLog = LoggingContext.getEnsureContext().getAttributes().get(attribute.getType().name());
    lastType = attribute.getType();
    LoggingContext.pushLogAttribute(attribute);
  }

  /**
   * Push.
   *
   * @param attribute the attribute
   */
  public void push(LogAttribute attribute)
  {
    if (morePushed == null) {
      morePushed = new ArrayList<Pair<LogAttribute, LogAttributeType>>();
    }
    LogAttribute llastLog = LoggingContext.getEnsureContext().getAttributes().get(attribute.getType().name());
    morePushed.add(Pair.make(llastLog, attribute.getType()));
    LoggingContext.pushLogAttribute(attribute);
  }

  /**
   * Restore.
   */
  public void restore()
  {
    // restore in revert order
    if (morePushed != null) {
      for (int i = morePushed.size() - 1; i >= 0; --i) {
        Pair<LogAttribute, LogAttributeType> p = morePushed.get(i);
        restore(p.getFirst(), p.getSecond());
      }
    }
    restore(lastLog, lastType);
    morePushed = null;
    lastLog = null;
    lastType = null;
  }

  /**
   * Restore.
   *
   * @param tlastLog the tlast log
   * @param tlastType the tlast type
   */
  private void restore(LogAttribute tlastLog, LogAttributeType tlastType)
  {
    if (tlastLog == null) {
      if (tlastType != null) {
        LoggingContext.popLogAttribute(tlastType);
      }
      return;
    }
    LoggingContext.pushLogAttribute(tlastLog);
  }
}
