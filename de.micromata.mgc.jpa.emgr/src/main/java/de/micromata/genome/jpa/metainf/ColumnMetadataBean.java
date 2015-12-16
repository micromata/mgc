package de.micromata.genome.jpa.metainf;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import de.micromata.genome.util.bean.AttrGetter;
import de.micromata.genome.util.bean.AttrSetter;

/**
 * The Class ColumnMetadataBean.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class ColumnMetadataBean extends EmgrDbElementBean implements ColumnMetadata
{

  /**
   * The name.
   */
  private String name;

  /**
   * The max length.
   */
  private int maxLength;

  /**
   * The association.
   */
  private boolean association;

  /**
   * The collection.
   */
  private boolean collection;

  /**
   * The unique.
   */
  private boolean unique;

  /**
   * The nullable.
   */
  private boolean nullable;

  /**
   * The insertable.
   */
  private boolean insertable;

  /**
   * The updatable.
   */
  private boolean updatable;

  /**
   * The column definition.
   */
  private String columnDefinition;

  /**
   * The precision.
   */
  private int precision;

  /**
   * The scale.
   */
  private int scale;

  private AttrGetter<Object, Object> getter;

  private AttrSetter<Object, Object> setter;
  /**
   * The annotations.
   */
  private List<Annotation> annotations = new ArrayList<>();

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int getMaxLength()
  {
    return maxLength;
  }

  /**
   * Sets the max length.
   *
   * @param maxLength the new max length
   */
  public void setMaxLength(int maxLength)
  {
    this.maxLength = maxLength;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public String getName()
  {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name the new name
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean isUnique()
  {
    return unique;
  }

  /**
   * Sets the unique.
   *
   * @param unique the new unique
   */
  public void setUnique(boolean unique)
  {
    this.unique = unique;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean isNullable()
  {
    return nullable;
  }

  /**
   * Sets the nullable.
   *
   * @param nullable the new nullable
   */
  public void setNullable(boolean nullable)
  {
    this.nullable = nullable;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean isInsertable()
  {
    return insertable;
  }

  /**
   * Sets the insertable.
   *
   * @param insertable the new insertable
   */
  public void setInsertable(boolean insertable)
  {
    this.insertable = insertable;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean isUpdatable()
  {
    return updatable;
  }

  /**
   * Sets the updatable.
   *
   * @param updatable the new updatable
   */
  public void setUpdatable(boolean updatable)
  {
    this.updatable = updatable;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public String getColumnDefinition()
  {
    return columnDefinition;
  }

  /**
   * Sets the column definition.
   *
   * @param columnDefinition the new column definition
   */
  public void setColumnDefinition(String columnDefinition)
  {
    this.columnDefinition = columnDefinition;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int getPrecision()
  {
    return precision;
  }

  /**
   * Sets the precision.
   *
   * @param precision the new precision
   */
  public void setPrecision(int precision)
  {
    this.precision = precision;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int getScale()
  {
    return scale;
  }

  /**
   * Sets the scale.
   *
   * @param scale the new scale
   */
  public void setScale(int scale)
  {
    this.scale = scale;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean isAssociation()
  {
    return association;
  }

  /**
   * Sets the association.
   *
   * @param association the new association
   */
  public void setAssociation(boolean association)
  {
    this.association = association;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean isCollection()
  {
    return collection;
  }

  /**
   * Sets the collection.
   *
   * @param collection the new collection
   */
  public void setCollection(boolean collection)
  {
    this.collection = collection;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public List<Annotation> getAnnotations()
  {
    return annotations;
  }

  /**
   * Sets the annotations.
   *
   * @param annotations the new annotations
   */
  public void setAnnotations(List<Annotation> annotations)
  {
    this.annotations = annotations;
  }

  @Override
  public AttrGetter<Object, Object> getGetter()
  {
    return getter;
  }

  public void setGetter(AttrGetter<Object, Object> getter)
  {
    this.getter = getter;
  }

  @Override
  public AttrSetter<Object, Object> getSetter()
  {
    return setter;
  }

  public void setSetter(AttrSetter<Object, Object> setter)
  {
    this.setter = setter;
  }

}
