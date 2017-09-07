package de.micromata.genome.jpa.test.events;

import de.micromata.genome.jpa.ComplexEntity;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.events.EmgrEventHandler;
import de.micromata.genome.jpa.events.EmgrInitForInsertEvent;

public class WithCustomerUpdateEmgrEventHandler implements EmgrEventHandler<EmgrInitForInsertEvent>
{
  @Override
  public void onEvent(EmgrInitForInsertEvent event)
  {
    if (event.getRecord() instanceof WithCustomer) {
      WithCustomer wc = (WithCustomer) event.getRecord();
      wc.setCustomerId(getCurrentUserCustomerId(event.getEmgr()));
    }
    if (event.getRecord() instanceof ComplexEntity) {
      ComplexEntity ce = (ComplexEntity) event.getRecord();
      ce.visit(childRec -> {
        if (childRec instanceof WithCustomer) {
          WithCustomer cwc = (WithCustomer) childRec;
          cwc.setCustomerId(getCurrentUserCustomerId(event.getEmgr()));
        }
      });
    }
  }

  private String getCurrentUserCustomerId(IEmgr<?> emgr)
  {
    return "ABC";
  }
}
