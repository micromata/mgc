package de.micromata.genome.jpa.metainf;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.PluralAttribute.CollectionType;
import javax.persistence.metamodel.Type;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.util.bean.PrivateBeanUtils;

/**
 * Utils to build the JpaMetadataRepostory.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class MetaInfoUtils
{

  /**
   * The Constant LOG.
   */
  private static final Logger LOG = Logger.getLogger(MetaInfoUtils.class);

  /**
   * Fill entity metadata.
   *
   * @param emgrFac the emgr fac
   * @return the jpa metadata repostory
   */
  public static JpaMetadataRepostory fillEntityMetadata(EmgrFactory<?> emgrFac)
  {
    JpaMetadataRepostory nrepo = new JpaMetadataRepostory();
    Metamodel metamodel = emgrFac.getEntityManagerFactory().getMetamodel();
    //    Set<EntityType<?>> entities = metamodel.getEntities();
    Set<ManagedType<?>> mtl = metamodel.getManagedTypes();
    for (ManagedType<?> mt : mtl) {
      EntityMetadata empt = toEntityMetaData(mt);
      nrepo.getEntities().put(empt.getJavaType(), empt);
    }
    return nrepo;
  }

  /**
   * To entity meta data.
   *
   * @param mt the mt
   * @return the entity metadata
   */
  private static EntityMetadata toEntityMetaData(ManagedType<?> mt)
  {
    EntityMetadataBean ret = new EntityMetadataBean();
    ret.setJavaType(mt.getJavaType());
    if (mt instanceof EntityType) {
      EntityType ift = (EntityType) mt;
      ret.setDatabaseName(ift.getName());
    }
    Set<?> attr = mt.getAttributes();
    for (Object oa : attr) {
      Attribute<?, ?> at = (Attribute<?, ?>) oa;
      ColumnMetadata colm = getColumnMetaData(mt.getJavaType(), at);
      ret.getColumns().put(colm.getName(), colm);
    }

    return ret;
  }

  /**
   * Gets the column meta data.
   *
   * @param emgrFac the emgr fac
   * @param entity the entity
   * @return the column meta data
   */
  public static EntityMetadata getColumnMetaData(EmgrFactory<?> emgrFac, Class<?> entity)
  {
    EntityMetadataBean ret = new EntityMetadataBean();

    Metamodel metamodel = emgrFac.getEntityManagerFactory().getMetamodel();
    ManagedType<?> mt = metamodel.managedType(entity);
    return toEntityMetaData(mt);
  }

  /**
   * Gets the column meta data.
   *
   * @param entityClass the entity class
   * @param at the at
   * @return the column meta data
   */
  public static ColumnMetadata getColumnMetaData(Class<?> entityClass, Attribute<?, ?> at)
  {
    ColumnMetadataBean ret = new ColumnMetadataBean();
    ret.setName(at.getName());
    ret.setJavaType(at.getJavaType());
    ret.setAssociation(at.isAssociation());
    ret.setCollection(at.isCollection());

    if (at instanceof PluralAttribute) {
      PluralAttribute pa = (PluralAttribute) at;
      Type eltype = pa.getElementType();
      CollectionType coltype = pa.getCollectionType();
    }

    Member jm = at.getJavaMember();

    if ((jm instanceof AccessibleObject) == false) {
      LOG.warn("Column " + at.getName() + " ha no valid Java Member");
      return ret;
    }
    AccessibleObject ao = (AccessibleObject) jm;
    ret.setAnnotations(getFieldAndMemberAnnots(entityClass, ao));

    Column colc = ao.getAnnotation(Column.class);
    if (colc == null) {
      ret.setMaxLength(255);
      return ret;
    }
    String name = colc.name();
    if (StringUtils.isEmpty(name) == true) {
      ret.setDatabaseName(ret.getName());
    } else {
      ret.setDatabaseName(name);
    }
    ret.setMaxLength(colc.length());
    ret.setUnique(colc.unique());
    ret.setColumnDefinition(colc.columnDefinition());
    ret.setInsertable(colc.insertable());
    ret.setNullable(colc.nullable());
    ret.setPrecision(colc.precision());
    ret.setScale(colc.scale());
    return ret;
  }

  /**
   * Gets the field and member annots.
   *
   * @param entityClass the entity class
   * @param ao the ao
   * @return the field and member annots
   */
  private static List<Annotation> getFieldAndMemberAnnots(Class<?> entityClass, AccessibleObject ao)
  {
    List<Annotation> ret = new ArrayList<>();
    fillAnnots(ao, ret);
    if (ao instanceof Field) {
      Field f = (Field) ao;
      Method m = PrivateBeanUtils.findGetterFromField(entityClass, f);
      if (m != null) {
        fillAnnots(m, ret);
      }
    } else if (ao instanceof Method) {
      Method method = (Method) ao;
      Field field = PrivateBeanUtils.findFieldFromGetter(entityClass, method);
      if (field != null) {
        fillAnnots(field, ret);
      }
    }
    return ret;
  }

  /**
   * Fill annots.
   *
   * @param ao the ao
   * @param anots the anots
   */
  private static void fillAnnots(AccessibleObject ao, List<Annotation> anots)
  {
    for (Annotation anot : ao.getAnnotations()) {
      anots.add(anot);
    }
  }
}
