/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   04.07.2006
// Copyright Micromata 04.07.2006
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

/**
 * Wrapps an Exception with a LogAttribute.
 *
 * @author roger@micromata.de
 */
public class LogExceptionAttribute extends LogAttribute implements WithLogAttributes
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -6868966772934042048L;

  /**
   * The exception.
   */
  private Throwable exception;

  /**
   * Instantiates a new log exception attribute.
   *
   * @param cause the cause
   */
  public LogExceptionAttribute(Throwable cause)
  {
    this(cause, false);
  }

  /**
   * Instantiates a new log exception attribute.
   *
   * @param cause Nie <code>null</code>.
   * @param shortMessage Nur Message, d.h. keinen Stacktrace ausgeben (Ausnahme: NullPointerException).
   */
  public LogExceptionAttribute(Throwable cause, boolean shortMessage)
  {
    super(GenomeAttributeType.TechReasonException, "");
    this.exception = cause;
    if (shortMessage == false || cause instanceof NullPointerException) {
      StringWriter sout = new StringWriter();
      PrintWriter pout = new PrintWriter(sout);
      cause.printStackTrace(pout);
      setValue(sout.getBuffer().toString());
    } else {
      StringBuilder sb = new StringBuilder();
      sb.append(getDescription(cause));
      if (cause.getCause() != null && cause != cause.getCause()) {
        cause = cause.getCause();
        sb.append("\n\t").append(getDescription(cause));
      }
      setValue(sb.toString());
    }
  }

  /**
   * Liefert die Message. Falls die leer ist wird toString zurückgegeben.
   * 
   * @param t Nie <code>null</code>
   * @return Nie <code>null</code>
   */
  protected String getDescription(Throwable t)
  {
    String result = t.getMessage();
    // if (StringUtils.isBlank(result) == true) {
    // result = t.toString();
    // }
    return result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Collection<LogAttribute> getLogAttributes()
  {
    if (exception instanceof WithLogAttributes) {
      return ((WithLogAttributes) exception).getLogAttributes();
    }
    // return CollectionUtils.EMPTY_COLLECTION;
    return null;
  }

  public Throwable getException()
  {
    return exception;
  }

  public void setException(Throwable exception)
  {
    this.exception = exception;
  }

}
