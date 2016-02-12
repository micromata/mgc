package de.micromata.genome.db.jdbc.trace;

import java.io.InputStream;
import java.io.Reader;

/**
 * The Class SqlTypeWrapper.
 *
 * @author roger
 */
public class SqlTypeWrapper
{

  /**
   * The value.
   */
  private Object value;

  /**
   * The sql type.
   */
  private int sqlType = 0;

  /**
   * The scale.
   */
  private int scale = 0;

  /**
   * The target sql type.
   */
  private int targetSqlType = 0;

  /**
   * The type name.
   */
  private String typeName = null;

  /**
   * The length.
   */
  private int length;

  /**
   * Instantiates a new sql type wrapper.
   */
  public SqlTypeWrapper()
  {

  }

  /**
   * Instantiates a new sql type wrapper.
   *
   * @param obj the obj
   */
  public SqlTypeWrapper(Object obj)
  {
    this.value = obj;
  }

  /**
   * Make.
   *
   * @param obj the obj
   * @return the sql type wrapper
   */
  public static SqlTypeWrapper make(Object obj)
  {
    return new SqlTypeWrapper(obj);
  }

  /**
   * Make null.
   *
   * @param sqlType the sql type
   * @param typeName the type name
   * @return the sql type wrapper
   */
  public static SqlTypeWrapper makeNull(int sqlType, String typeName)
  {
    SqlTypeWrapper ret = new SqlTypeWrapper(null);
    ret.sqlType = sqlType;
    ret.typeName = typeName;
    return ret;
  }

  /**
   * Make ascii.
   *
   * @param x the x
   * @param length the length
   * @return the sql type wrapper
   */
  public static SqlTypeWrapper makeAscii(InputStream x, int length)
  {
    SqlTypeWrapper ret = new SqlTypeWrapper(x);
    ret.length = length;
    return ret;
  }

  /**
   * Make binary.
   *
   * @param x the x
   * @param length the length
   * @return the sql type wrapper
   */
  public static SqlTypeWrapper makeBinary(InputStream x, int length)
  {
    SqlTypeWrapper ret = new SqlTypeWrapper(x);
    ret.length = length;
    return ret;
  }

  /**
   * Make character stream.
   *
   * @param reader the reader
   * @param length the length
   * @return the sql type wrapper
   */
  public static SqlTypeWrapper makeCharacterStream(Reader reader, int length)
  {
    SqlTypeWrapper ret = new SqlTypeWrapper(reader);
    ret.length = length;
    return ret;
  }

  /**
   * Make object.
   *
   * @param x the x
   * @param targetSqlType the target sql type
   * @param scale the scale
   * @return the sql type wrapper
   */
  public static SqlTypeWrapper makeObject(Object x, int targetSqlType, int scale)
  {
    SqlTypeWrapper ret = new SqlTypeWrapper(x);
    ret.targetSqlType = targetSqlType;
    ret.scale = scale;
    return ret;
  }

  public Object getValue()
  {
    return value;
  }

  public void setValue(Object value)
  {
    this.value = value;
  }

  public int getSqlType()
  {
    return sqlType;
  }

  public void setSqlType(int sqlType)
  {
    this.sqlType = sqlType;
  }

  public int getScale()
  {
    return scale;
  }

  public void setScale(int scale)
  {
    this.scale = scale;
  }

  public int getTargetSqlType()
  {
    return targetSqlType;
  }

  public void setTargetSqlType(int targetSqlType)
  {
    this.targetSqlType = targetSqlType;
  }

  public String getTypeName()
  {
    return typeName;
  }

  public void setTypeName(String typeName)
  {
    this.typeName = typeName;
  }

  public int getLength()
  {
    return length;
  }

  public void setLength(int length)
  {
    this.length = length;
  }

}
