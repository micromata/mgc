package de.micromata.genome.db.jpa.xmldump.impl;

import de.micromata.genome.db.jpa.xmldump.api.XmlDumpRestoreContext;
import de.micromata.genome.jpa.metainf.EntityMetadata;

/**
 * The Interface XmlJpaPersistService.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface XmlJpaPersistService
{

  /**
   * Persist.
   *
   * @param ctx the ctx
   * @param data the data
   */
  Object persist(XmlDumpRestoreContext ctx, Object data);

  /**
   * Calls listener and then store.
   *
   * @param ctx the ctx
   * @param entityMetadata the entity metadata
   * @param entity the entity
   * @return the stored attached object
   */
  Object persist(XmlDumpRestoreContext ctx, EntityMetadata entityMetadata, Object entity);

  /**
   * Store entity.
   *
   * @param ctx the ctx
   * @param entityMetadata the entity metadata
   * @param entity the entity
   * @return the stored attached object
   */
  Object store(XmlDumpRestoreContext ctx, EntityMetadata entityMetadata, Object entity);

  /**
   * Flush the underlying entitymanager.
   * 
   * @param ctx
   */
  void flush(XmlDumpRestoreContext ctx);

  /**
   * Prepare persist.
   *
   * @param ctx the ctx
   * @param entityMetadata the entity metadata
   * @param data the data
   * @return != null if the entity is already persistet.
   */
  Object preparePersist(XmlDumpRestoreContext ctx, EntityMetadata entityMetadata, Object data);
}
