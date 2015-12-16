package de.micromata.genome.jpa.metainf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import de.micromata.genome.jpa.DbRecordDO;

@Entity
public class MyAnotChildDO extends DbRecordDO<Long>
{
  private MyAnotTestDO parent;

  private String propertyName;

  @Column(length = 64)
  public String getPropertyName()
  {
    return propertyName;
  }

  public void setPropertyName(String propertyName)
  {
    this.propertyName = propertyName;
  }

  @Id
  @Column(name = "pk")
  @Override
  public Long getPk()
  {
    return pk;
  }

  @ManyToOne(optional = false)
  @JoinColumn(name = "parent", referencedColumnName = "pk")
  public MyAnotTestDO getParent()
  {
    return parent;
  }

  public void setParent(MyAnotTestDO parent)
  {
    this.parent = parent;
  }

}
