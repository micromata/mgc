package de.micromata.genome.db.jpa.history.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.micromata.genome.db.jpa.history.api.DiffEntry;
import de.micromata.genome.db.jpa.history.api.HistoryEntry;
import de.micromata.genome.db.jpa.history.api.HistoryServiceManager;
import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrBaseDO;
import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrDataBaseDO;
import de.micromata.genome.db.jpa.tabattr.entities.JpaTabMasterBaseDO;

/**
 * Stores History on changing entities.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Entity
@Table(name = "TB_BASE_GHISTORY")
@SequenceGenerator(name = "SQ_BASE_GHISTORY_PK", sequenceName = "SQ_BASE_GHISTORY_PK")
public class HistoryMasterDO extends JpaTabMasterBaseDO<HistoryMasterDO, Long> implements HistoryEntry
{

  /**
   * The Constant serialVersionUID.
   */

  private static final long serialVersionUID = 1687712634400467851L;

  /**
   * The entity op type.
   */
  private EntityOpType entityOpType;

  /**
   * Table/Entity name changing.
   */
  private String entityName;
  /**
   * PK of Record changing.
   */
  private Long entityId;

  /**
   * The user comment.
   */
  private String userComment;

  /**
   * Optional transactionId grouping varios modifications.
   */
  private String transactionId;

  @Override
  @Id
  @Column(name = "BASE_GHISTORY")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_BASE_GHISTORY_PK")
  public Long getPk()
  {
    return pk;
  }

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", targetEntity = HistoryAttrDO.class, orphanRemoval = true,
      fetch = FetchType.EAGER)
  @MapKey(name = "propertyName")
  @Override
  public Map<String, JpaTabAttrBaseDO<HistoryMasterDO, Long>> getAttributes()
  {
    return super.getAttributes();
  }

  @Override
  @Transient
  public Class<? extends JpaTabAttrBaseDO<HistoryMasterDO, ? extends Serializable>> getAttrEntityClass()
  {
    return HistoryAttrDO.class;
  }

  @Override
  @Transient
  public Class<? extends JpaTabAttrBaseDO<HistoryMasterDO, ? extends Serializable>> getAttrEntityWithDataClass()
  {
    return HistoryAttrWithDataDO.class;
  }

  @Override
  @Transient
  public Class<? extends JpaTabAttrDataBaseDO<? extends JpaTabAttrBaseDO<HistoryMasterDO, Long>, Long>> getAttrDataEntityClass()
  {
    return HistoryAttrDataDO.class;
  }

  @Override
  public JpaTabAttrBaseDO<HistoryMasterDO, Long> createAttrEntity(String key, char type, String value)
  {
    return new HistoryAttrDO(this, key, type, value);
  }

  @Override
  public JpaTabAttrBaseDO<HistoryMasterDO, Long> createAttrEntityWithData(String key, char type, String value)
  {
    return new HistoryAttrWithDataDO(this, key, type, value);
  }

  @Override
  @Enumerated(EnumType.STRING)
  @Column(name = "ENTITY_OPTYPE")
  public EntityOpType getEntityOpType()
  {
    return entityOpType;
  }

  public void setEntityOpType(EntityOpType entityOpType)
  {
    this.entityOpType = entityOpType;
  }

  @Override
  @Column(name = "ENTITY_NAME", length = 64, nullable = false)
  public String getEntityName()
  {
    return entityName;
  }

  public void setEntityName(String entityName)
  {
    this.entityName = entityName;
  }

  @Override
  @Column(name = "ENTITY_ID", nullable = false)
  public Long getEntityId()
  {
    return entityId;
  }

  public void setEntityId(Long entityId)
  {
    this.entityId = entityId;
  }

  @Override
  @Column(name = "USER_COMMENT", length = 2000)
  public String getUserComment()
  {
    return userComment;
  }

  public void setUserComment(String userComment)
  {
    this.userComment = userComment;
  }

  @Override
  @Column(name = "TRANSACTION_ID", length = 64)
  public String getTransactionId()
  {
    return transactionId;
  }

  public void setTransactionId(String transactionId)
  {
    this.transactionId = transactionId;
  }

  @Override
  @Transient
  public List<DiffEntry> getDiffEntries()
  {
    return HistoryServiceManager.get().getHistoryService().getDiffEntriesForHistoryMaster(this);
  }
}
