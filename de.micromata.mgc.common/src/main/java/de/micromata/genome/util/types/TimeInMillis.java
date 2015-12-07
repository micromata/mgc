/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   04.04.2008
// Copyright Micromata 04.04.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.types;

/**
 * Just a few constants for Time in milli seconds.
 *
 * @author roger@micromata.de
 */
public interface TimeInMillis
{

  /**
   * The Constant SECOND.
   */
  public static final long SECOND = 1000;

  /**
   * The Constant MINUTE.
   */
  public static final long MINUTE = SECOND * 60;

  /**
   * The Constant HOUR.
   */
  public static final long HOUR = MINUTE * 60;

  /**
   * The Constant DAY.
   */
  public static final long DAY = HOUR * 24;

  /**
   * The Constant WEEK.
   */
  public static final long WEEK = DAY * 7;

  /**
   * The Constant MAX_MONTH.
   */
  public static final long MAX_MONTH = DAY * 31;

  /**
   * The Constant YEAR.
   */
  public static final long YEAR = DAY * 365;
}
