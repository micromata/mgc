package de.micromata.mgc.jpa.xmldump.entities;

import de.micromata.genome.db.jpa.xmldump.api.JpaXmlBeforePersistListener;
import de.micromata.genome.db.jpa.xmldump.api.XmlDumpRestoreContext;
import de.micromata.genome.jpa.metainf.EntityMetadata;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class TestWithLazyRefBeforePeristListener implements JpaXmlBeforePersistListener
{

  @Override
  public boolean preparePersist(EntityMetadata entityMetadata, Object entity, XmlDumpRestoreContext ctx)
  {
    TestWithLazyRef lr = (TestWithLazyRef) entity;
    if (lr.getParent() == null) {
      return true;
    }
    ctx.getPersistService().persist(ctx, entityMetadata, lr.getParent());

    return true;
  }

}
