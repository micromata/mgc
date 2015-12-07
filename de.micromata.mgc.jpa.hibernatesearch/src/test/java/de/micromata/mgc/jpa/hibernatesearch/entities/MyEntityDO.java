package de.micromata.mgc.jpa.hibernatesearch.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import de.micromata.mgc.db.jpa.stddo.StdRecordDO;

@Entity
@Indexed
public class MyEntityDO extends StdRecordDO<Long>
{
  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
  private String name;

  @Override
  @Id
  @GeneratedValue
  public Long getPk()
  {
    return pk;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

}
