package de.micromata.genome.db.jpa.xmldump.api;

import java.io.File;
import java.io.InputStream;
import java.io.Writer;

import de.micromata.genome.jpa.EmgrFactory;

/**
 * The Interface JpaXmlDumpService.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface JpaXmlDumpService
{
  String dumpToXml(EmgrFactory<?> fac);

  void dumpToXml(EmgrFactory<?> fac, File outfile);

  void dumpToXml(EmgrFactory<?> fac, Writer writer);

  public static enum RestoreMode
  {
    /**
     * Asume Pks are stable, and only insert new entities
     */
    InsertNew,
    /**
     * Asume Pks are stable merge
     */
    OverWrite,
    /**
     * Insert entities. May fail, if constraints
     */
    InsertAll
  }

  void restoreDb(EmgrFactory<?> fac, InputStream inputStream, RestoreMode restoreMode);

  void restoreDb(EmgrFactory<?> fac, File file, RestoreMode restoreMode);

}
