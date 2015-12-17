package de.micromata.genome.jpa.events.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.WrappedTypedQuery;
import de.micromata.genome.jpa.events.EmgrTypeQueryGetResultListFilterEvent;

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
        new EmgrTypeQueryGetResultListFilterEvent<E>(emgr, nested),
        (event) -> {
          event.setResult(nested.getResultList());
        });

  }

  @Override
  public E getSingleResult()
  {
    return nested.getSingleResult();
  }

  @Override
  public int executeUpdate()
  {
    return nested.executeUpdate();
  }

}