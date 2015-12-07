package de.micromata.mgc.db.jpa.stddo;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import de.micromata.mgc.db.jpa.api.DbRecord;

/**
 * Standard implementation of a record with a pk.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @author Johannes Unterstein (j.unterstein@micromata.de)
 */
@MappedSuperclass
public abstract class DbRecordDO<PK extends Serializable> implements DbRecord<PK>, Serializable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -7751062458383712172L;

  /**
   * The entity primary key.
   */
  protected PK pk;

  @Override
  @Transient
  public abstract PK getPk();

  @Override
  public void setPk(final PK pk)
  {
    this.pk = pk;
  }

  @Override
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

}
