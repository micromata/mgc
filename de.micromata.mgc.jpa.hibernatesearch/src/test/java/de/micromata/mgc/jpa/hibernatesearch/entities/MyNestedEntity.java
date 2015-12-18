package de.micromata.mgc.jpa.hibernatesearch.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import de.micromata.genome.jpa.StdRecordDO;

@Entity
@Indexed
public class MyNestedEntity extends StdRecordDO<Long>
{
  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
  private String name;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
  private String comment;

  @Override
  public int hashCode()
  {
    return ObjectUtils.hashCode(name);
  }

  @Override
  public boolean equals(Object obj)
  {
    if ((obj instanceof MyNestedEntity) == true) {
      return false;
    }
    MyNestedEntity other = (MyNestedEntity) obj;
    return ObjectUtils.equals(name, other.getName());
  }

  @Id
  @Override
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

  public String getComment()
  {
    return comment;
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }

}
