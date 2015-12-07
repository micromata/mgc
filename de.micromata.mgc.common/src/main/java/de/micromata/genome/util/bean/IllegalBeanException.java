/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   21.07.2006
// Copyright Micromata 21.07.2006
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.bean;

/**
 * The Class IllegalBeanException.
 *
 * @author roger@micromata.de
 */

public class IllegalBeanException extends IllegalArgumentException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -7373246436602310106L;

  /**
   * The bean.
   */
  private Object bean;

  /**
   * Instantiates a new illegal bean exception.
   *
   * @param bean the bean
   * @param message the message
   */
  public IllegalBeanException(Object bean, String message)
  {
    super(message);
    this.bean = bean;
  }

  public Object getBean()
  {
    return bean;
  }

  public void setBean(Object bean)
  {
    this.bean = bean;
  }
}
