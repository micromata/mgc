package de.micromata.mgc.jpa.hibernatesearch.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import de.micromata.genome.jpa.StdRecordDO;

@Entity
@Indexed
public class MyNestedNestedEntity extends StdRecordDO<Long>
{
  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
  private String nestedNestedComment;

  @Id
  @Override
  @GeneratedValue
  public Long getPk()
  {
    return pk;
  }

  public String getNestedNestedComment()
  {
    return nestedNestedComment;
  }

  public void setNestedNestedComment(String nestedNestedComment)
  {
    this.nestedNestedComment = nestedNestedComment;
  }

}
