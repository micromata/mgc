package de.micromata.genome.jpa.test.events;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import de.micromata.genome.jpa.StdRecordDO;

@Entity
public class MyStockItemDO extends StdRecordDO<Long> implements WithCustomer
{
  private String customerId;

  @Override
  @Id
  @GeneratedValue
  public Long getPk()
  {
    return pk;
  }

  @Override
  public String getCustomerId()
  {
    return customerId;
  }

  @Override
  public void setCustomerId(String customerId)
  {
    this.customerId = customerId;
  }
}
