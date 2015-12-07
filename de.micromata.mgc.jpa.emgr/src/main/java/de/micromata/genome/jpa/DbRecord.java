package de.micromata.genome.jpa;

import java.io.Serializable;

import javax.persistence.Transient;

/**
 * Marker-Interface for all Entities with a primary key.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface DbRecord<PK extends Serializable>extends Serializable
{

  /**
   * get the PK of the record.
   *
   * @return the pk
   */
  @Transient
  public PK getPk();

  /**
   * Sets the PK of the record.
   *
   * @param pk the new pk
   */
  public void setPk(PK pk);
}
