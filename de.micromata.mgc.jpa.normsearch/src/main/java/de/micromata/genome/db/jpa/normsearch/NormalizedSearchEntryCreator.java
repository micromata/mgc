package de.micromata.genome.db.jpa.normsearch;

import de.micromata.genome.db.jpa.normsearch.entities.NormSearchDO;
import de.micromata.genome.jpa.DbRecord;

/**
 * creates an normsearch entry.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface NormalizedSearchEntryCreator
{
  NormSearchDO createEntry(NormalizedSearchService normalizedSearchService, DbRecord<?> master,
      Class<? extends NormSearchDO> normEntityClass);
}
