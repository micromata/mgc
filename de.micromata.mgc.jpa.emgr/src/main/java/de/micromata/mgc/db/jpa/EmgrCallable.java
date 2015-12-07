package de.micromata.mgc.db.jpa;

/**
 * Scope for operating on an IEmgr.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @author Sebastian Hardt (s.hardt@micromata.de) Date: 10/10/13 Time: 1:46 PM
 */
public interface EmgrCallable<V, EMGR>
{

  /**
   * Call for using a jpa.
   *
   * @param emgr the emgr
   * @return the v
   */
  V call(final EMGR emgr);
}
