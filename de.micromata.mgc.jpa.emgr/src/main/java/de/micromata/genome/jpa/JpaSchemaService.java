package de.micromata.genome.jpa;

/**
 * provides utils to manage schema
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface JpaSchemaService
{

  /**
   * Writes create script to file.
   *
   * @param fileName the file name
   */
  void exportCreateSchemaToFile(String fileName);

  /**
   * Export drop schema to file.
   *
   * @param fileName the file name
   */
  void exportDropSchemaToFile(String fileName);

  /**
   * Clear all tables. NOTE: Do this only in unittest.
   * 
   * The default implementation loads all entities into RAM.
   * 
   * If you need special handling, you can annotate entity class with ATableTruncater and implement a own table deleter.
   */
  void clearDatabase();
}
