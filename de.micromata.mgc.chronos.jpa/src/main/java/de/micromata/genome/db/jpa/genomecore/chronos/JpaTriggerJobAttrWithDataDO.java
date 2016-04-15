package de.micromata.genome.db.jpa.genomecore.chronos;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrDataBaseDO;

/**
 * Entity holds Strings longer than fits into one attribute value.
 *
 * @author lado
 */
@Entity
@DiscriminatorValue("1")
public class JpaTriggerJobAttrWithDataDO extends JpaTriggerJobAttrDO
{
  /**
   *
  */

  private static final long serialVersionUID = 4218266842522632806L;

  /**
   * Standard constructor.
   */
  public JpaTriggerJobAttrWithDataDO()
  {
    // Empty on purpose.
  }

  /**
   * Initialize with parent.
   *
   * @param parent the parent.
   */
  public JpaTriggerJobAttrWithDataDO(JpaTriggerJobDO parent)
  {
    super(parent);
  }

  /**
   * Full constructor.
   *
   * @param parent the parent.
   * @param propertyName the property name.
   * @param type the type
   * @param value the value to store.
   */
  public JpaTriggerJobAttrWithDataDO(JpaTriggerJobDO parent, String propertyName, char type, String value)
  {
    super(parent, propertyName, type, value);
  }

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", targetEntity = JpaTriggerJobAttrDataDO.class,
      orphanRemoval = true, fetch = FetchType.EAGER)
  @OrderColumn(name = "datarow")
  @Override
  public List<JpaTabAttrDataBaseDO<?, Long>> getData()
  {
    return super.getData();
  }
}
