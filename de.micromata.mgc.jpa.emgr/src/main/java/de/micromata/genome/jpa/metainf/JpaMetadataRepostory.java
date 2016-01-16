package de.micromata.genome.jpa.metainf;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Contains meta data to entities.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JpaMetadataRepostory
{

  /**
   * The Constant DEFAULT_COLLENGTH.
   */
  private static final int DEFAULT_COLLENGTH = 255;

  /**
   * The entities.
   */
  private Map<Class<?>, EntityMetadata> entities = new HashMap<>();
  /**
   * Class is a service implementation, wich can store it's own data. The Attributes should be stored at intialization
   * time, so that access to the map is not forced to be synchronized.
   */
  private Map<Class<?>, Object> serviceCustomAttributes = new HashMap<>();
  /**
   * Entities, representing tables. Sorted by dependency
   */
  private List<EntityMetadata> tableEntities = new ArrayList<>();

  public Map<Class<?>, Object> getServiceCustomAttributes()
  {
    return serviceCustomAttributes;
  }

  /**
   * Gets the entities.
   *
   * @return the entities
   */
  public Map<Class<?>, EntityMetadata> getEntities()
  {
    return entities;
  }

  /**
   * Get a list of all table entities.
   * 
   * The Tables are sorted by dependencies. Tables with less deps are first.
   *
   * @return the table entities
   */
  public List<EntityMetadata> getTableEntities()
  {

    return tableEntities;
  }

  /**
   * Gets the column data.
   *
   * @param <T> the generic type
   * @param entity the entity
   * @param property the property
   * @param defaultValue the default value
   * @param func the func
   * @return the column data
   */
  private <T> T getColumnData(Class<?> entity, String property, T defaultValue, Function<ColumnMetadata, T> func)
  {
    EntityMetadata em = entities.get(entity);
    if (em == null) {
      return defaultValue;
    }
    ColumnMetadata pe = em.getColumns().get(property);
    if (pe == null) {
      return defaultValue;
    }
    return func.apply(pe);
  }

  /**
   * Find entity metadata.
   *
   * @param clazz the clazz
   * @return the entity metadata
   */
  public EntityMetadata findEntityMetadata(Class<?> clazz)
  {
    return entities.get(clazz);
  }

  /**
   * Gets the entity meta data.
   *
   * @param clazz the clazz
   * @return the entity meta data
   * @throws JpaMetadataEntityNotFoundException the jpa metadata entity not found exception
   */
  public EntityMetadata getEntityMetadata(Class<?> clazz) throws JpaMetadataEntityNotFoundException
  {
    EntityMetadata fm = findEntityMetadata(clazz);
    if (fm != null) {
      return fm;
    }
    throw new JpaMetadataEntityNotFoundException("No Metadata found for class: " + clazz.getName());
  }

  /**
   * Find column metadata.
   *
   * @param entity the entity
   * @param property the property
   * @return the column metadata
   */
  public ColumnMetadata findColumnMetadata(Class<?> entity, String property)
  {
    EntityMetadata em = entities.get(entity);
    if (em == null) {
      return null;
    }
    ColumnMetadata pe = em.getColumns().get(property);
    return pe;
  }

  /**
   * Find entity metadata.
   *
   * @param entityClassName the entity class name
   * @return the entity metadata
   */
  public EntityMetadata findEntityMetadata(String entityClassName)
  {
    for (Map.Entry<Class<?>, EntityMetadata> me : entities.entrySet()) {
      if (me.getKey().getName().equals(entityClassName) == true
          || me.getKey().getSimpleName().equals(entityClassName)) {
        return me.getValue();
      }
    }
    return null;
  }

  /**
   * Checks if is known entity.
   *
   * @param clazz the clazz
   * @return true, if is known entity
   */
  public boolean isKnownEntity(Class<?> clazz)
  {
    return entities.containsKey(clazz);
  }

  /**
   * Gets the max length.
   *
   * @param entity the entity
   * @param property the property
   * @return the max length
   */
  public int getMaxLength(Class<?> entity, String property)
  {
    return getColumnData(entity, property, DEFAULT_COLLENGTH, (cmd) -> cmd.getMaxLength());
  }

  /**
   * Gets the column annotations.
   *
   * @param entity the entity
   * @param property the property
   * @return the column annotations
   */
  public List<Annotation> getColumnAnnotations(Class<?> entity, String property)
  {
    return (List) getColumnData(entity, property, Collections.emptyList(), (cmd) -> cmd.getAnnotations());
  }

  /**
   * Find column annotation.
   *
   * @param <T> the generic type
   * @param entity the entity
   * @param property the property
   * @param annotationClazz the annotation clazz
   * @return the t
   */
  public <T extends Annotation> T findColumnAnnotation(Class<?> entity, String property, Class<T> annotationClazz)
  {
    for (Annotation anot : getColumnAnnotations(entity, property)) {
      if (annotationClazz.isAssignableFrom(anot.getClass()) == true) {
        return (T) anot;
      }
    }
    return null;
  }

  /**
   * Gets the entity meta data by simple class name.
   *
   * @param classname the classname
   * @return the entity meta data by simple class name
   * @throws JpaMetadataEntityNotFoundException the jpa metadata entity not found exception in case of not found and
   *           ambiguent multiple found.
   */
  public EntityMetadata getEntityMetaDataBySimpleClassName(String classname) throws JpaMetadataEntityNotFoundException
  {
    EntityMetadata found = null;
    for (EntityMetadata em : entities.values()) {
      if (em.getJavaType().getSimpleName().equals(classname) == true) {
        if (found != null) {
          throw new JpaMetadataEntityNotFoundException("Multiple matching simple classname found: "
              + em.getJavaType().getName() + ", " + found.getJavaType().getName());
        }
        found = em;
      }
    }
    if (found == null) {
      throw new JpaMetadataEntityNotFoundException("No entity Found by simple classname: " + classname);
    }
    return found;
  }
}
