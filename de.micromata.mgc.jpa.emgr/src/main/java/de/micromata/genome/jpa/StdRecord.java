package de.micromata.genome.jpa;

import java.io.Serializable;
import java.util.Date;

/**
 * Default interface for a JPA entity.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface StdRecord<PK extends Serializable>extends DbRecord<PK>
{

  /**
   * sets the modified by.
   *
   * @param modifiedBy the new modified by
   */
  public void setModifiedBy(final String modifiedBy);

  /**
   * get the modified by.
   *
   * @param modifiedAt the new modified at
   */
  public void setModifiedAt(final Date modifiedAt);

  /**
   * set the modified at timestamp.
   *
   * @param createdAt the new created at
   */
  public void setCreatedAt(final Date createdAt);

  /**
   * {@inheritDoc}
   *
   */

  public Integer getUpdateCounter();

  /**
   * set the update counter.
   *
   * @param updateCounter the new update counter
   */
  public void setUpdateCounter(final Integer updateCounter);

  /**
   * {@inheritDoc}
   *
   */

  public String getCreatedBy();

  /**
   * {@inheritDoc}
   *
   */

  public void setCreatedBy(final String createdBy);

  /**
   * {@inheritDoc}
   *
   */

  public String getModifiedBy();

  /**
   * {@inheritDoc}
   *
   */

  public Date getModifiedAt();

  /**
   * {@inheritDoc}
   *
   */

  public Date getCreatedAt();

}
