/*
 Created on 08.01.2008
 */
package de.micromata.genome.logging;

/**
 * Logging Attributes used inside Genome.
 *
 * @author roger@micromata.de
 */
public enum GenomeAttributeType implements LogAttributeType
{

  /**
   * The Request url.
   */
  RequestUrl(new RequestUrlDefaultFiller()),

  /**
   * The Http session id.
   */
  HttpSessionId("HTTPSESSIONID", 120, new HttpSessionIdFiller(), new SearchKeyLogAttributeRenderer()),

  /**
   * The Node name.
   */
  NodeName("NODENAME", 32, new HostNameDefaultFiller(), new SearchKeyLogAttributeRenderer()),

  /**
   * The Thread context.
   */
  ThreadContext,

  /**
   * The Tech reason exception.
   */
  TechReasonException,

  /**
   * The Stacktrace.
   */
  Stacktrace,

  /**
   * The Http client dump.
   */
  HttpClientDump,

  /**
   * The Sql statement.
   */
  SqlStatement(new CurrentSqlFiller()),

  /**
   * The Sql args.
   */
  SqlArgs,
  /**
   * SQL State.
   *
   * Often contains db error codes.
   */
  SqlState,

  /**
   * The Sql resolved statement.
   */
  SqlResolvedStatement,

  /**
   * The Miscellaneous.
   */
  Miscellaneous,

  /**
   * The Miscellaneous2.
   */
  Miscellaneous2,

  /**
   * The Timepending table.
   */
  TimependingTable,

  /**
   * The Request params.
   */
  RequestParams,

  /**
   * The Out request url.
   */
  OutRequestUrl,

  /**
   * A complete dump of a request.
   */
  HttpRequestDump,

  /**
   * A complete Dump of a response.
   */
  HttpResponseDump,

  /**
   * The Time in ms.
   */
  TimeInMs,

  /**
   * The Resource id.
   */
  ResourceId,

  /**
   * The Config domain.
   */
  ConfigDomain,

  /**
   * The Resource name.
   */
  ResourceName,

  /**
   * The Res version id.
   */
  ResVersionId,

  /**
   * The Plugin name.
   */
  PluginName,

  /**
   * The Class name.
   */
  ClassName,

  /**
   * The Job event.
   */
  JobEvent,

  /**
   * The Purpose.
   */
  Purpose,

  /**
   * The Admin user name.
   */
  AdminUserName("USERNAME", 50, new AdminUserDefaultFiller(), new SearchKeyLogAttributeRenderer()),

  /**
   * The I18 n key.
   */
  I18NKey,

  /**
   * The Http status.
   */
  HttpStatus,

  /**
   * The Back trace.
   */
  BackTrace,

  /**
   * The Perf type.
   */
  PerfType,

  /**
   * The Perf avg time.
   */
  PerfAvgTime,

  /**
   * The Perf max time.
   */
  PerfMaxTime,

  /**
   * The Perf min time.
   */
  PerfMinTime,

  /**
   * The Perf sample count.
   */
  PerfSampleCount,

  /**
   * The Genome job id.
   */
  GenomeJobId,

  /**
   * URL of the outgoing communication.
   */
  OutHttpUrl,

  /**
   * Name of the call of the outgpoing communication.
   */
  OutHttpCall,

  /**
   * The Email message.
   */
  EmailMessage,

  /**
   * The User email.
   */
  UserEmail,

  /**
   * The Ftp terminal log.
   */
  FtpTerminalLog,

  /**
   * The Terminal log.
   */
  TerminalLog,

  /**
   * The Flow log.
   */
  FlowLog,

  /**
   * The File name.
   */
  FileName,

  /**
   * The User agent.
   */
  UserAgent,

  /**
   * The Root log category.
   */
  RootLogCategory,

  /**
   * The Root log message.
   */
  RootLogMessage,

  /**
   * The Pattern.
   */
  Pattern,
  /**
   * The Org unit name.
   */
  OrgUnitName,

  /**
   * The Message stack trace.
   */
  MessageStackTrace,

  /**
   * The Exit status.
   */
  /*
   * Exit Status eines Prozesses
   */
  ExitStatus,

  /**
   * The Expetected exit status.
   */
  ExpetectedExitStatus,

  /**
   * The Stderr.
   */
  Stderr,

  /**
   * The Stdout.
   */
  Stdout,

  /**
   * Referer HTTP Header.
   */
  Referer,

  /**
   * The Url.
   */
  Url,

  /**
   * The Error code.
   */
  ErrorCode,

  /**
   * The Error message.
   */
  ErrorMessage,

  /**
   * The State.
   */
  State,

  ;
  static {
    BaseLogging.registerLogAttributeType(values());
  }

  /**
   * The column name.
   */
  private String columnName;

  /**
   * The max size.
   */
  private int maxSize = -1;

  /**
   * The attribute default filler.
   */
  private AttributeTypeDefaultFiller attributeDefaultFiller;

  /**
   * The renderer.
   */
  private LogAttributeRenderer renderer = null;

  /**
   * Instantiates a new genome attribute type.
   */
  private GenomeAttributeType()
  {

  }

  /**
   * Instantiates a new genome attribute type.
   *
   * @param renderer the renderer
   */
  private GenomeAttributeType(LogAttributeRenderer renderer)
  {
    this(null, -1, null, renderer);
  }

  /**
   * Instantiates a new genome attribute type.
   *
   * @param columnName the column name
   * @param maxSize the max size
   */
  private GenomeAttributeType(String columnName, int maxSize)
  {
    this.columnName = columnName;
    this.maxSize = maxSize;
  }

  /**
   * Instantiates a new genome attribute type.
   *
   * @param columnName the column name
   * @param maxSize the max size
   * @param defaultFiller the default filler
   * @param renderer the renderer
   */
  private GenomeAttributeType(String columnName, int maxSize, AttributeTypeDefaultFiller defaultFiller,
      LogAttributeRenderer renderer)
  {
    this.columnName = columnName;
    this.maxSize = maxSize;
    this.attributeDefaultFiller = defaultFiller;
    this.renderer = renderer;
  }

  /**
   * Instantiates a new genome attribute type.
   *
   * @param columnName the column name
   * @param maxSize the max size
   * @param defaultFiller the default filler
   */
  private GenomeAttributeType(String columnName, int maxSize, AttributeTypeDefaultFiller defaultFiller)
  {
    this(columnName, maxSize, defaultFiller, new DefaultLogAttributeRenderer());
  }

  /**
   * Instantiates a new genome attribute type.
   *
   * @param defaultFiller the default filler
   * @param renderer the renderer
   */
  private GenomeAttributeType(AttributeTypeDefaultFiller defaultFiller, LogAttributeRenderer renderer)
  {
    this(null, -1, defaultFiller, renderer);
  }

  /**
   * Instantiates a new genome attribute type.
   *
   * @param defaultFiller the default filler
   */
  private GenomeAttributeType(AttributeTypeDefaultFiller defaultFiller)
  {
    this(null, -1, defaultFiller, new DefaultLogAttributeRenderer());
  }

  @Override
  public AttributeTypeDefaultFiller getAttributeDefaultFiller()
  {
    return attributeDefaultFiller;
  }

  @Override
  public String columnName()
  {
    return columnName;
  }

  @Override
  public boolean isSearchKey()
  {
    return columnName != null;
  }

  @Override
  public int maxValueSize()
  {
    return maxSize;
  }

  @Override
  public LogAttributeRenderer getRenderer()
  {
    if (this.renderer == null) {
      return DefaultLogAttributeRenderer.INSTANCE;
    }
    return this.renderer;
  }

  public void setRenderer(LogAttributeRenderer renderer)
  {
    this.renderer = renderer;
  }

}
