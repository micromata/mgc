package de.micromata.genome.db.jpa.normsearch;

import javax.persistence.Transient;

import de.micromata.genome.db.jpa.normsearch.entities.NormSearchDO;
import de.micromata.genome.jpa.StdRecord;

/**
 * An Interface, which a table should implement to write its columns as normalized search values.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */

public interface NormSearchMasterTable extends StdRecord<Long>
{
  /**
   * Create a new row for searching
   * 
   * @param column the column to create normalized search
   * @param value the value to create normalized search
   * @return
   */
  public NormSearchDO createNormSearchEntry(String column, String value);

  /**
   * return the class to for the normalized search.
   *
   * @return the norm search table class
   */
  @Transient
  public Class<? extends NormSearchDO> getNormSearchTableClass();

  /**
   * The columns to search with values.
   *
   * @return never null
   */
  @Transient
  String[] getSearchPropertyNames();
}
