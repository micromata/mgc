/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   26.01.2008
// Copyright Micromata 26.01.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

/**
 * Wrapps a Log Category.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LogCategoryWrapper implements LogCategory
{

  /**
   * The name.
   */
  private String name;

  /**
   * The prefix.
   */
  private String prefix;

  /**
   * The fq name.
   */
  private String fqName;

  /**
   * Instantiates a new log category wrapper.
   */
  public LogCategoryWrapper()
  {

  }

  /**
   * Instantiates a new log category wrapper.
   *
   * @param prefix the prefix
   * @param name the name
   */
  public LogCategoryWrapper(String prefix, String name)
  {
    this.name = name;
    this.prefix = prefix;
    this.fqName = prefix + "." + name;
  }

  /**
   * Instantiates a new log category wrapper.
   *
   * @param other the other
   */
  public LogCategoryWrapper(LogCategory other)
  {
    this.name = other.name();
    this.fqName = other.getFqName();
    this.prefix = other.getPrefix();
  }

  @Override
  public String name()
  {
    return name;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @Override
  public String getFqName()
  {
    return fqName;
  }

  public void setFqName(String fqName)
  {
    this.fqName = fqName;
  }

  @Override
  public String getPrefix()
  {
    return prefix;
  }

  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }
}
