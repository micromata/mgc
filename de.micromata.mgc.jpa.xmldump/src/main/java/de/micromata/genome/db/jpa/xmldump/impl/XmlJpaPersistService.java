package de.micromata.genome.db.jpa.xmldump.impl;

import de.micromata.genome.db.jpa.xmldump.api.XmlDumpRestoreContext;
import de.micromata.genome.jpa.metainf.EntityMetadata;

public interface XmlJpaPersistService
{
  void persist(XmlDumpRestoreContext ctx, Object data);

  void persist(XmlDumpRestoreContext ctx, EntityMetadata entityMetadata, Object entity);

  boolean preparePersist(XmlDumpRestoreContext ctx, EntityMetadata entityMetadata, Object data);
}
