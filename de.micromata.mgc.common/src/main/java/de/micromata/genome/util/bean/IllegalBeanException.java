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

package de.micromata.genome.util.bean;

/**
 * The Class IllegalBeanException.
 *
 * @author roger@micromata.de
 */

public class IllegalBeanException extends IllegalArgumentException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -7373246436602310106L;

  /**
   * The bean.
   */
  private Object bean;

  /**
   * Instantiates a new illegal bean exception.
   *
   * @param bean the bean
   * @param message the message
   */
  public IllegalBeanException(Object bean, String message)
  {
    super(message);
    this.bean = bean;
  }

  public Object getBean()
  {
    return bean;
  }

  public void setBean(Object bean)
  {
    this.bean = bean;
  }
}
