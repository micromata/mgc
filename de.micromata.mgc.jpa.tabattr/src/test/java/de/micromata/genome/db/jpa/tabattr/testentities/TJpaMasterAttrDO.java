package de.micromata.genome.db.jpa.tabattr.testentities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TJPA_MASTER_ATTR")
public class TJpaMasterAttrDO extends TJpaBaseDO
{
  private TJpaMasterDO parent;
  private String propertyName;
  private String value;

  @ManyToOne(optional = false)
  @JoinColumn(name = "parent", referencedColumnName = "pk")
  public TJpaMasterDO getParent()
  {
    return parent;
  }

  public void setParent(TJpaMasterDO parent)
  {
    this.parent = parent;
  }

  @Column(name = "propertyName", nullable = false)
  public String getPropertyName()
  {
    return propertyName;
  }

  public void setPropertyName(String propertyName)
  {
    this.propertyName = propertyName;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

}
