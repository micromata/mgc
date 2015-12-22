package de.micromata.genome.db.jpa.history;

import org.junit.Test;

import de.micromata.genome.GenomeTestCase;
import de.micromata.genome.db.jpa.history.test.HistoryTestEmgrFactory;
import de.micromata.genome.db.jpa.history.test.TestHistoryMasterExtDO;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ExtHistoryTest extends GenomeTestCase
{
  @Test
  public void testPersist()
  {
    TestHistoryMasterExtDO nist = new TestHistoryMasterExtDO();
    nist.setEntityId(42L);
    nist.setEntityName("Bla");
    HistoryTestEmgrFactory emf = HistoryTestEmgrFactory.get();
    Object pk = emf.runInTrans((emgr) -> emgr.insertAttached(nist));

  }
}
