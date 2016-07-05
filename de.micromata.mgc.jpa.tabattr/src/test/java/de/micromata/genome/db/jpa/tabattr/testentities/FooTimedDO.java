package de.micromata.genome.db.jpa.tabattr.testentities;

import java.io.Serializable;

import de.micromata.genome.db.jpa.tabattr.api.TimeableAttrRow;
import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrBaseDO;
import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrDataBaseDO;
import de.micromata.genome.db.jpa.tabattr.entities.TimeableBaseDO;

public class FooTimedDO extends TimeableBaseDO<FooTimedDO, Integer> implements TimeableAttrRow<Integer>
{
  @Override
  public Class<? extends JpaTabAttrBaseDO<FooTimedDO, ? extends Serializable>> getAttrEntityClass()
  {
    return null;
  }

  @Override
  public Class<? extends JpaTabAttrBaseDO<FooTimedDO, ? extends Serializable>> getAttrEntityWithDataClass()
  {
    return null;
  }

  @Override
  public Class<? extends JpaTabAttrDataBaseDO<? extends JpaTabAttrBaseDO<FooTimedDO, Integer>, Integer>> getAttrDataEntityClass()
  {
    return null;
  }

  @Override
  public JpaTabAttrBaseDO<FooTimedDO, Integer> createAttrEntity(String key, char type, String value)
  {
    return null;
  }

  @Override
  public JpaTabAttrBaseDO<FooTimedDO, Integer> createAttrEntityWithData(String key, char type, String value)
  {
    return null;
  }

  @Override
  public Integer getPk()
  {
    return null;
  }
}
