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
 * Wrapper to a given LogAttributeType.
 *
 * @author roger
 */
public class LogAttributeTypeWrapper implements LogAttributeType
{

  /**
   * The orignal attribute type class name.
   */
  private String orignalAttributeTypeClassName;

  /**
   * The column name.
   */
  private String columnName;

  /**
   * The is search key.
   */
  private boolean isSearchKey;

  /**
   * The max value size.
   */
  private int maxValueSize;

  /**
   * The name.
   */
  private String name;

  /**
   * The renderer.
   */
  private LogAttributeRenderer renderer;

  /**
   * Instantiates a new log attribute type wrapper.
   *
   * @param name the name
   */
  public LogAttributeTypeWrapper(String name)
  {
    this.renderer = new DefaultLogAttributeRenderer();
    this.name = name;
  }

  public LogAttributeTypeWrapper(LogAttributeType attributeType)
  {
    this(attributeType, false);
  }

  /**
   * Instantiates a new log attribute type wrapper.
   *
   * @param attributeType the attribute type
   */
  public LogAttributeTypeWrapper(LogAttributeType attributeType, boolean dumpDataOnly)
  {
    if (attributeType instanceof LogAttributeTypeWrapper) {
      orignalAttributeTypeClassName = ((LogAttributeTypeWrapper) attributeType).getOrignalAttributeTypeClassName();
    } else {
      orignalAttributeTypeClassName = attributeType.getClass().getCanonicalName();
    }
    this.columnName = attributeType.columnName();
    this.isSearchKey = attributeType.isSearchKey();
    this.maxValueSize = attributeType.maxValueSize();
    this.name = attributeType.name();
    if (dumpDataOnly == false) {
      this.renderer = attributeType.getRenderer();
    }
  }

  @Override
  public String columnName()
  {
    return columnName;
  }

  @Override
  public AttributeTypeDefaultFiller getAttributeDefaultFiller()
  {
    return null;
  }

  @Override
  public boolean isSearchKey()
  {
    return isSearchKey;
  }

  @Override
  public int maxValueSize()
  {
    return maxValueSize;
  }

  @Override
  public String name()
  {
    return name;
  }

  public String getColumnName()
  {
    return columnName;
  }

  public void setColumnName(String columnName)
  {
    this.columnName = columnName;
  }

  public int getMaxValueSize()
  {
    return maxValueSize;
  }

  public void setMaxValueSize(int maxValueSize)
  {
    this.maxValueSize = maxValueSize;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setSearchKey(boolean isSearchKey)
  {
    this.isSearchKey = isSearchKey;
  }

  @Override
  public LogAttributeRenderer getRenderer()
  {
    return this.renderer;
  }

  public void setRenderer(LogAttributeRenderer renderer)
  {
    this.renderer = renderer;
  }

  public String getOrignalAttributeTypeClassName()
  {
    return orignalAttributeTypeClassName;
  }

  public void setOrignalAttributeTypeClassName(String orignalAttributeTypeClassName)
  {
    this.orignalAttributeTypeClassName = orignalAttributeTypeClassName;
  }

}
