package de.micromata.genome.db.jpa.history.impl;

/**
 * Context sensitive Information of History entry.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface HistoryMetaInfo
{
  /**
   * should property ignored for history.
   * 
   * @param property
   * @return
   */
  boolean ignoreProperty(String property);

}
