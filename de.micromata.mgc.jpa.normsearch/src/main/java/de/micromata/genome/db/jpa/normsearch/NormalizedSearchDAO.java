package de.micromata.genome.db.jpa.normsearch;

import java.util.List;

import de.micromata.genome.db.jpa.normsearch.entities.NormSearchDO;
import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.IEmgr;

/**
 * Store fields of a master table into a tokenized search table.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public interface NormalizedSearchDAO
{
  /**
   * Register the listener to Emgr.
   *
   * @param emgrFactory the emgr factory
   */
  void registerEmgrListener(EmgrFactory<?> emgrFactory);

  /**
   * Converts a string into normalized form.
   *
   * @param input unnormalized string. Must not be null.
   * @return the normalized string
   */
  public String normalize(String input);

  /**
   * split a input string into token.
   *
   * @param input the input
   * @return the string[]
   */
  public String[] tokenize(String input);

  /**
   * Search.
   *
   * @param emgr the emgr
   * @param clazz the clazz
   * @param expression the expression
   * @return the list
   */
  public List<Long> search(IEmgr<?> emgr, Class<? extends NormSearchDO> clazz, String expression);

  /**
   * internal to insert normalized values.
   * 
   * Note: All operations has be done inside a transaction. Normally the transaction, which also insert/update/delete
   * the rec.
   * 
   * @param emgr Entity Manager
   * @param rec the table row, containing the columns/properties
   * @param fields property names of columns
   */
  public void insert(IEmgr<?> emgr, NormSearchMasterTable rec, String... fields);

  /**
   * internal to update normalized values.
   *
   * @param emgr Entity Manager
   * @param rec the table row, containing the columns/properties
   * @param fields property names of columns
   */
  public void update(IEmgr<?> emgr, NormSearchMasterTable rec, String... fields);

  /**
   * internal to delete normalized values.
   *
   * @param emgr Entity Manager
   * @param rec the table row, containing the columns/properties
   * @param fields property names of columns
   */
  public void delete(IEmgr<?> emgr, NormSearchMasterTable rec, String... fields);

  /**
   * Insert using the NormSearchTable annotation.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public void insert(IEmgr<?> emgr, DbRecord entity);

  /**
   * Update using the NormSearchTable annotation.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public void update(IEmgr<?> emgr, DbRecord entity);

  /**
   * delete using the NormSearchTable annotation.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public void delete(IEmgr<?> emgr, DbRecord entity);

}
