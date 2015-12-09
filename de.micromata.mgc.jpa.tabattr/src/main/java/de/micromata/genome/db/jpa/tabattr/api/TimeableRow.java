/////////////////////////////////////////////////////////////////////////////
//
// Project   ProjectForge
//
// Copyright 2001-2009, Micromata GmbH, Kai Reinhard
//           All rights reserved.
//
/////////////////////////////////////////////////////////////////////////////

package de.micromata.genome.db.jpa.tabattr.api;

import java.util.Date;

/**
 * @author Roger Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface TimeableRow
{
  Date getStartTime();

  void setStartTime(final Date startTime);

  Date getEndTime();

  void setEndTime(final Date endTime);
}
