package de.micromata.genome.db.jpa.history.impl;

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.db.jpa.history.api.DiffEntry;
import de.micromata.genome.db.jpa.history.entities.EntityOpType;

/**
 * A set of differences.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class DiffSet
{

  /**
   * The entity op type.
   */
  private EntityOpType entityOpType;

  /**
   * The diff entries.
   */
  private List<DiffEntry> diffEntries = new ArrayList<>();

  public List<DiffEntry> getDiffEntries()
  {
    return diffEntries;
  }

  public void setDiffEntries(List<DiffEntry> diffEntries)
  {
    this.diffEntries = diffEntries;
  }
}
