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

import de.micromata.genome.util.types.Pair;
import java.util.ArrayList;
import java.util.List;

/**
 * Fuer einen Scope pushed ein LogAttribute in den LoggingContext Wird vor einem try / finally block initialisiert und innerhalb des
 * finally-Blocks restore() aufgerufen.
 * 
 * ACHTUNG besser NICHT im finally restore aufrufen, sondern im Falle einer Exception drinnen lassen. damit ggf
 *
 * @author roger@micromata.de
 * @since 1.2.1 supports multiple pushes.
 */
public class ScopedLogContextAttribute implements AutoCloseable
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
    lastLog = LoggingContext.getEnsureContext().getAttributes().get(attribute.getType());
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
    LogAttribute llastLog = LoggingContext.getEnsureContext().getAttributes().get(attribute.getType());
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

  @Override
  public void close()
  {
    restore();
  }
}
