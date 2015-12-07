package de.micromata.genome.logging;

import org.apache.commons.lang.ArrayUtils;

/**
 * A Stacktrace.
 *
 * @author roger
 */
public class LogStacktraceAttribute extends LogAttribute
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 752946809919859766L;

  /**
   * from top to down skip these package stacktraces.
   */
  private String[] skipLeadingStacktraces;

  /**
   * The stack trace elements.
   */
  private StackTraceElement[] stackTraceElements;

  /**
   * The rendered stacktrace.
   */
  private String renderedStacktrace;

  /**
   * Instantiates a new log stacktrace attribute.
   *
   * @param skipLeadingStacktraces the skip leading stacktraces
   */
  public LogStacktraceAttribute(String... skipLeadingStacktraces)
  {
    super(GenomeAttributeType.Stacktrace, "");
    this.skipLeadingStacktraces = (String[]) ArrayUtils.add(skipLeadingStacktraces, "de.micromata.genome.logging.LogStacktraceAttribute");
    stackTraceElements = new Throwable().getStackTrace();
  }

  /**
   * Instantiates a new log stacktrace attribute.
   */
  public LogStacktraceAttribute()
  {
    super(GenomeAttributeType.Stacktrace, "");
    stackTraceElements = new Throwable().getStackTrace();
    skipLeadingStacktraces = new String[] { "de.micromata.genome.logging.LogStacktraceAttribute"};
  }

  /**
   * Skip stacktrace.
   *
   * @param sc the sc
   * @return true, if successful
   */
  protected boolean skipStacktrace(StackTraceElement sc)
  {
    String l = sc.toString();
    for (String s : skipLeadingStacktraces) {
      if (l.startsWith(s) == true) {
        return true;
      }
    }
    return false;
  }

  /**
   * Render stack trace.
   */
  protected void renderStackTrace()
  {
    StringBuilder sb = new StringBuilder();
    boolean skippedLeading = true;
    for (int i = 0; i < this.stackTraceElements.length; i++) {
      if (skippedLeading == true && skipStacktrace(stackTraceElements[i]) == true) {
        continue;
      }
      sb.append("" + stackTraceElements[i] + "\n");
      skippedLeading = false;
    }
    renderedStacktrace = sb.toString();
  }

  @Override
  public String getValue()
  {
    if (renderedStacktrace == null) {
      renderStackTrace();
    }
    return renderedStacktrace;

  }

  public StackTraceElement[] getStackTraceElements()
  {
    return stackTraceElements;
  }

  public void setStackTraceElements(StackTraceElement[] stackTraceElements)
  {
    this.stackTraceElements = stackTraceElements;
  }

  public String getRenderedStacktrace()
  {
    return renderedStacktrace;
  }

  public void setRenderedStacktrace(String renderedStacktrace)
  {
    this.renderedStacktrace = renderedStacktrace;
  }
}
