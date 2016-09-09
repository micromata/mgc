package de.micromata.genome.db.jpa.tabattr.testentities;

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.db.jpa.tabattr.api.EntityWithTimeableAttr;

public class FooDO implements EntityWithTimeableAttr<Integer, FooTimedDO>
{
  private List<FooTimedDO> timeableAttributes = new ArrayList<>();

  @Override
  public void addTimeableAttribute(FooTimedDO row)
  {
    timeableAttributes.add(row);
  }

  @Override
  public List<FooTimedDO> getTimeableAttributes()
  {
    return timeableAttributes;
  }
}
