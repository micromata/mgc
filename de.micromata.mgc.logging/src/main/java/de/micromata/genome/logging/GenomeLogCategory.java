/*
 Created on 08.01.2008
 */
package de.micromata.genome.logging;

/**
 * LogCategories used inside Genome.
 *
 * @author roger@micromata.de
 */
public enum GenomeLogCategory implements LogCategory
{

  /**
   * The Database.
   */
  Database,

  /**
   * The Database tech admin.
   */
  DatabaseTechAdmin,

  /**
   * The Database domain admin.
   */
  DatabaseDomainAdmin, /**
                        * Used normally for Performance stats.
                        */
  Jpa,

  /**
   * The Configuration.
   */
  Configuration,

  /**
   * The Coding.
   */
  Coding,

  /**
   * The Test mode.
   */
  TestMode,

  /**
   * The Internal.
   */
  Internal,

  /**
   * The Request processing.
   */
  RequestProcessing,

  /**
   * The Attr.
   */
  Attr,

  /**
   * The Beans.
   */
  Beans,

  /**
   * GenomeConsole.
   */
  RequestProcessingGC,

  /**
   * GenomeStats.
   */
  RequestProcessingGS,

  /**
   * The Performance trace.
   */
  PerformanceTrace,

  /**
   * The Sql trace.
   */
  SqlTrace,

  /**
   * The System.
   */
  System,

  /**
   * Communication with other systems.
   */
  Communication,

  /**
   * The Scheduler.
   */
  Scheduler,

  /**
   * The User management.
   */
  UserManagement,

  /**
   * The Reporting.
   */
  Reporting,

  /**
   * The Caches.
   */
  Caches,

  /**
   * The Unknown.
   */
  Unknown,

  /**
   * The Invalid entry parameter.
   */
  InvalidEntryParameter,

  /**
   * The Email.
   */
  Email,

  /**
   * The Performance stat.
   */
  PerformanceStat,

  /**
   * The Stats.
   */
  Stats,

  /**
   * Should only be used from UnitTests.
   */
  UnitTest,

  /**
   * Used to trace GenomeDaoManagerLogs.
   */
  GenomeDao,

  /**
   * InterNodeCallDAO.
   */
  InterNodeCall,

  /**
   * The Flow log.
   */
  FlowLog,

  /**
   * User management.
   */
  Umgmt;

  static {
    BaseLogging.registerLogCategories(values());
  }

  /**
   * The fq name.
   */
  private String fqName;

  /**
   * Instantiates a new genome log category.
   */
  private GenomeLogCategory()
  {
    fqName = "GNM" + "." + name();
  }

  @Override
  public String getFqName()
  {
    return fqName;
  }

  @Override
  public String getPrefix()
  {
    return "GNM";
  }

}
