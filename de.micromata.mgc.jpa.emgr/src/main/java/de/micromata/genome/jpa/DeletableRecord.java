package de.micromata.genome.jpa;

import java.io.Serializable;

/**
 * An record can be marked as deleted.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 * @param <PK>
 */
public interface DeletableRecord<PK extends Serializable>extends DbRecord<PK>
{

  /**
   * Is marked as deleted.
   *
   * @return true, if is deleted
   */
  boolean isDeleted();

  /**
   * Sets the deleted.
   *
   * @param deleted the new deleted
   */
  void setDeleted(boolean deleted);
}
