/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome 
//
// Author    r.kommer.extern@micromata.de
// Created   18.02.2013
// Copyright Micromata 2013
//
/////////////////////////////////////////////////////////////////////////////

package de.micromata.genome.db.jpa.tabattr.testentities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.junit.Ignore;

import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrDataBaseDO;

/**
 * JPA entity for TB_TA_GATTR_DATA
 * 
 * @author roger
 * 
 */
@Ignore
@Entity
@Table(name = "TB_TST_ATTRMASTER_ATTR_DATA")
@SequenceGenerator(name = "SQ_TST_AM_ATTR_DATA_PK", sequenceName = "SQ_TST_AM_ATTR_DATA_PK")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@org.hibernate.annotations.Table(//
    indexes = { //
        @Index(name = "IX_TST_AM_ATTR_DATA_MODAT", columnNames = { "MODIFIEDAT" }), //
        @Index(name = "IX_TST_AM_ATTR_DATA_PARENT", columnNames = { "PARENT_PK" }),//
    }, appliesTo = "TB_TST_ATTRMASTER_ATTR_DATA")
public class TestTabAttrDataDO extends JpaTabAttrDataBaseDO<TestTabAttrDO, Long>
{

  private static final long serialVersionUID = -3845387843789907008L;

  public TestTabAttrDataDO()
  {

  }

  public TestTabAttrDataDO(TestTabAttrDO parent)
  {
    super(parent);
  }

  public TestTabAttrDataDO(TestTabAttrDO parent, String value)
  {
    super(parent, value);
  }

  @Override
  @Id
  @Column(name = "TST_ATTRMASTER_ATTR_DATA")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TST_AM_ATTR_DATA_PK")
  public Long getPk()
  {
    return pk;
  }

  @Override
  @ManyToOne(optional = false)
  @JoinColumn(name = "PARENT_PK", referencedColumnName = "TST_ATTRMASTER_ATTR")
  public TestTabAttrDO getParent()
  {
    return super.getParent();
  }
}
