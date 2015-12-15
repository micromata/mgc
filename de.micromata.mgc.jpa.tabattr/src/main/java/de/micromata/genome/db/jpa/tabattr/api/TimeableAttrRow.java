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

import de.micromata.genome.jpa.DbRecord;

/**
 * Timeable rows with attributes.
 *
 * @author Roger Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface TimeableAttrRow<PK extends Serializable>extends TimeableRow, EntityWithAttributes, DbRecord<PK>
{

}
