package de.micromata.mgc.db.jpa.multipc.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import de.micromata.genome.jpa.StdRecordDO;

@Entity
public class MyUserDO extends StdRecordDO<Long>
{
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
