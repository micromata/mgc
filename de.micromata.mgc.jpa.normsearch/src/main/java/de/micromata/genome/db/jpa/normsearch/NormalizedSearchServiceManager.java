package de.micromata.genome.db.jpa.normsearch;

/**
 * Simple singleton DaoManager for NormalizedSearchDaoManager.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class NormalizedSearchServiceManager
{

  /**
   * The instance.
   */
  private static NormalizedSearchServiceManager INSTANCE = new NormalizedSearchServiceManager();

  /**
   * The normalized search dao.
   */
  private NormalizedSearchService normalizedSearchService = new NormalizedSearchServiceImpl();

  /**
   * Gets the.
   *
   * @return the normalized search dao manager
   */
  public static NormalizedSearchServiceManager get()
  {
    return INSTANCE;
  }

  /**
   * Gets the normalized search dao.
   *
   * @return the normalized search dao
   */
  public NormalizedSearchService getNormalizedSearchService()
  {
    return normalizedSearchService;
  }

  /**
   * Sets the normalized search dao.
   *
   * @param normalizedSearchDAO the new normalized search dao
   */
  public void setNormalizedSearchService(NormalizedSearchService normalizedSearchDAO)
  {
    this.normalizedSearchService = normalizedSearchDAO;
  }

}
