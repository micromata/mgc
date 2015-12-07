/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   21.03.2008
// Copyright Micromata 21.03.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.stats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogCategory;
import de.micromata.genome.logging.LogEntry;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.util.matcher.EveryMatcher;
import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.matcher.MatcherFactory;
import de.micromata.genome.util.matcher.string.MatchUtil;
import de.micromata.genome.util.matcher.string.SimpleWildcardMatcherFactory;
import de.micromata.genome.util.runtime.CallableX;
import de.micromata.genome.util.types.Pair;

/**
 * Collectin Data in RAM. Other process (job) just cleanup the stats.
 * 
 * @author roger@micromata.de
 * 
 */
public class StatsDAOImpl implements StatsDAO
{

  /**
   * The stats.
   */
  private static StatsDO stats = new StatsDO();

  /**
   * The cache size class name matcher.
   */
  private Matcher<String> cacheSizeClassNameMatcher = new EveryMatcher<String>();

  public Matcher<String> getCacheSizeClassNameMatcher()
  {
    return cacheSizeClassNameMatcher;
  }

  public void setCacheSizeClassNameMatcher(Matcher<String> cacheSizeClassNameMatcher)
  {
    this.cacheSizeClassNameMatcher = cacheSizeClassNameMatcher;
  }

  /**
   * for this servletPath's use RequestUrl, not servlet Path. Value will be ignored
   */
  private Map<String, Object> useRequestUrl = new HashMap<String, Object>();

  /**
   * The matcher list.
   */
  private List<Pair<Boolean, Matcher<String>>> matcherList;

  /**
   * For the result detail string (servletPath, requestUrl or replaced) use this LogCategory.
   */
  private List<Pair<Matcher<String>, LogCategory>> categoryForRequestDetailRules = null;

  /**
   * The current ops.
   */
  private Set<CurrentOperation> currentOps = new HashSet<CurrentOperation>();

  /**
   * Adds the logging.
   *
   * @param lwe the lwe
   */
  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.stats.StatsDAO#addLogging(de.micromata.genome.logging.LogWriteEntry)
   */
  @Override
  public void addLogging(LogEntry lwe)
  {
    synchronized (stats) {
      stats.getLogStats().setStat(lwe.getCategory(), lwe.getLogLevel());

    }
  }

  /**
   * Do add performance.
   *
   * @param category the category
   * @param pointName the point name
   * @return true, if successful
   */
  protected boolean doAddPerformance(LogCategory category, String pointName)
  {
    if (matcherList == null) {
      return true;
    }
    String joined = category.getFqName();
    if (pointName != null) {
      joined = joined + "." + pointName;
    }
    Boolean matches = Boolean.TRUE;
    for (Pair<Boolean, Matcher<String>> p : matcherList) {
      if (p.getSecond().match(joined) == true) {
        matches = p.getFirst();
      }
    }
    return matches;
  }

  /**
   * Adds the performance.
   *
   * @param category the category
   * @param pointName the point name
   * @param millis the millis
   * @param wait the wait
   */
  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.stats.StatsDAO#addPerformance(de.micromata.genome.logging.LogCategory, java.lang.String,
   * long, long)
   */
  @Override
  public synchronized void addPerformance(LogCategory category, String pointName, long millis, long wait)
  {
    if (doAddPerformance(category, pointName) == false) {
      return;
    }
    synchronized (stats) {
      stats.getPerfStats().addPerfElement(category, pointName, millis, wait);
    }
  }

  /**
   * String params like ;jsessionid=asdf from path.
   *
   * @param pp the pp
   * @return the string
   */
  private String stripParamsFromUrl(String pp)
  {
    if (pp == null) {
      return pp;
    }
    int idx = pp.indexOf(';');
    if (idx != -1) {
      pp = pp.substring(0, idx);
    }
    return pp;
  }

  /**
   * Adds the request.
   *
   * @param req the req
   * @param millis the millis
   */
  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.stats.StatsDAO#addRequest(javax.servlet.http.HttpServletRequest, long)
   */
  @Override
  public void addRequest(HttpServletRequest req, long millis)
  {
    String path = req.getServletPath();
    String cp = req.getContextPath();

    String pp = path;

    if (useRequestUrl.containsKey(path) == true) {
      path = req.getRequestURI();
      pp = path.substring(cp.length());
    }

    path = stripParamsFromUrl(path);
    pp = stripParamsFromUrl(pp);

    LogCategory cat = GenomeLogCategory.RequestProcessing;

    if (categoryForRequestDetailRules != null) {
      for (Pair<Matcher<String>, LogCategory> p : categoryForRequestDetailRules) {
        if (p.getKey().match(pp) == true) {
          cat = p.getValue();
          break;
        }
      }
    }
    addPerformance(cat, path, millis, 0);
  }

  /**
   * Liefert eine Kopie der aktuellen LogStats
   * 
   * @return
   */
  public static StatsDO getStats()
  {
    synchronized (stats) {
      return (StatsDO) stats.clone();
    }
  }

  /**
   * liefert die aktuellen LogStats zurueck und setzt die internen zurueck.
   *
   * @return the stats do
   */
  public static StatsDO fetchStats()
  {
    synchronized (stats) { // NOSONAR
      StatsDO oldLogStats = stats;
      stats = new StatsDO();
      return oldLogStats;
    }
  }

  public static void setStats(StatsDO stats)
  {
    StatsDAOImpl.stats = stats;
  }

  // public Map<String, LogCategory> getCategoryForRequestDetail()
  // {
  // return categoryForRequestDetail;
  // }

  public void setCategoryForRequestDetail(Map<String, LogCategory> categoryForRequestDetail)
  {
    categoryForRequestDetailRules = new ArrayList<Pair<Matcher<String>, LogCategory>>();
    MatcherFactory<String> matchFactory = new SimpleWildcardMatcherFactory<String>();
    for (Map.Entry<String, LogCategory> me : categoryForRequestDetail.entrySet()) {
      categoryForRequestDetailRules
          .add(new Pair<Matcher<String>, LogCategory>(matchFactory.createMatcher(me.getKey()), me.getValue()));
    }
    // this.categoryForRequestDetail = categoryForRequestDetail;
  }

  public Map<String, Object> getUseRequestUrl()
  {
    return useRequestUrl;
  }

  public void setUseRequestUrl(Map<String, Object> useRequestUrl)
  {
    this.useRequestUrl = useRequestUrl;
  }

  /**
   * Run long running op.
   *
   * @param <T> the generic type
   * @param <EX> the generic type
   * @param category the category
   * @param pointName the point name
   * @param displayObject the display object
   * @param callback the callback
   * @return the t
   * @throws EX the ex
   */
  @Override
  public <T, EX extends Throwable> T runLongRunningOp(LogCategory category, String pointName, Object displayObject,
      CallableX<T, EX> callback) throws EX
  {
    CurrentOperation curOps = new CurrentOperation(category, pointName, displayObject);
    synchronized (currentOps) {
      currentOps.add(curOps);
    }
    long startTime = System.currentTimeMillis();
    try {
      return callback.call();
    } finally {
      addPerformance(category, pointName, System.currentTimeMillis() - startTime, 0);
      synchronized (currentOps) {
        currentOps.remove(curOps);
      }
    }

  }

  @Override
  public Collection<CurrentOperation> getCurrentOperations()
  {
    List<CurrentOperation> ret = new ArrayList<CurrentOperation>();
    synchronized (currentOps) {
      ret.addAll(currentOps);
    }
    return ret;
  }

  public void setMatcherRule(String rules)
  {
    matcherList = MatchUtil.parseMatcherRuleList(rules, new SimpleWildcardMatcherFactory());
  }

  public List<Pair<Boolean, Matcher<String>>> getMatcherList()
  {
    return matcherList;
  }

  public void setMatcherList(List<Pair<Boolean, Matcher<String>>> matcherList)
  {
    this.matcherList = matcherList;
  }

  public List<Pair<Matcher<String>, LogCategory>> getCategoryForRequestDetailRules()
  {
    return categoryForRequestDetailRules;
  }

  public void setCategoryForRequestDetailRules(List<Pair<Matcher<String>, LogCategory>> categoryForRequestDetailRules)
  {
    this.categoryForRequestDetailRules = categoryForRequestDetailRules;
  }

  /**
   * Adds the logging.
   *
   * @param lwe the lwe
   */
  @Override
  public void addLogging(LogWriteEntry lwe)
  {
    synchronized (stats) {
      stats.getLogStats().setStat(lwe.getCategory(), lwe.getLevel());
    }
  }

}
