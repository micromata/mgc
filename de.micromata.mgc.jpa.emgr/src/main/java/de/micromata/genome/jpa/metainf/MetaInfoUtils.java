//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
    Set<ManagedType<?>> mtl = metamodel.getManagedTypes();
    for (ManagedType<?> mt : mtl) {
      EntityMetadata empt = toEntityMetaData(mt);
      nrepo.getEntities().put(empt.getJavaType(), empt);
    }
    resolve(nrepo);
    buildReferences(nrepo);
    List<EntityMetadata> entities = buildSortedEnties(nrepo);
    nrepo.getTableEntities().addAll(entities);

    if (LOG.isDebugEnabled() == true) {
      LOG.debug("Sorted entities: "
          + entities.stream().map((e) -> e.getJavaType().getSimpleName()).collect(Collectors.toList()));
    }
    return nrepo;
  }

  private static List<EntityMetadata> buildSortedEnties(JpaMetadataRepostory nrepo)
  {
    List<EntityMetadata> unsortedTables = new ArrayList<>(nrepo.getEntities().size());
    for (EntityMetadata em : nrepo.getEntities().values()) {
      if (em.isTableEntity() == true) {
        unsortedTables.add(em);
      }
    }
    Set<EntityMetadata> remainsings = new HashSet<>(unsortedTables);
    List<EntityMetadata> sortedTables = new ArrayList<>();
    for (EntityMetadata table : unsortedTables) {
      addDepsFirst(table, remainsings, sortedTables);
    }
    return sortedTables;
  }

  private static void addDepsFirst(EntityMetadata table, Set<EntityMetadata> remainsings,
      List<EntityMetadata> sortedTable)
  {
    if (remainsings.contains(table) == false) {
      return;
    }
    remainsings.remove(table);
    for (EntityMetadata nt : table.getReferencedBy()) {
      addDepsFirst(nt, remainsings, sortedTable);
    }
    sortedTable.add(table);
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

  private static void buildReferences(JpaMetadataRepostory nrepo)
  {
    for (EntityMetadata ent : nrepo.getEntities().values()) {
      EntityDependencies edeps = ent.getJavaType().getAnnotation(EntityDependencies.class);
      if (edeps != null) {
        for (Class<?> ref : edeps.references()) {
          EntityMetadata other = nrepo.getEntityMetadata(ref);
          ent.getReferencesTo().add(other);
          other.getReferencedBy().add(ent);
        }
        for (Class<?> ref : edeps.referencedBy()) {
          EntityMetadata other = nrepo.getEntityMetadata(ref);
          ent.getReferencedBy().add(other);
          other.getReferencesTo().add(ent);
        }
      }
      for (ColumnMetadata col : ent.getColumns().values()) {
        EntityMetadata te = col.getTargetEntity();
        if (te == null) {
          continue;
        }
        if (col.isCollection() == true) {
          ManyToMany mm = col.findAnnoation(ManyToMany.class);
          if (mm != null) {
            // has to be declared by EntityDependencies
            continue;
          }
          ent.getReferencedBy().add(te);
          te.getReferencesTo().add(ent);
          continue;
        }
        ManyToOne mo = col.findAnnoation(ManyToOne.class);
        OneToMany om = col.findAnnoation(OneToMany.class);
        ManyToMany mm = col.findAnnoation(ManyToMany.class);

        if (mo != null) {
          te.getReferencedBy().add(ent);
          ent.getReferencesTo().add(te);
        } else {
          ent.getReferencedBy().add(te);
          te.getReferencesTo().add(ent);
        }
      }
    }
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
    /// EntityType.getName() is not correct.
    Table[] tabs = mt.getJavaType().getAnnotationsByType(Table.class);
    if (tabs != null && tabs.length > 0) {
      for (Table tab : tabs) {
        if (StringUtils.isNotBlank(tab.name()) == true) {
          ret.setDatabaseName(tab.name());
          break;
        }
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

  public static String dumpMetaTableReferenceByTree(List<EntityMetadata> deps, boolean fullTree)
  {
    Set<EntityMetadata> printed = new HashSet<>();
    StringBuilder sb = new StringBuilder();
    for (EntityMetadata em : deps) {
      sb.append(em);
      sb.append("\n");
      for (EntityMetadata re : em.getReferencedBy()) {

        if (fullTree == false) {
          if (printed.contains(re) == true) {
            continue;
          }
          printed.add(re);
        }
        String ident = "  ";
        HashSet<EntityMetadata> used = new HashSet<>();

        dumpReferencePath(sb, re, fullTree ? used : printed, ident);
      }
    }
    return sb.toString();
  }

  private static void dumpReferencePath(StringBuilder sb, EntityMetadata em, Set<EntityMetadata> visited, String ident)
  {
    sb.append(ident).append(em);
    if (visited.contains(em) == true) {
      sb.append(" < CYCLIC ").append("\n");
      return;
    }
    visited.add(em);
    sb.append("\n");

    for (EntityMetadata re : em.getReferencedBy()) {
      dumpReferencePath(sb, re, visited, ident + "  ");

    }

  }
}
