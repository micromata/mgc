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

  /**
   * Dump all table entities into xml.
   *
   * @param fac the fac
   * @return the string
   */
  String dumpToXml(EmgrFactory<?> fac);

  /**
   * Dump to xml.
   *
   * @param fac the fac
   * @param outfile the outfile
   */
  void dumpToXml(EmgrFactory<?> fac, File outfile);

  /**
   * Dump to xml.
   *
   * @param fac the fac
   * @param writer the writer
   */
  void dumpToXml(EmgrFactory<?> fac, Writer writer);

  /**
   * The Enum RestoreMode.
   */
  public static enum RestoreMode
  {

    /**
     * Asume Pks are stable, and only insert new entities.
     */
    InsertNew,

    /**
     * Asume Pks are stable merge.
     */
    OverWrite,
    /**
     * Insert entities. May fail, if constraints
     */
    InsertAll
  }

  /**
   * Restore db.
   *
   * @param fac the fac
   * @param inputStream the input stream
   * @param restoreMode the restore mode
   */
  void restoreDb(EmgrFactory<?> fac, InputStream inputStream, RestoreMode restoreMode);

  /**
   * Restore db.
   *
   * @param fac the fac
   * @param file the file
   * @param restoreMode the restore mode
   */
  void restoreDb(EmgrFactory<?> fac, File file, RestoreMode restoreMode);

}
