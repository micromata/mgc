package de.micromata.genome.db.jpa.logging;

import de.micromata.genome.db.jpa.logging.entities.GenomeLogMasterDO;
import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrFactory;

/**
 * Standard Logging table.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class GenomeJpaLoggingImpl extends BaseJpaLoggingImpl<GenomeLogMasterDO>
{
  public GenomeJpaLoggingImpl()
  {
    initProps();
  }

  @Override
  protected Class<GenomeLogMasterDO> getMasterClass()
  {
    return GenomeLogMasterDO.class;
  }

  @Override
  protected GenomeLogMasterDO createNewMaster()
  {
    return new GenomeLogMasterDO();
  }

  @Override
  protected EmgrFactory<DefaultEmgr> getEmgrFactory()
  {
    return JpaLoggingEntMgrFactory.get();
  }

}
