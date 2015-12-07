package de.micromata.genome.logging;

import javax.servlet.http.HttpServletRequest;

/**
 * The Interface LogConfigurationWithHistoryDAO.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface LogConfigurationWithHistoryDAO
{
  /**
   * Sets the attributes for /AttrHistoryAjax.action
   * 
   * @param req
   */
  public void setHistoryAjaxActionAttributes(HttpServletRequest req);
}
