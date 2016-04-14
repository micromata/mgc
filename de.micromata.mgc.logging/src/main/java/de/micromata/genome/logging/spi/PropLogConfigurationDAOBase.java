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

package de.micromata.genome.logging.spi;

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.logging.LogConfigurationDAO;
import de.micromata.genome.logging.LogEntry;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.matcher.MatcherFactory;
import de.micromata.genome.util.matcher.string.SimpleWildcardMatcherFactory;
import de.micromata.genome.util.types.Pair;

/**
 * Manage the logging in a property format.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class PropLogConfigurationDAOBase implements LogConfigurationDAO
{

  /**
   * The Constant CACHE_NAME.
   */
  final static public String CACHE_NAME = "GENOME.LOGGING.AttrLogConfiguration";

  /**
   * The Constant THRESHOLD_NAME.
   */
  final static public String THRESHOLD_NAME = "Threshold";

  /**
   * The max threshold.
   */
  private int maxThreshold = LogLevel.Note.getLevel();

  /**
   * The pattern.
   */
  protected List<Pair<Matcher<String>, Integer>> pattern = new ArrayList<Pair<Matcher<String>, Integer>>();

  /**
   * The matcher factory.
   */
  protected MatcherFactory<String> matcherFactory = new SimpleWildcardMatcherFactory<String>();

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.logging.LogConfigurationDAO#setLogLevel(de.micromata.genome.logging.LogLevel,
   * java.lang.String)
   */
  @Override
  public abstract void setLogLevel(LogLevel logLevel, String pattern);

  /**
   * Builds the pattern.
   */
  protected abstract void buildPattern();

  @Override
  public LogLevel getThreshold()
  {
    return LogLevel.getLevelFrom(maxThreshold);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.logging.LogConfigurationDAO#setThreshold(de.micromata.genome.logging.LogLevel)
   */
  @Override
  public void setThreshold(LogLevel logLevel)
  {
    maxThreshold = logLevel.getLevel();
    setLogLevel(logLevel, THRESHOLD_NAME);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.logging.LogConfigurationDAO#isLogEnabled(de.micromata.genome.logging.LogLevel)
   */
  @Override
  public boolean isLogEnabled(LogLevel logLevel)
  {

    if (logLevel.getLevel() < maxThreshold) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.logging.LogConfigurationDAO#isLogEnabled(de.micromata.genome.logging.LogLevel,
   * java.lang.String, java.lang.String)
   */
  @Override
  public boolean isLogEnabled(LogLevel logLevel, String categoryNamePart, String msg)
  {
    String merged = categoryNamePart + "." + msg;
    int ll = logLevel.getLevel();
    if (ll < maxThreshold) {
      return false;
    }
    for (Pair<Matcher<String>, Integer> p : pattern) {
      if (p.getFirst().match(merged) == true) {
        if (p.getSecond() > ll) {
          return false;
        }
        return true;
      }
    }
    return true;
  }

  @Override
  public void resetLogLevelRules()
  {
    List<Pair<String, LogLevel>> pl = getLogLevelRules();
    for (Pair<String, LogLevel> p : pl) {
      setLogLevel(null, p.getFirst());
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.logging.LogConfigurationDAO#getLogLevelRules()
   */
  @Override
  public List<Pair<String, LogLevel>> getLogLevelRules()
  {
    List<Pair<String, LogLevel>> ret = new ArrayList<Pair<String, LogLevel>>();

    for (Pair<Matcher<String>, Integer> p : pattern) {
      ret.add(
          new Pair<String, LogLevel>(matcherFactory.getRuleString(p.getFirst()), LogLevel.getLevelFrom(p.getSecond())));
    }
    return ret;
  }

  @Override
  public boolean filterView(LogEntry lwe)
  {
    return true;
  }

  public int getMaxThreshold()
  {
    return maxThreshold;
  }

  public void setMaxThreshold(int maxThreshold)
  {
    this.maxThreshold = maxThreshold;
  }

  public List<Pair<Matcher<String>, Integer>> getPattern()
  {
    return pattern;
  }

  public void setPattern(List<Pair<Matcher<String>, Integer>> pattern)
  {
    this.pattern = pattern;
  }

  public MatcherFactory<String> getMatcherFactory()
  {
    return matcherFactory;
  }

  public void setMatcherFactory(MatcherFactory<String> matcherFactory)
  {
    this.matcherFactory = matcherFactory;
  }

}
