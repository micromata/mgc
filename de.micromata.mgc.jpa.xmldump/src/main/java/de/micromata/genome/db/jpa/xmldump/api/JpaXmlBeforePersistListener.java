package de.micromata.genome.db.jpa.xmldump.api;

import de.micromata.genome.jpa.metainf.EntityMetadata;

/**
 * Prepare an entity after restoring from xml.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface JpaXmlBeforePersistListener
{

  /**
   * Prepare persist.
   *
   * @param emgr the emgr
   * @param entityMetadata the entity metadata
   * @param entity the entity
   * @param ctx the ctx
   * @return persisted attached object, otherwise null.
   */
  public Object preparePersist(EntityMetadata entityMetadata, Object entity, XmlDumpRestoreContext ctx);
}
