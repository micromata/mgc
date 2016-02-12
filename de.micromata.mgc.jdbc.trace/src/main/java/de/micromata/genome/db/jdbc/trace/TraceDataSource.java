package de.micromata.genome.db.jdbc.trace;

import javax.sql.DataSource;

import de.micromata.genome.db.jdbc.wrapper.DataSourceWrapper;
import de.micromata.genome.logging.LogCategory;

/**
 * experimental.
 *
 * @author roger
 */
public class TraceDataSource extends DataSourceWrapper
{

  /**
   * The trace config.
   */
  private TraceConfig traceConfig = new TraceConfig();

  /**
   * Instantiates a new trace data source.
   */
  public TraceDataSource()
  {

  }

  /**
   * Instantiates a new trace data source.
   *
   * @param nestedDataSource the nested data source
   */
  public TraceDataSource(DataSource nestedDataSource)
  {
    super(nestedDataSource, new TraceSqlWrapperFactory());
  }

  /**
   * Instantiates a new trace data source.
   *
   * @param nestedDataSource the nested data source
   * @param traceConfig the trace config
   */
  public TraceDataSource(DataSource nestedDataSource, TraceConfig traceConfig)
  {
    this(nestedDataSource);
    this.traceConfig = traceConfig;
  }

  /**
   * Instantiates a new trace data source.
   *
   * @param nestedDataSource the nested data source
   * @param logCategory the log category
   */
  public TraceDataSource(DataSource nestedDataSource, LogCategory logCategory)
  {
    super(nestedDataSource, new TraceSqlWrapperFactory());
    traceConfig.setLogCategory(logCategory);
  }

  public TraceConfig getTraceConfig()
  {
    return traceConfig;
  }

  public void setTraceConfig(TraceConfig traceConfig)
  {
    this.traceConfig = traceConfig;
  }

}
