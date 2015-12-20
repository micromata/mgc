package de.micromata.genome.jpa.metainf;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.PluralAttribute.CollectionType;
import javax.persistence.metamodel.Type;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.runtime.ClassUtils;

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
    resolve(nrepo);
    return nrepo;
  }

  /**
   * Resolve.
   *
   * @param nrepo the nrepo
   */
  private static void resolve(JpaMetadataRepostory nrepo)
  {
    for (EntityMetadata ent : nrepo.getEntities().values()) {
      resolve(nrepo, ent);
    }
  }

  /**
   * Resolve.
   *
   * @param nrepo the nrepo
   * @param ent the ent
   */
  private static void resolve(JpaMetadataRepostory nrepo, EntityMetadata ent)
  {
    for (ColumnMetadata cmd : ent.getColumns().values()) {
      resolve(nrepo, (ColumnMetadataBean) cmd);
    }
  }

  /**
   * Resolve.
   *
   * @param nrepo the nrepo
   * @param cmd the cmd
   */
  private static void resolve(JpaMetadataRepostory nrepo, ColumnMetadataBean cmd)
  {
    resolveTargetEntity(nrepo, cmd);
  }

  /**
   * Find set target entity.
   *
   * @param nrepo the nrepo
   * @param clazz the clazz
   * @param cmd the cmd
   * @return true, if successful
   */
  private static boolean findSetTargetEntity(JpaMetadataRepostory nrepo, Class<?> clazz, ColumnMetadataBean cmd)
  {
    if (void.class == clazz) {
      return false;
    }
    EntityMetadata entmeta = nrepo.findEntityMetadata(clazz);
    if (entmeta == null) {
      LOG.warn("Cannot find targetEntity: " + clazz);
      return false;
    }
    cmd.setTargetEntity(entmeta);
    return true;
  }

  /**
   * Resolve target entity.
   *
   * @param nrepo the nrepo
   * @param cmd the cmd
   */
  private static void resolveTargetEntity(JpaMetadataRepostory nrepo, ColumnMetadataBean cmd)
  {
    {
      OneToOne anot = cmd.findAnnoation(OneToOne.class);
      if (anot != null) {
        if (findSetTargetEntity(nrepo, anot.targetEntity(), cmd) == true) {
          return;
        }
      }
    }
    {
      OneToMany anot = cmd.findAnnoation(OneToMany.class);
      if (anot != null) {
        if (findSetTargetEntity(nrepo, anot.targetEntity(), cmd) == true) {
          return;
        }
      }
    }
    {
      ManyToOne anot = cmd.findAnnoation(ManyToOne.class);
      if (anot != null) {
        if (findSetTargetEntity(nrepo, anot.targetEntity(), cmd) == true) {
          return;
        }
      }
    }
    EntityMetadata entmeta = nrepo.findEntityMetadata(cmd.getJavaType());
    if (entmeta != null) {
      cmd.setTargetEntity(entmeta);
      return;
    }
    if (Collection.class.isAssignableFrom(cmd.getJavaType()) == true) {
      Class<?> entclazz = cmd.getEntity().getJavaType();
      Class<?> genClazz = ClassUtils.findGenericTypeFromProperty(entclazz, cmd.getName(), 0);
      if (genClazz != null) {
        entmeta = nrepo.findEntityMetadata(genClazz);
        if (entmeta != null) {
          cmd.setTargetEntity(entmeta);
          return;
        }
      }
    }
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
    // TODO RK delete propDesc
    List<PropertyDescriptor> propDescs = Arrays.asList(PropertyUtils.getPropertyDescriptors(ret.getJavaType()));
    Set<?> attr = mt.getAttributes();
    for (Object oa : attr) {

      Attribute<?, ?> at = (Attribute<?, ?>) oa;
      Optional<PropertyDescriptor> pdo = propDescs.stream().filter((el) -> el.getName().equals(at.getName()))
          .findFirst();
      ColumnMetadata colm = getColumnMetaData(ret, mt.getJavaType(), at, pdo);
      if (colm != null) {
        ret.getColumns().put(colm.getName(), colm);
      }
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
   * @param entity the entity
   * @param entityClass the entity class
   * @param at the at
   * @param pdo the pdo
   * @return the column meta data
   */
  public static ColumnMetadata getColumnMetaData(EntityMetadataBean entity, Class<?> entityClass, Attribute<?, ?> at,
      Optional<PropertyDescriptor> pdo)
  {
    ColumnMetadataBean ret = new ColumnMetadataBean(entity);
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
    // TODO maybe handle this.
    at.getPersistentAttributeType();
    if ((jm instanceof AccessibleObject) == false) {
      LOG.warn("Column " + at.getName() + " ha no valid Java Member");
      return ret;
    }
    AccessibleObject ao = (AccessibleObject) jm;
    getGetterSetter(entityClass, ao, pdo, ret);
    ret.setAnnotations(getFieldAndMemberAnnots(entityClass, ao));
    if (ret.getAnnotations().stream().filter((anot) -> anot.getClass() == Transient.class).count() > 0) {
      return null;
    }
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
   * Gets the getter setter.
   *
   * @param entityClass the entity class
   * @param accessableObject the accessable object
   * @param pdo the pdo
   * @param ret the ret
   * @return the getter setter
   */
  private static void getGetterSetter(Class<?> entityClass, AccessibleObject accessableObject,
      Optional<PropertyDescriptor> pdo, ColumnMetadataBean ret)
  {
    if (accessableObject instanceof Method) {
      ret.setGetter(PrivateBeanUtils.getMethodAttrGetter((Class) entityClass, (Method) accessableObject, Object.class));
    }
    if (ret.getGetter() == null) {
      Method method = PrivateBeanUtils.findGetterFromField(entityClass, ret.getName(), ret.getJavaType());
      if (method != null) {
        ret.setGetter(PrivateBeanUtils.getMethodAttrGetter((Class) entityClass, method, Object.class));
      }
    }
    if (ret.getSetter() == null) {
      Method method = PrivateBeanUtils.findSetterFromField(entityClass, ret.getName(), ret.getJavaType());
      if (method != null) {
        ret.setSetter(PrivateBeanUtils.getMethodAttrSetter((Class) entityClass, method, Object.class));
      }
    }
    if (ret.getSetter() != null && ret.getGetter() != null) {
      return;
    }
    Field field = PrivateBeanUtils.findField(entityClass, ret.getName());
    if (field == null) {
      if (ret.getGetter() != null) {
        // no warn on collections.
        LOG.warn(
            "Cannot determine all getter method or field for " + entityClass.getName() + "."
                + ret.getName());
      }
      return;
    }
    if (ret.getGetter() == null) {
      ret.setGetter(PrivateBeanUtils.getFieldAttrGetter((Class) entityClass, field, Object.class));
    }
    if (ret.getSetter() == null) {
      ret.setSetter(PrivateBeanUtils.getFieldAttrSetter((Class) entityClass, field, Object.class));
    }

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
