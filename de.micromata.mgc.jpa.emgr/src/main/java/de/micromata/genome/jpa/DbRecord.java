package de.micromata.genome.jpa;

import java.io.Serializable;

import javax.persistence.Transient;

import org.apache.log4j.Logger;

/**
 * Marker-Interface for all Entities with a primary key.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface DbRecord<PK extends Serializable>extends Serializable
{
  static final Logger LOG = Logger.getLogger(DbRecord.class);

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

  default Long getPkAsLong()
  {
    PK pk = getPk();
    if (pk == null) {
      return null;
    }
    if (pk instanceof Number) {
      return ((Number) pk).longValue();
    }
    LOG.error("Cannot cast pk to Long: " + getClass().getName() + "; " + pk.getClass());
    return null;
  }
}
