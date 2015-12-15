/////////////////////////////////////////////////////////////////////////////
//
// Project   ProjectForge
//
// Copyright 2001-2009, Micromata GmbH, Kai Reinhard
//           All rights reserved.
//
/////////////////////////////////////////////////////////////////////////////

package de.micromata.genome.db.jpa.tabattr.api;

import java.io.Serializable;
import java.util.List;

/**
 *
 * A Schema for Attributes.
 *
 * @author Roger Kommer (r.kommer.extern@micromata.de)
 *
 */
public class AttrSchema implements Serializable
{

  /**
   * The columns.
   */
  private List<AttrDescription> columns;

  /**
   * Instantiates a new attr schema.
   */
  public AttrSchema()
  {

  }

  /**
   * Instantiates a new attr schema.
   *
   * @param columns the columns
   */
  public AttrSchema(final List<AttrDescription> columns)
  {
    this.columns = columns;
  }

  /**
   * Gets the columns.
   *
   * @return the columns
   */
  public List<AttrDescription> getColumns()
  {
    return columns;
  }

  /**
   * Sets the columns.
   *
   * @param columns the new columns
   */
  public void setColumns(final List<AttrDescription> columns)
  {
    this.columns = columns;

  }
}
