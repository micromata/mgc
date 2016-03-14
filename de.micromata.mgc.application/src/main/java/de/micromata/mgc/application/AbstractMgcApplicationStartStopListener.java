package de.micromata.mgc.application;

import de.micromata.genome.util.event.MgcEventListener;

/**
 * The listener interface for receiving standardMgcApplicationStartStop events. The class that is interested in
 * processing a standardMgcApplicationStartStop event implements this interface, and the object created with that class
 * is registered with a component using the component's <code>addStandardMgcApplicationStartStopListener<code> method.
 * When the standardMgcApplicationStartStop event occurs, that object's appropriate method is invoked.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public abstract class AbstractMgcApplicationStartStopListener implements MgcEventListener<MgcApplicationStartStopEvent>
{

}
