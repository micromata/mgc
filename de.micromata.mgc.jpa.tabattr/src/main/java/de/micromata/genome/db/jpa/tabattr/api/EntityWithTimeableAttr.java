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
 * Entity having an timeable attributes.
 *
 * @author Roger Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface EntityWithTimeableAttr<PK extends Serializable, T extends TimeableAttrRow<PK>>
{

  void addTimeableAttribute(T row);

  /**
   * Gets the timeable attributes.
   *
   * @return the timeable attributes
   */
  List<T> getTimeableAttributes();

  /**
   * Name of the schema of the attributes.
   *
   * @return the attr schema name
   */
  String getAttrSchemaName();
}
