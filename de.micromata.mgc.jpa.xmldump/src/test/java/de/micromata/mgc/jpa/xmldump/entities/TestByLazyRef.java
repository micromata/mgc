package de.micromata.mgc.jpa.xmldump.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import de.micromata.genome.jpa.StdRecordDO;

@Entity
public class TestByLazyRef extends StdRecordDO<Long>
{

  @Override
  @Id
  @GeneratedValue
  public Long getPk()
  {
    return pk;
  }

}
