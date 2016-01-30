package de.micromata.genome.db.jpa.tabattr.testentities;

import java.util.Map;
import java.util.TreeMap;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table()
@Entity
public class TJpaMasterDO extends TJpaBaseDO
{
  Map<String, TJpaMasterAttrDO> attributes = new TreeMap<>();

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", targetEntity = TJpaMasterAttrDO.class,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  @MapKey(name = "propertyName")
  public Map<String, TJpaMasterAttrDO> getAttributes()
  {
    return attributes;
  }

  public void setAttributes(Map<String, TJpaMasterAttrDO> attributes)
  {
    this.attributes = attributes;
  }

  public void putAttribute(String key, String value)
  {
    TJpaMasterAttrDO atr = attributes.get(key);
    if (atr == null) {
      atr = new TJpaMasterAttrDO();
      atr.setPropertyName(key);
      atr.setParent(this);
      attributes.put(key, atr);
    }
    atr.setValue(value);
  }

  public void putNewAttribute(String key, String value)
  {
    TJpaMasterAttrDO atr = new TJpaMasterAttrDO();
    atr.setPropertyName(key);
    atr.setParent(this);
    atr.setValue(value);
    attributes.put(key, atr);
  }
}
