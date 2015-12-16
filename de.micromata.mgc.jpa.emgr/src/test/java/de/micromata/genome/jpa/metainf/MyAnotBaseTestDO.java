package de.micromata.genome.jpa.metainf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.micromata.genome.jpa.DbRecordDO;

@MappedSuperclass
public abstract class MyAnotBaseTestDO extends DbRecordDO<Long>
{
  private Long synPk1;
  private long synpk2;

  private Date myDate;

  public Long getSynPk1()
  {
    return synPk1;
  }

  public void setSynPk1(Long synPk1)
  {
    this.synPk1 = synPk1;
  }

  public long getSynpk2()
  {
    return synpk2;
  }

  public void setSynpk2(long synpk2)
  {
    this.synpk2 = synpk2;
  }

  @Column(name = "day")
  @Temporal(TemporalType.TIMESTAMP)
  public Date getMyDate()
  {
    return myDate;
  }

  public void setMyDate(Date myDate)
  {
    this.myDate = myDate;
  }

}
