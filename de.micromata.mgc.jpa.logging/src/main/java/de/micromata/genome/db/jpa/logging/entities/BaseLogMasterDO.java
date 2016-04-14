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

package de.micromata.genome.db.jpa.logging.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.jpa.ComplexEntity;
import de.micromata.genome.jpa.ComplexEntityVisitor;
import de.micromata.genome.jpa.StdRecordDO;

/**
 * The Class BaseLogMaster.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
@MappedSuperclass
public abstract class BaseLogMasterDO<M extends BaseLogMasterDO<?>>extends StdRecordDO<Long> implements ComplexEntity
{

  /**
   * The Constant MAX_LOG_MESSAGE_SIZE.
   */
  private static final int MAX_LOG_MESSAGE_SIZE = 254;

  /**
   * The Constant MAX_LOG_SHORT_MESSAGE_SIZE.
   */
  private static final int MAX_LOG_SHORT_MESSAGE_SIZE = 31;

  /**
   * The category.
   */
  private String category;

  /**
   * The loglevel.
   */
  private short loglevel;

  /**
   * The message.
   */
  private String message;

  /**
   * The shortmessage.
   */
  private String shortmessage;

  /**
   * The httpsessionid.
   */
  private String httpsessionid;

  /**
   * The username.
   */
  private String username;

  /**
   * The nodename.
   */
  private String nodename;

  /**
   * The attributes.
   */
  protected Collection<BaseLogAttributeDO<M>> attributes = new ArrayList<>();

  public abstract BaseLogAttributeDO<M> createAddAttribute();

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public void visit(ComplexEntityVisitor visitor)
  {
    visitor.visit(this);
    if (attributes == null) {
      return;
    }
    for (BaseLogAttributeDO la : attributes) {
      visitor.visit(la);
    }

  }

  @Transient
  public Collection<BaseLogAttributeDO<M>> getAttributes()
  {
    return attributes;
  }

  /**
   * Sets the attributes.
   *
   * @param attributes the new attributes
   */
  public void setAttributes(Collection<BaseLogAttributeDO<M>> attributes)
  {
    this.attributes = attributes;
  }

  /**
   * Gets the category.
   *
   * @return the category
   */
  @Column(name = "CATEGORY", length = 30)
  public String getCategory()
  {
    return category;
  }

  /**
   * Sets the category.
   *
   * @param category the new category
   */
  public void setCategory(String category)
  {
    this.category = category;
  }

  /**
   * Gets the loglevel.
   *
   * @return the loglevel
   */
  @Column(name = "LOGLEVEL")
  public short getLoglevel()
  {
    return loglevel;
  }

  /**
   * Sets the loglevel.
   *
   * @param loglevel the new loglevel
   */
  public void setLoglevel(short loglevel)
  {
    this.loglevel = loglevel;
  }

  /**
   * Gets the message.
   *
   * @return the message
   */
  @Column(name = "MESSAGE", length = MAX_LOG_MESSAGE_SIZE + 1)
  public String getMessage()
  {
    return message;
  }

  /**
   * Sets the message.
   *
   * @param message the new message
   */
  public void setMessage(String message)
  {
    this.message = StringUtils.substring(message, 0, MAX_LOG_MESSAGE_SIZE);
  }

  /**
   * Gets the shortmessage.
   *
   * @return the shortmessage
   */
  @Column(name = "SHORTMESSAGE", length = MAX_LOG_SHORT_MESSAGE_SIZE + 1)
  public String getShortmessage()
  {
    return shortmessage;
  }

  /**
   * Sets the shortmessage.
   *
   * @param shortmessage the new shortmessage
   */
  public void setShortmessage(String shortmessage)
  {
    this.shortmessage = StringUtils.substring(shortmessage, 0, MAX_LOG_SHORT_MESSAGE_SIZE);
  }

  /**
   * Gets the httpsessionid.
   *
   * @return the httpsessionid
   */
  @Column(name = "HTTPSESSIONID", length = 120)
  @EntityLogSearchAttribute(enumName = "HttpSessionId")
  public String getHttpsessionid()
  {
    return httpsessionid;
  }

  /**
   * Sets the httpsessionid.
   *
   * @param httpsesseionid the new httpsessionid
   */
  public void setHttpsessionid(String httpsesseionid)
  {
    this.httpsessionid = httpsesseionid;
  }

  /**
   * Gets the username.
   *
   * @return the username
   */
  @Column(name = "USERNAME", length = 50)
  @EntityLogSearchAttribute(enumName = "AdminUserName")
  public String getUsername()
  {
    return username;
  }

  /**
   * Sets the username.
   *
   * @param username the new username
   */
  public void setUsername(String username)
  {
    this.username = username;
  }

  /**
   * Gets the nodename.
   *
   * @return the nodename
   */
  @Column(name = "NODENAME", length = 32)
  @EntityLogSearchAttribute(enumName = "NodeName")
  public String getNodename()
  {
    return nodename;
  }

  /**
   * Sets the nodename.
   *
   * @param nodename the new nodename
   */
  public void setNodename(String nodename)
  {
    this.nodename = nodename;
  }
}
