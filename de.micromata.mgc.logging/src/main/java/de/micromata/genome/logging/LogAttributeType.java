// ///////////////////////////////////////////////////////////////////////////
//
// Project DHL-ParcelOnlinePostage
//
// Author roger@micromata.de
// Created 03.07.2006
// Copyright Micromata 03.07.2006
//
// ///////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

/**
 * has to be synchronized with db.
 *
 * @author roger
 */
public interface LogAttributeType
{

  /**
   * The Root log message.
   */
  LogAttributeType RootLogMessage = null;

  /**
   * Name.
   *
   * @return enumeration name
   */
  public String name();

  /**
   * Will be ignored, if this LogAttributeType is not a searchKey.
   * 
   * @return db column name. Return null if no column name is given
   */
  public String columnName();

  /**
   * Max value size.
   *
   * @return maximal column width. Will be ignored, if this LogAttributeType is not a searchKey.
   */
  public int maxValueSize();

  /**
   * @return true if this attribute can be searched
   */
  public boolean isSearchKey();

  /**
   * 
   * @return null, if no defaultFiller is registered for this LogAttributeType
   */
  public AttributeTypeDefaultFiller getAttributeDefaultFiller();

  /**
   * Renderer for this Attribute Type. Return DefaultLogAttributeRenderer.INSTANCE if no customized renderer should be used.
   * 
   * @return
   */
  public LogAttributeRenderer getRenderer();
}