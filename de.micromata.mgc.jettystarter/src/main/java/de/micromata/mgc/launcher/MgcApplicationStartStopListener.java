package de.micromata.mgc.launcher;

import de.micromata.genome.util.validation.ValMessage;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@FunctionalInterface
public interface MgcApplicationStartStopListener
{
  void listen(MgcApplication<?> application, MgcApplicationStartStopStatus status, ValMessage msg);
}
