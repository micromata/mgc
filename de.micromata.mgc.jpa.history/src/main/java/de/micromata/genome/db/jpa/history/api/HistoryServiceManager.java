//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

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
