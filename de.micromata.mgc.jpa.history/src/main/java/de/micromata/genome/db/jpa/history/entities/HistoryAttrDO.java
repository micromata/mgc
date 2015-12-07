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
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Index;
import org.junit.Ignore;

import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrBaseDO;
import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrDataBaseDO;

/**
 * JPA entity for TB_TA_GATTR.
 *
 * @author roger
 *
 */
@Ignore
@Entity
@Table(name = "TB_BASE_GHISTORY_ATTR")
@SequenceGenerator(name = "SQ_BASE_GHISTORY_ATTR_PK", sequenceName = "SQ_BASE_GHISTORY_ATTR_PK")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@org.hibernate.annotations.Table(//
    indexes = { //
        @Index(name = "IX_BASE_GHISTORY_ATTR_MODAT", columnNames = { "MODIFIEDAT" }), //
        @Index(name = "IX_BASE_GHISTORY_ATTR_MST_FK", columnNames = { "MASTER_FK" }),//
    }, appliesTo = "TB_BASE_GHISTORY_ATTR")
@DiscriminatorColumn(name = "WITHDATA", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("0")
public class HistoryAttrDO extends JpaTabAttrBaseDO<HistoryMasterDO>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -5490342158738541970L;

  /**
   * Instantiates a new history attr do.
   */
  public HistoryAttrDO()
  {

  }

  /**
   * Instantiates a new history attr do.
   *
   * @param parent the parent
   */
  public HistoryAttrDO(HistoryMasterDO parent)
  {
    super(parent);
  }

  /**
   * Instantiates a new history attr do.
   *
   * @param parent the parent
   * @param propertyName the property name
   * @param type the type
   * @param value the value
   */
  public HistoryAttrDO(HistoryMasterDO parent, String propertyName, char type, String value)
  {
    super(parent, propertyName, type, value);
  }

  @Override
  public JpaTabAttrDataBaseDO<?> createData(String data)
  {
    return new HistoryAttrDataDO(this, data);
  }

  @Override
  @Transient
  public int getMaxDataLength()
  {
    return JpaTabAttrDataBaseDO.DATA_MAXLENGTH;
  }

  @Override
  @Id
  @Column(name = "BASE_GHISTORY_ATTR")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_BASE_GHISTORY_ATTR_PK")
  public Long getPk()
  {
    return pk;
  }

  @Override
  @ManyToOne(optional = false)
  @JoinColumn(name = "MASTER_FK", referencedColumnName = "BASE_GHISTORY")
  public HistoryMasterDO getParent()
  {
    return super.getParent();
  }

}
