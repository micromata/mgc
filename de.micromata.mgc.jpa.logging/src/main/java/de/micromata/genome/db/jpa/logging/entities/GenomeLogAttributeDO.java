package de.micromata.genome.db.jpa.logging.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

/**
 * A logging Attribute entity.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
@Entity
@Table(name = "TB_TA_GLOG_ATTRIBUTE")
@org.hibernate.annotations.Table(indexes = { //
    @Index(name = "IX_TA_BLOGATTR_SVALUE", columnNames = { "BASE_GLOG_ATTRIBUTE", "SHORT_VALUE" }), //
    @Index(name = "IX_TA_GLOG_ATTRIBUTE_MODAT", columnNames = { "MODIFIEDAT" })
    //
}, appliesTo = "TB_TA_GLOG_ATTRIBUTE")
@SequenceGenerator(name = "SQ_TA_GLOG_ATTRIBUTE", sequenceName = "SQ_TA_GLOG_ATTRIBUTE")
public class GenomeLogAttributeDO extends BaseLogAttributeDO<GenomeLogMasterDO>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -688188919513712393L;

  /**
   * {@inheritDoc}
   *
   */

  @Override
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TA_GLOG_ATTRIBUTE")
  @Column(name = "TA_GLOG_ATTRIBUTE")
  public Long getPk()
  {
    return pk;
  }

  /**
   * Gets the log master.
   *
   * @return the log master
   */

  @Override
  @ManyToOne
  @JoinColumn(name = "TA_GLOG_MASTER", referencedColumnName = "TA_GLOG_MASTER")
  public GenomeLogMasterDO getLogMaster()
  {
    return super.getLogMaster();
  }

}
