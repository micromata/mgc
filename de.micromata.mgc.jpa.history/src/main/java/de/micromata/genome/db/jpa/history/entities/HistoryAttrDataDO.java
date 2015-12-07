/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome 
//
// Author    r.kommer.extern@micromata.de
// Created   18.02.2013
// Copyright Micromata 2013
//
/////////////////////////////////////////////////////////////////////////////

package de.micromata.genome.db.jpa.history.entities;

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

import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrDataBaseDO;

/**
 * JPA entity for TB_TA_GATTR_DATA.
 *
 * @author roger
 */

@Entity
@Table(name = "TB_BASE_GHISTORY_ATTR_DATA")
@SequenceGenerator(name = "SQ_BASE_GHISTORY_ATTR_DATA_PK", sequenceName = "SQ_BASE_GHISTORY_ATTR_DATA_PK")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@org.hibernate.annotations.Table(//
    indexes = { //
        @Index(name = "IX_BASE_GHISTORY_A_D_MODAT", columnNames = { "MODIFIEDAT" }), //
        @Index(name = "IX_BASE_GHISTORY_A_D_PARENT", columnNames = { "PARENT_PK" }),//
    }, appliesTo = "TB_BASE_GHISTORY_ATTR_DATA")
public class HistoryAttrDataDO extends JpaTabAttrDataBaseDO<HistoryAttrDO, Long>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -3845387843789907008L;

  /**
   * Instantiates a new history attr data do.
   */
  public HistoryAttrDataDO()
  {

  }

  /**
   * Instantiates a new history attr data do.
   *
   * @param parent the parent
   */
  public HistoryAttrDataDO(HistoryAttrDO parent)
  {
    super(parent);
  }

  /**
   * Instantiates a new history attr data do.
   *
   * @param parent the parent
   * @param value the value
   */
  public HistoryAttrDataDO(HistoryAttrDO parent, String value)
  {
    super(parent, value);
  }

  @Override
  @Id
  @Column(name = "BASE_GHISTORY_ATTR_DATA")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_BASE_GHISTORY_ATTR_DATA_PK")
  public Long getPk()
  {
    return pk;
  }

  @Override
  @ManyToOne(optional = false)
  @JoinColumn(name = "PARENT_PK", referencedColumnName = "BASE_GHISTORY_ATTR")
  public HistoryAttrDO getParent()
  {
    return super.getParent();
  }
}
