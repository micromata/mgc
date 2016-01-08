package de.micromata.mgc.jpa.hibernatesearch.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import de.micromata.genome.jpa.StdRecordDO;

/**
 * to test.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Entity
@Indexed
public class MyNestedEntity extends StdRecordDO<Long>
{
  @ContainedIn
  private MyEntityDO parent;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
  private String nestedName;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
  private String nestedComment;

  @IndexedEmbedded(depth = 1)
  private MyNestedNestedEntity nestedNested;

  @Override
  public int hashCode()
  {
    return ObjectUtils.hashCode(nestedName);
  }

  @Override
  public boolean equals(Object obj)
  {
    if ((obj instanceof MyNestedEntity) == true) {
      return false;
    }
    MyNestedEntity other = (MyNestedEntity) obj;
    return ObjectUtils.equals(nestedName, other.getNestedName());
  }

  @Id
  @Override
  @GeneratedValue
  public Long getPk()
  {
    return pk;
  }

  @ManyToOne(optional = true)
  @JoinColumn(name = "parent", referencedColumnName = "pk")
  public MyEntityDO getParent()
  {
    return parent;
  }

  @OneToOne(optional = true)
  @JoinColumn(name = "nestednested", referencedColumnName = "pk")
  public MyNestedNestedEntity getNestedNested()
  {
    return nestedNested;
  }

  public void setNestedNested(MyNestedNestedEntity nestedNested)
  {
    this.nestedNested = nestedNested;
  }

  public void setParent(MyEntityDO parent)
  {
    this.parent = parent;
  }

  public String getNestedName()
  {
    return nestedName;
  }

  public void setNestedName(String name)
  {
    this.nestedName = name;
  }

  public String getNestedComment()
  {
    return nestedComment;
  }

  public void setNestedComment(String comment)
  {
    this.nestedComment = comment;
  }

}
