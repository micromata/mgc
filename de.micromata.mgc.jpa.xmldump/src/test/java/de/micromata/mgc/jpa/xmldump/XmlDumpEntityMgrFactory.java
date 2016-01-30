package de.micromata.mgc.jpa.xmldump;

import javax.persistence.EntityManager;

import org.junit.Ignore;

import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.EmgrTx;

/**
 * emgr factory for tests.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
@Ignore
public class XmlDumpEntityMgrFactory extends EmgrFactory<DefaultEmgr>
{

  static XmlDumpEntityMgrFactory INSTANCE;

  public static synchronized XmlDumpEntityMgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new XmlDumpEntityMgrFactory();
    return INSTANCE;
  }

  public XmlDumpEntityMgrFactory()
  {
    super("de.micromata.genome.jpa.tabattr.testentities");
  }

  @Override
  protected DefaultEmgr createEmgr(EntityManager entitManager, EmgrTx<DefaultEmgr> emgrTx)
  {
    return new DefaultEmgr(entitManager, this, emgrTx);
  }

}
