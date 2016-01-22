package de.micromata.mgc.jpa.xmldump.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import de.micromata.genome.db.jpa.xmldump.api.JpaXmlPersist;
import de.micromata.genome.jpa.StdRecordDO;

@Entity
@JpaXmlPersist(beforePersistListener = TestWithLazyRefBeforePeristListener.class)
public class TestWithLazyRef extends StdRecordDO<Long>
{
  private TestWithLazyRef parent;

  private TestByLazyRef other;

  @Override
  @Id
  @GeneratedValue
  public Long getPk()
  {
    return pk;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_fk")
  public TestWithLazyRef getParent()
  {
    return parent;
  }

  public void setParent(TestWithLazyRef parent)
  {
    this.parent = parent;
  }

  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "other_fk")
  public TestByLazyRef getOther()
  {
    return other;
  }

  public void setOther(TestByLazyRef other)
  {
    this.other = other;
  }

}
