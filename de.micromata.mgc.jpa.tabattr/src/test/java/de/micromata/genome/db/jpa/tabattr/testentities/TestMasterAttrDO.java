//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.db.jpa.tabattr.testentities;

import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrBaseDO;
import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrDataBaseDO;
import de.micromata.genome.db.jpa.tabattr.entities.JpaTabMasterBaseDO;
import java.util.Date;
import java.util.Map;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.junit.Ignore;

@Ignore
@Entity
@Table(name = "TB_TST_ATTRMASTER", indexes = {
    @Index(name = "IX_TST_ATTRMASTER_MODAT", columnList = "MODIFIEDAT"),
    @Index(name = "IX_TST_ATTRMASTER_EKPT", columnList = "EKP"),
    @Index(name = "IX_TST_ATTRMASTER_ABRN", columnList = "ABRN"),
    @Index(name = "IX_TST_ATTRMASTER_SHPNR", columnList = "shipmentNumber"),
    @Index(name = "IX_TST_ATTRMASTER_GP", columnList = "groupProfile"),
    @Index(name = "IX_TST_ATTRMASTER_SHMST", columnList = "shipmentStatus"),
    @Index(name = "IX_TST_ATTRMASTER_SN1", columnList = "senderName1"),
    @Index(name = "IX_TST_ATTRMASTER_SN2", columnList = "senderName2"),
    @Index(name = "IX_TST_ATTRMASTER_RN1", columnList = "recvName1"),
    @Index(name = "IX_TST_ATTRMASTER_SN2", columnList = "senderName2"),
    @Index(name = "IX_TST_ATTRMASTER_RSTR", columnList = "recvStreet"),
    @Index(name = "IX_TST_ATTRMASTER_RPLZ", columnList = "recvPlz"),
    @Index(name = "IX_TST_ATTRMASTER_RREF", columnList = "recvReference"),
})
@SequenceGenerator(name = "SQ_TST_ATTRMASTER_PK", sequenceName = "SQ_TST_ATTRMASTER_PK")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class TestMasterAttrDO extends JpaTabMasterBaseDO<TestMasterAttrDO, Long>
{

  private static final long serialVersionUID = 2899584659368016740L;

  @Column(length = 10, nullable = false, columnDefinition = "VARCHAR2")
  private String ekp;

  @Column(length = 14, nullable = false, columnDefinition = "VARCHAR2")
  private String abrn;

  @Column(length = 50, columnDefinition = "VARCHAR2")
  private String groupProfile;

  private Long userFk;

  @Column(length = 15, columnDefinition = "VARCHAR2")
  private String shipmentStatus;

  @Column(length = 50, columnDefinition = "VARCHAR2")
  private String shipmentNumber;

  private Long manifestFk;

  @Column(length = 64, nullable = false, columnDefinition = "VARCHAR2")
  private String product;

  @Column(length = 64, nullable = false, columnDefinition = "VARCHAR2")
  private String productModelVersion;

  private Date lastPrinted;

  private Integer printCount;

  @Column(length = 35, columnDefinition = "NVARCHAR2")
  private String senderReference;

  @Column(length = 35, columnDefinition = "NVARCHAR2")
  private String senderName1;

  @Column(length = 35, columnDefinition = "NVARCHAR2")
  private String senderName2;

  @Column(length = 35, columnDefinition = "NVARCHAR2")
  private String senderName3;

  @Column(length = 35, columnDefinition = "NVARCHAR2")
  private String senderStreet;

  @Column(length = 5, columnDefinition = "NVARCHAR2")
  private String senderHouseNumber;

  @Column(length = 17, columnDefinition = "NVARCHAR2")
  private String senderPlz;

  @Column(length = 35, columnDefinition = "NVARCHAR2")
  private String senderCity;

  @Column(length = 9, columnDefinition = "VARCHAR2")
  private String senderProvince;

  @Column(length = 70, columnDefinition = "NVARCHAR2")
  private String senderEmail;

  @Column(length = 35, columnDefinition = "NVARCHAR2")
  private String senderPhone;

  @Column(length = 3, columnDefinition = "VARCHAR2")
  private String senderCountry;

  @Column(length = 35, columnDefinition = "NVARCHAR2")
  private String recvName1;

  @Column(length = 35, columnDefinition = "NVARCHAR2")
  private String recvName2;

  @Column(length = 35, columnDefinition = "NVARCHAR2")
  private String recvName3;

  @Column(length = 35, columnDefinition = "NVARCHAR2")
  private String recvStreet;

  @Column(length = 5, columnDefinition = "NVARCHAR2")
  private String recvHouseNumber;

  @Column(length = 17, columnDefinition = "NVARCHAR2")
  private String recvPlz;

  @Column(length = 35, columnDefinition = "NVARCHAR2")
  private String recvCity;

  @Column(length = 9, columnDefinition = "VARCHAR2")
  private String recvProvince;

  @Column(length = 70, columnDefinition = "NVARCHAR2")
  private String recvEmail;

  @Column(length = 35, columnDefinition = "NVARCHAR2")
  private String recvPhone;

  @Column(length = 3, columnDefinition = "VARCHAR2")
  private String recvCountry;

  @Column(length = 35, columnDefinition = "NVARCHAR2")
  private String recvReference;

  @Column(length = 35, columnDefinition = "NVARCHAR2")
  private String shipmentReference;

  @Column(length = 50, columnDefinition = "VARCHAR2")
  private String routingCode;

  @Override
  @Id
  @Column(name = "TST_ATTRMASTER")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TST_ATTRMASTER_PK")
  public Long getPk()
  {
    return pk;
  }

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", targetEntity = TestTabAttrDO.class, orphanRemoval = true,
      fetch = FetchType.EAGER)
  @MapKey(name = "propertyName")
  @Override
  public Map<String, JpaTabAttrBaseDO<TestMasterAttrDO, Long>> getAttributes()
  {
    return super.getAttributes();
  }

  @Transient
  @Override
  public Class<? extends JpaTabAttrBaseDO<TestMasterAttrDO, Long>> getAttrEntityClass()
  {
    return TestTabAttrDO.class;
  }

  @Override
  public JpaTabAttrBaseDO<TestMasterAttrDO, Long> createAttrEntity(String key, char type, String value)
  {
    return new TestTabAttrDO(this, key, type, value);
  }

  @Transient
  @Override
  public Class<? extends JpaTabAttrBaseDO<TestMasterAttrDO, Long>> getAttrEntityWithDataClass()
  {
    return TestTabAttrWithDataDO.class;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Transient
  @Override
  public Class<? extends JpaTabAttrDataBaseDO<? extends JpaTabAttrBaseDO<TestMasterAttrDO, Long>, Long>> getAttrDataEntityClass()
  {
    return TestTabAttrDataDO.class;
  }

  @Override
  public JpaTabAttrBaseDO<TestMasterAttrDO, Long> createAttrEntityWithData(String key, char type, String value)
  {
    return new TestTabAttrWithDataDO(this, key, type, value);
  }

  public String getEkp()
  {
    return ekp;
  }

  public void setEkp(String ekp)
  {
    this.ekp = ekp;
  }

  public String getAbrn()
  {
    return abrn;
  }

  public void setAbrn(String abrn)
  {
    this.abrn = abrn;
  }

  public String getGroupProfile()
  {
    return groupProfile;
  }

  public Long getUserFk()
  {
    return userFk;
  }

  public void setUserFk(Long userFk)
  {
    this.userFk = userFk;
  }

  public String getShipmentStatus()
  {
    return shipmentStatus;
  }

  public void setShipmentStatus(String shipmentStatus)
  {
    this.shipmentStatus = shipmentStatus;
  }

  public Long getManifestFk()
  {
    return manifestFk;
  }

  public void setManifestFk(Long manifestFk)
  {
    this.manifestFk = manifestFk;
  }

  public String getProduct()
  {
    return product;
  }

  public void setProduct(String product)
  {
    this.product = product;
  }

  public String getProductModelVersion()
  {
    return productModelVersion;
  }

  public void setProductModelVersion(String productModelVersion)
  {
    this.productModelVersion = productModelVersion;
  }

  public Date getLastPrinted()
  {
    return lastPrinted;
  }

  public void setLastPrinted(Date lastPrinted)
  {
    this.lastPrinted = lastPrinted;
  }

  public Integer getPrintCount()
  {
    return printCount;
  }

  public void setPrintCount(Integer printCount)
  {
    this.printCount = printCount;
  }

  public String getSenderName1()
  {
    return senderName1;
  }

  public void setSenderName1(String senderName1)
  {
    this.senderName1 = senderName1;
  }

  public String getSenderName2()
  {
    return senderName2;
  }

  public void setSenderName2(String senderName2)
  {
    this.senderName2 = senderName2;
  }

  public String getSenderName3()
  {
    return senderName3;
  }

  public void setSenderName3(String senderName3)
  {
    this.senderName3 = senderName3;
  }

  public String getSenderStreet()
  {
    return senderStreet;
  }

  public void setSenderStreet(String senderStreet)
  {
    this.senderStreet = senderStreet;
  }

  public String getSenderHouseNumber()
  {
    return senderHouseNumber;
  }

  public void setSenderHouseNumber(String senderHouseNumber)
  {
    this.senderHouseNumber = senderHouseNumber;
  }

  public String getSenderReference()
  {
    return senderReference;
  }

  public void setSenderReference(String senderReference)
  {
    this.senderReference = senderReference;
  }

  public String getSenderPlz()
  {
    return senderPlz;
  }

  public void setSenderPlz(String senderPlz)
  {
    this.senderPlz = senderPlz;
  }

  public String getSenderCity()
  {
    return senderCity;
  }

  public void setSenderCity(String senderCity)
  {
    this.senderCity = senderCity;
  }

  public String getSenderProvince()
  {
    return senderProvince;
  }

  public void setSenderProvince(String senderProvince)
  {
    this.senderProvince = senderProvince;
  }

  public String getSenderEmail()
  {
    return senderEmail;
  }

  public void setSenderEmail(String senderEmail)
  {
    this.senderEmail = senderEmail;
  }

  public String getSenderPhone()
  {
    return senderPhone;
  }

  public void setSenderPhone(String senderPhone)
  {
    this.senderPhone = senderPhone;
  }

  public String getSenderCountry()
  {
    return senderCountry;
  }

  public void setSenderCountry(String senderCountry)
  {
    this.senderCountry = senderCountry;
  }

  public String getRecvName1()
  {
    return recvName1;
  }

  public void setRecvName1(String recvName1)
  {
    this.recvName1 = recvName1;
  }

  public String getRecvName2()
  {
    return recvName2;
  }

  public void setRecvName2(String recvName2)
  {
    this.recvName2 = recvName2;
  }

  public String getRecvName3()
  {
    return recvName3;
  }

  public void setRecvName3(String recvName3)
  {
    this.recvName3 = recvName3;
  }

  public String getRecvStreet()
  {
    return recvStreet;
  }

  public void setRecvStreet(String recvStreet)
  {
    this.recvStreet = recvStreet;
  }

  public String getRecvHouseNumber()
  {
    return recvHouseNumber;
  }

  public void setRecvHouseNumber(String recvHouseNumber)
  {
    this.recvHouseNumber = recvHouseNumber;
  }

  public String getRecvPlz()
  {
    return recvPlz;
  }

  public void setRecvPlz(String recvPlz)
  {
    this.recvPlz = recvPlz;
  }

  public String getRecvCity()
  {
    return recvCity;
  }

  public void setRecvCity(String recvCity)
  {
    this.recvCity = recvCity;
  }

  public String getRecvProvince()
  {
    return recvProvince;
  }

  public void setRecvProvince(String recvProvince)
  {
    this.recvProvince = recvProvince;
  }

  public String getRecvEmail()
  {
    return recvEmail;
  }

  public void setRecvEmail(String recvEmail)
  {
    this.recvEmail = recvEmail;
  }

  public String getRecvPhone()
  {
    return recvPhone;
  }

  public void setRecvPhone(String recvPhone)
  {
    this.recvPhone = recvPhone;
  }

  public String getRecvCountry()
  {
    return recvCountry;
  }

  public void setRecvCountry(String recvCountry)
  {
    this.recvCountry = recvCountry;
  }

  public String getRecvReference()
  {
    return recvReference;
  }

  public void setRecvReference(String recvReference)
  {
    this.recvReference = recvReference;
  }

  public String getShipmentReference()
  {
    return shipmentReference;
  }

  public void setShipmentReference(String shipmentReference)
  {
    this.shipmentReference = shipmentReference;
  }

  public String getShipmentNumber()
  {
    return shipmentNumber;
  }

  public void setShipmentNumber(String shipmentNumber)
  {
    this.shipmentNumber = shipmentNumber;
  }

  public String getRoutingCode()
  {
    return routingCode;
  }

  public void setRoutingCode(String routingCode)
  {
    this.routingCode = routingCode;
  }

  public void setGroupProfile(String groupProfile)
  {
    this.groupProfile = groupProfile;
  }

}
