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

package de.micromata.genome.util.validation;

import java.io.Serializable;
import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import de.micromata.genome.util.i18n.I18NTranslationProvider;

/**
 * A Validation Message.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ValMessage implements Serializable
{

  /**
   *
  */
  private static final long serialVersionUID = 8082998849698310422L;
  /**
   * The current validation state, initially set to "unvalidated".
   */
  private ValState valState = ValState.Unvalidated;
  /**
   * Optional reference to an object this message is related to.
   */
  private Object reference;
  /**
   * Affected property.
   */
  private String property;

  /**
   * I18N Key to display.
   */
  private String i18nkey;

  /**
   * Variable Arguments, passt to i18n.
   */
  private Object[] arguments = new Object[] {};

  /**
   * The translated message.
   *
   * Only will be set, if UsmStage.Translate is set.
   */
  private String message;
  /**
   * If any exception occours.
   */
  private Exception exception;
  /**
   * Internal state if this message is already shown.
   */
  private boolean consumed = false;

  public ValMessage()
  {

  }

  public ValMessage(ValState valState, String i18nkey)
  {
    this(valState, i18nkey, (Object[]) null);
  }

  public ValMessage(ValState valState, String i18nkey, Exception ex)
  {
    this(valState, i18nkey, (Object[]) null);
    this.exception = ex;
  }

  public ValMessage(ValState valState, String i18nkey, Exception ex, Object[] arguments)
  {
    this.valState = valState;
    this.i18nkey = i18nkey;
    this.exception = ex;
    if (arguments != null) {
      this.arguments = arguments;
    }
  }

  public ValMessage(ValState valState, String i18nkey, Object... arguments)
  {
    this(valState, i18nkey, null, arguments);
  }

  public void addProperty(String property)
  {
    if (StringUtils.isBlank(property) == true) {
      return;
    }
    if (StringUtils.isBlank(this.property) == true) {
      this.property = property;
    } else {
      this.property = this.property + '.' + property;
    }
  }

  public String getTranslatedMessage(I18NTranslationProvider transprov)
  {
    if (message != null) {
      return message;
    }
    String msg = (String) transprov.getTranslationForKey(getI18nkey());
    if (getArguments() != null && getArguments().length > 0) {
      msg = MessageFormat.format(msg, getArguments());
    } else if (exception != null && StringUtils.contains(msg, "{0}") == true) {
      msg = MessageFormat.format(msg, exception.getMessage());

    }
    message = msg;
    return message;
  }

  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

  public ValState getValState()
  {
    return valState;
  }

  public void setValState(ValState valState)
  {
    this.valState = valState;
  }

  public Object getReference()
  {
    return reference;
  }

  public void setReference(Object reference)
  {
    this.reference = reference;
  }

  public String getProperty()
  {
    return property;
  }

  public void setProperty(String property)
  {
    this.property = property;
  }

  public String getI18nkey()
  {
    return i18nkey;
  }

  public void setI18nkey(String i18nkey)
  {
    this.i18nkey = i18nkey;
  }

  public Object[] getArguments()
  {
    return arguments;
  }

  public void setArguments(Object[] arguments)
  {
    this.arguments = arguments;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage(String message)
  {
    this.message = message;
  }

  public Exception getException()
  {
    return exception;
  }

  public void setException(Exception exception)
  {
    this.exception = exception;
  }

  public void consume()
  {
    consumed = true;
  }

  public boolean isConsumed()
  {
    return consumed;
  }

  public void setConsumed(boolean consumed)
  {
    this.consumed = consumed;
  }

}
