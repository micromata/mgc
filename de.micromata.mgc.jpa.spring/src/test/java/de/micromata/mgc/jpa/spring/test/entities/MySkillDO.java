package de.micromata.mgc.jpa.spring.test.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import de.micromata.genome.jpa.DbRecordDO;

@Entity
public class MySkillDO extends DbRecordDO<Long>
{
  private MyUserDO user;

  private String name;

  @Override
  @Id
  @GeneratedValue
  public Long getPk()
  {
    return pk;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  public MyUserDO getUser()
  {
    return user;
  }

  public void setUser(MyUserDO user)
  {
    this.user = user;
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
