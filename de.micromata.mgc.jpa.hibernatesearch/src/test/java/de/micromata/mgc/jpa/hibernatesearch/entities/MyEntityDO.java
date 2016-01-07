package de.micromata.mgc.jpa.hibernatesearch.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import de.micromata.genome.jpa.ComplexEntity;
import de.micromata.genome.jpa.ComplexEntityVisitor;
import de.micromata.genome.jpa.StdRecordDO;

@Entity
@Indexed
public class MyEntityDO extends StdRecordDO<Long> implements ComplexEntity

{
  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
  private String name;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
  private String loginName;

  private String notSearchable;

  @IndexedEmbedded(depth = 2, includePaths = { "nestedName", "nestedNested.nestedNestedComment" })
  private MyNestedEntity nested;

  @IndexedEmbedded(depth = 1)
  private Set<MyNestedEntity> assignedNested = new HashSet<>();

  @Override
  public void visit(ComplexEntityVisitor visitor)
  {
    visitor.visit(this);
    for (MyNestedEntity ne : assignedNested) {
      visitor.visit(ne);
    }
  }

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

  public String getLoginName()
  {
    return loginName;
  }

  public void setLoginName(String loginName)
  {
    this.loginName = loginName;
  }

  public String getNotSearchable()
  {
    return notSearchable;
  }

  public void setNotSearchable(String notSearchable)
  {
    this.notSearchable = notSearchable;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  public MyNestedEntity getNested()
  {
    return nested;
  }

  public void setNested(MyNestedEntity nested)
  {
    this.nested = nested;
  }

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = MyNestedEntity.class)
  public Set<MyNestedEntity> getAssignedNested()
  {
    return assignedNested;
  }

  public void setAssignedNested(Set<MyNestedEntity> assignedNested)
  {
    this.assignedNested = assignedNested;
  }

}
