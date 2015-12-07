package de.micromata.genome.jpa.events;

import java.io.Serializable;

import de.micromata.genome.jpa.IEmgr;

/**
 * Filter, which wrapps loading a entity by pk.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <E> the element type
 */
public class EmgrFindByPkFilterEvent<E, PK extends Serializable>extends EmgrFilterEvent<E>
{

  /**
   * The entity class.
   */
  private Class<E> entityClass;

  /**
   * The pk.
   */
  private PK pk;

  /**
   * Instantiates a new emgr find by pk filter event.
   *
   * @param emgr the emgr
   * @param entityClass the entity class
   * @param pk the pk
   */
  public EmgrFindByPkFilterEvent(IEmgr<?> emgr, Class<E> entityClass, PK pk)
  {
    super(emgr);
    this.entityClass = entityClass;
    this.pk = pk;
  }

  public Class<E> getEntityClass()
  {
    return entityClass;
  }

  public void setEntityClass(Class<E> entityClass)
  {
    this.entityClass = entityClass;
  }

  public PK getPk()
  {
    return pk;
  }

  public void setPk(PK pk)
  {
    this.pk = pk;
  }
}
