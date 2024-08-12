package de.micromata.genome.jpa.test.events;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

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
