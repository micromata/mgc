package de.micromata.mgc.jpa.xmldump.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import org.junit.Ignore;

import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrDataBaseDO;
import de.micromata.genome.db.jpa.xmldump.api.JpaXmlPersist;

/**
 * Entity holds Strings longer than fits into one attribute value.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
@JpaXmlPersist(noStore = true)
@Ignore
@Entity
@DiscriminatorValue("1")
public class TestTabAttrWithDataDO extends TestTabAttrDO
{

  private static final long serialVersionUID = 1965960042100228573L;

  public TestTabAttrWithDataDO()
  {

  }

  public TestTabAttrWithDataDO(TestMasterAttrDO parent)
  {
    super(parent);
  }

  public TestTabAttrWithDataDO(TestMasterAttrDO parent, String propertyName, char type, String value)
  {
    super(parent, propertyName, type, value);
  }

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", targetEntity = TestTabAttrDataDO.class,
      orphanRemoval = true, fetch = FetchType.EAGER)
  @Override
  @OrderColumn(name = "datarow")
  public List<JpaTabAttrDataBaseDO<?, Long>> getData()
  {
    return super.getData();
  }

}
