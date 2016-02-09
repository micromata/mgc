package de.micromata.genome.jpa.events.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.WrappedTypedQuery;
import de.micromata.genome.jpa.events.EmgrTypedQueryGetResultListFilterEvent;

/**
 * Wrapps the execution of query.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <E> the element type
 */
public class EmgrEventTypedQuery<E>extends WrappedTypedQuery<E>
{

  /**
   * The emgr.
   */
  private IEmgr<?> emgr;

  /**
   * Instantiates a new emgr event typed query.
   *
   * @param emgr the emgr
   * @param nested the nested
   */
  public EmgrEventTypedQuery(IEmgr<?> emgr, TypedQuery<E> nested)
  {
    super(nested);
    this.emgr = emgr;
  }

  @Override
  public List<E> getResultList()
  {
    return emgr.getEmgrFactory().getEventFactory().invokeEvents(
        new EmgrTypedQueryGetResultListFilterEvent<E>(emgr, nested),
        (event) -> {
          event.setResult(nested.getResultList());
        });

  }

  @Override
  public E getSingleResult()
  {

    List<E> res = emgr.getEmgrFactory().getEventFactory().invokeEvents(
        new EmgrTypedQueryGetResultListFilterEvent<E>(emgr, nested),
        (event) -> {
          List<E> resl = new ArrayList<>();
          resl.add(nested.getSingleResult());
          event.setResult(resl);
        });
    if (res.isEmpty() == true) {
      throw new NoResultException("No entity found for query");
    }
    if (res.size() > 1) {
      throw new NonUniqueResultException("result returns more than one elements");
    }
    return res.get(0);
  }

  @Override
  public int executeUpdate()
  {
    return nested.executeUpdate();
  }

}
