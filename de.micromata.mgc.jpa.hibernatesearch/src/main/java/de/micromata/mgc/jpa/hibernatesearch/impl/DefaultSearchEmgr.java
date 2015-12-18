package de.micromata.mgc.jpa.hibernatesearch.impl;

import javax.persistence.EntityManager;

import de.micromata.mgc.jpa.hibernatesearch.api.SearchEmgrFactory;

/**
 * Default implementation of SearchEmgr.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class DefaultSearchEmgr extends SearchEmgr<DefaultSearchEmgr>
{

  public DefaultSearchEmgr(EntityManager entityManager, SearchEmgrFactory<DefaultSearchEmgr> emgrFactory)
  {
    super(entityManager, emgrFactory);
  }

}
