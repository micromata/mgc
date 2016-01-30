package de.micromata.mgc.jpa.hibernatesearch.impl;

import javax.persistence.EntityManager;

import de.micromata.genome.jpa.EmgrTx;
import de.micromata.mgc.jpa.hibernatesearch.api.SearchEmgrFactory;

/**
 * Default implementation of SearchEmgr.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class DefaultSearchEmgr extends SearchEmgr<DefaultSearchEmgr>
{

  public DefaultSearchEmgr(EntityManager entityManager, SearchEmgrFactory<DefaultSearchEmgr> emgrFactory,
      EmgrTx<DefaultSearchEmgr> emgrTx)
  {
    super(entityManager, emgrFactory, emgrTx);
  }

}
