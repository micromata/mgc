package de.micromata.genome.jpa.events;

import javax.persistence.Query;

import de.micromata.genome.jpa.IEmgr;

/**
 * Wrapps creating a query.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class EmgrCreateQueryFilterEvent extends EmgrFilterEvent<Query>
{

  /**
   * The sql.
   */
  private String sql;

  /**
   * Instantiates a new emgr create query filter event.
   *
   * @param emgr the emgr
   * @param sql the sql
   */
  public EmgrCreateQueryFilterEvent(IEmgr<?> emgr, String sql)
  {
    super(emgr);
    this.sql = sql;
  }

  public String getSql()
  {
    return sql;
  }

  public void setSql(String sql)
  {
    this.sql = sql;
  }
}
