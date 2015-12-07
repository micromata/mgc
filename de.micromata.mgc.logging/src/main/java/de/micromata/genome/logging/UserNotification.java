/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   17.01.2009
// Copyright Micromata 17.01.2009
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

/**
 * Interface for a notification to user.
 * 
 * Either a directMessage or an I18N key can be set.
 * 
 * 
 * @author roger@micromata.de
 * 
 */
public interface UserNotification
{

  /**
   * Direct message to user.
   *
   * @return the direct message
   */
  public String getDirectMessage();

  /**
   * I18N-Key for user
   * 
   * @return
   */
  public String getI18NKey();

  /**
   * Only used if I18N will be used
   * 
   * @return may return null
   */
  public String[] getMessageArgs();

  /**
   * For web frameworks, etc. Place to show notification
   * 
   * @return
   */
  public String getFormName();
}
