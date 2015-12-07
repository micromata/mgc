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
 * The category has to match in the data base.
 *
 * @author roger
 */
public interface LogCategory
{

  /**
   * Short Name, commonly name() of the enumeration.
   *
   * @return the string
   */
  public String name();

  /**
   * Fully Qualified name seperated with dots
   * 
   * Normally prefix + . + name is fq name
   * 
   * Common implementation is getClass.getSimpleName() + "." + getName()
   * 
   * @return
   */
  public String getFqName();

  /**
   * A short prefix
   * 
   * @return
   */
  public String getPrefix();

}
