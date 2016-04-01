package de.micromata.genome.logging.spi;

import java.util.List;

import de.micromata.genome.logging.LogConfigurationDAO;
import de.micromata.genome.logging.LogEntry;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.util.types.Pair;

/**
 * Wrapper to LogConfigurationDAO.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LogConfigurationDAOWrapper implements LogConfigurationDAO
{
  private LogConfigurationDAO target;

  public LogConfigurationDAOWrapper()
  {

  }

  public LogConfigurationDAOWrapper(LogConfigurationDAO target)
  {
    this.target = target;
  }

  @Override
  public LogLevel getThreshold()
  {
    return target.getThreshold();
  }

  @Override
  public void setThreshold(LogLevel level)
  {
    target.setThreshold(level);
  }

  @Override
  public boolean isLogEnabled(LogLevel logLevel)
  {
    return target.isLogEnabled(logLevel);
  }

  @Override
  public boolean isLogEnabled(LogLevel logLevel, String category, String msg)
  {
    return target.isLogEnabled(logLevel, category, msg);
  }

  @Override
  public void setLogLevel(LogLevel logLevel, String pattern)
  {
    target.setLogLevel(logLevel, pattern);
  }

  @Override
  public void resetLogLevelRules()
  {
    target.resetLogLevelRules();
  }

  @Override
  public List<Pair<String, LogLevel>> getLogLevelRules()
  {
    return target.getLogLevelRules();
  }

  @Override
  public boolean filterView(LogEntry lwe)
  {
    return target.filterView(lwe);
  }

  public LogConfigurationDAO getTarget()
  {
    return target;
  }

  public void setTarget(LogConfigurationDAO target)
  {
    this.target = target;
  }

}
