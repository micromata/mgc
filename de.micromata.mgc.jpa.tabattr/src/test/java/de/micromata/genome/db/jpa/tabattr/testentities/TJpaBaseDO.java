package de.micromata.genome.db.jpa.tabattr.testentities;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class TJpaBaseDO
{
  private Long pk;

  @GeneratedValue
  @Id
  public Long getPk()
  {
    return pk;
  }

  public void setPk(Long pk)
  {
    this.pk = pk;
  }

}
