package de.micromata.genome.db.jpa.logging.schema;

import de.micromata.genome.db.jpa.logging.JpaLoggingEntMgrFactory;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.LocalSettingsEnv;

/**
 * Generate schema for this modul
 * 
 * @author roger
 * 
 */
public class GenSchema
{
  public static void main(String[] args)
  {
    LocalSettings ls = LocalSettings.get();
    LocalSettingsEnv.get();
    JpaLoggingEntMgrFactory.get().getJpaSchemaService().exportCreateSchemaToFile("genome-log-create.sql");
  }
}
