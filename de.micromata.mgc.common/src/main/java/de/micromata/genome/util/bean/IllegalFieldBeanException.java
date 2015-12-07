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
 * The Class IllegalFieldBeanException.
 *
 * @author roger@micromata.de
 */
public class IllegalFieldBeanException extends IllegalBeanException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 5443964327302002070L;

  /**
   * The field name.
   */
  private String fieldName;

  /**
   * Instantiates a new illegal field bean exception.
   *
   * @param bean the bean
   * @param fieldName the field name
   * @param message the message
   */
  public IllegalFieldBeanException(Object bean, String fieldName, String message)
  {
    super(bean, message);
    this.fieldName = fieldName;
  }

  public String getFieldName()
  {
    return fieldName;
  }

  public void setFieldName(String fieldName)
  {
    this.fieldName = fieldName;
  }
}
