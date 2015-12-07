package de.micromata.mgc.jpa.spring.test.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import de.micromata.genome.jpa.DbRecordDO;

@Entity
public class MyUserDO extends DbRecordDO<Long>
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
