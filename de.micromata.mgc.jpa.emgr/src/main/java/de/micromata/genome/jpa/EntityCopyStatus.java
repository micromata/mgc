package de.micromata.genome.jpa;

/**
 * Status of changes.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public enum EntityCopyStatus
{
  /**
   * No changes
   */
  NONE(0),
  /**
   * Minor - business irrelevant - changes
   */
  MINOR(1),
  /**
   * Mayor changes.
   */
  MAJOR(2);

  private int status;

  private EntityCopyStatus(int status)
  {
    this.status = status;
  }

  public EntityCopyStatus combine(EntityCopyStatus other)
  {
    return status < other.status ? other : this;
  }
}
