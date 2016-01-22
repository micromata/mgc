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
   * @return true, if entity should be persisted.
   */
  public boolean preparePersist(EntityMetadata entityMetadata, Object entity, XmlDumpRestoreContext ctx);
}
