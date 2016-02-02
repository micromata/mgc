package de.micromata.genome.db.jpa.normsearch;

/**
 * Simple singleton DaoManager for NormalizedSearchDaoManager.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class NormalizedSearchDaoManager
{

  /**
   * The instance.
   */
  private static NormalizedSearchDaoManager INSTANCE = new NormalizedSearchDaoManager();

  /**
   * The normalized search dao.
   */
  private NormalizedSearchDAOImpl normalizedSearchDAO = new NormalizedSearchDAOImpl();

  /**
   * Gets the.
   *
   * @return the normalized search dao manager
   */
  public static NormalizedSearchDaoManager get()
  {
    return INSTANCE;
  }

  /**
   * Gets the normalized search dao.
   *
   * @return the normalized search dao
   */
  public NormalizedSearchDAOImpl getNormalizedSearchDAO()
  {
    return normalizedSearchDAO;
  }

  /**
   * Sets the normalized search dao.
   *
   * @param normalizedSearchDAO the new normalized search dao
   */
  public void setNormalizedSearchDAO(NormalizedSearchDAOImpl normalizedSearchDAO)
  {
    this.normalizedSearchDAO = normalizedSearchDAO;
  }

}
