package de.micromata.genome.db.jpa.history.api;

import de.micromata.genome.dao.AbstractModuleDaoManager;
import de.micromata.genome.dao.StaticDaoManager;
import de.micromata.genome.db.jpa.history.impl.HistoryServiceImpl;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogCategory;

/**
 * The Class HistoryServiceManager.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class HistoryServiceManager implements AbstractModuleDaoManager
{

  /**
   * The instance.
   */
  protected static HistoryServiceManager INSTANCE = StaticDaoManager.get().getModuleDaoDomainManager() // NOSONAR "Malicious code vulnerability" Framework  
      .initNewModuleDaoManager(new HistoryServiceManager());
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
    return StaticDaoManager.get().getModuleDaoDomainManager()
        .getDaoManager(DAODOMAINNAME, HistoryServiceManager.class, INSTANCE);
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

  @Override
  public LogCategory getDaoManagerLogCategory()
  {
    return GenomeLogCategory.Database;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public String getDomain()
  {
    return DAODOMAINNAME;
  }

}
