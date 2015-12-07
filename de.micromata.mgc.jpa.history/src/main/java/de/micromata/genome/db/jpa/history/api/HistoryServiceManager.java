package de.micromata.genome.db.jpa.history.api;

import de.micromata.genome.db.jpa.history.impl.HistoryServiceImpl;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogCategory;

/**
 * The Class HistoryServiceManager.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class HistoryServiceManager
{

  /**
   * The instance.
   */
  protected static HistoryServiceManager INSTANCE = new HistoryServiceManager();
  /**
   * The Constant DAODOMAINNAME.
   */
  public static final String DAODOMAINNAME = "GMN_JPA_HISTORY";

  /**
   * The history service.
   */
  private HistoryService historyService = new HistoryServiceImpl();

  /**
   * Gets the service manager.
   * 
   * @return the vls product model dao manager
   */
  public static HistoryServiceManager get()
  {
    return INSTANCE;
  }

  /**
   * Gets the history service.
   *
   * @return the history service
   */
  public HistoryService getHistoryService()
  {
    return historyService;
  }

  /**
   * Sets the history service.
   *
   * @param historyService the new history service
   */
  public void setHistoryService(HistoryService historyService)
  {
    this.historyService = historyService;
  }

  /**
   * {@inheritDoc}
   *
   */

  public LogCategory getDaoManagerLogCategory()
  {
    return GenomeLogCategory.Database;
  }

  /**
   * {@inheritDoc}
   *
   */

  public String getDomain()
  {
    return DAODOMAINNAME;
  }

}
