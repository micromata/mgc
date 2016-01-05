package de.micromata.mgc.jpa.hibernatesearch.impl;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.SortableFields;

import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.metainf.ColumnMetadata;
import de.micromata.genome.jpa.metainf.EntityMetadata;
import de.micromata.genome.jpa.metainf.JpaMetadataRepostory;
import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.runtime.ClassUtils;
import de.micromata.mgc.jpa.hibernatesearch.api.HibernateSearchFieldInfoProvider;
import de.micromata.mgc.jpa.hibernatesearch.api.HibernateSearchInfo;
import de.micromata.mgc.jpa.hibernatesearch.api.SearchEmgrFactory;

/**
 * Figure out all Search Fields from the entities.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */

public class SearchEmgrFactoryRegistryUtils
{

  private static final Logger LOG = Logger.getLogger(SearchEmgrFactoryRegistryUtils.class);

  /**
   * Dummy method to hold default IndexedEmbedded anot.
   */
  @IndexedEmbedded()
  public static void annotationDummyMethod()
  {

  }

  private static IndexedEmbedded DEFAULT_IndexedEmbedded;

  static {
    Method method = ClassUtils.findMethod(SearchEmgrFactoryRegistryUtils.class, "annotationDummyMethod");
    DEFAULT_IndexedEmbedded = method.getAnnotation(IndexedEmbedded.class);
  }

  public static void initJpaMetadataRepostory(EmgrFactory<?> emf)
  {
    JpaMetadataRepostory repo = emf.getMetadataRepository();
    Map<Class<?>, Map<String, ColumnMetadata>> entitiesWithSearchFields = new HashMap<>();
    for (EntityMetadata entm : repo.getEntities().values()) {
      Map<String, ColumnMetadata> sf = getSearchFields(repo, entm, Integer.MAX_VALUE);
      entitiesWithSearchFields.put(entm.getJavaType(), sf);
    }
    repo.getServiceCustomAttributes().put(SearchEmgrFactory.REPO_ENTITY_SEARCHFIELDS, entitiesWithSearchFields);
  }

  private static Map<String, ColumnMetadata> getSearchFields(JpaMetadataRepostory repo, EntityMetadata entm,
      int maxDepth)
  {
    Map<String, ColumnMetadata> ret = new HashMap<>();

    for (ColumnMetadata cm : entm.getColumns().values()) {

      // TODO RK bei mehrzahl die columns rausfinden.
      // TODO bei @ContainedIn      @IndexedEmbedded(depth = 1) muss entsprechend die Detailentitaet ausgelesen werden.
      {
        Field field = cm.findAnnoation(Field.class);
        if (field != null) {
          String name = field.name();
          if (StringUtils.isBlank(name) == true) {
            name = cm.getName();
          }
          ret.put(name, cm);
        }
      }
      {
        SortableField sfield = cm.findAnnoation(SortableField.class);
        if (sfield != null) {
          String name = sfield.forField();
          if (StringUtils.isBlank(name) == true) {
            name = cm.getName();
          }
          ret.put(name, cm);
        }
      }
      {
        Fields fields = cm.findAnnoation(Fields.class);
        if (fields != null) {
          for (Field field : fields.value()) {
            String name = field.name();
            if (StringUtils.isBlank(name) == true) {
              name = cm.getName();
            }
            ret.put(name, cm);
          }
        }
      }
      {
        SortableFields sfields = cm.findAnnoation(SortableFields.class);
        if (sfields != null) {
          for (SortableField field : sfields.value()) {
            String name = field.forField();
            if (StringUtils.isBlank(name) == true) {
              name = cm.getName();
            }
            ret.put(name, cm);
          }
        }
      }
      {
        ContainedIn ci = cm.findAnnoation(ContainedIn.class);
        if (ci != null) {
          IndexedEmbedded iemb = cm.findAnnoation(IndexedEmbedded.class);
          if (iemb == null) {
            iemb = DEFAULT_IndexedEmbedded;
          }
          addNestedSearchFields(repo, cm, iemb, maxDepth - 1, ret);
        } else {
          IndexedEmbedded iemb = cm.findAnnoation(IndexedEmbedded.class);
          if (iemb != null) {
            addNestedSearchFields(repo, cm, iemb, maxDepth - 1, ret);
          }
        }
      }
    }
    addCustomSearchFields(ret, entm);
    return ret;
  }

  private static void addCustomSearchFields(Map<String, ColumnMetadata> ret, EntityMetadata entm)
  {
    List<HibernateSearchInfo> cil = ClassUtils.findClassAnnotations(entm.getJavaType(), HibernateSearchInfo.class);
    for (HibernateSearchInfo ci : cil) {
      HibernateSearchFieldInfoProvider fip = PrivateBeanUtils.createInstance(ci.fieldInfoProvider());
      Map<String, ColumnMetadata> sm = fip.getAdditionallySearchFields(entm, ci.param());
      ret.putAll(sm);

    }
  }

  private static void addNestedSearchFields(JpaMetadataRepostory repo, ColumnMetadata masterColumn,
      IndexedEmbedded iemb, int maxDepth,
      Map<String, ColumnMetadata> ret)
  {
    if (maxDepth < 1) {
      return;
    }
    String[] paths = iemb.includePaths();
    if (paths.length > 0) {
      for (String path : paths) {
        ret.put(masterColumn.getName() + "." + path, masterColumn);
      }
      return;
    }
    EntityMetadata assicatedEntity = getAssocatedJavaType(masterColumn);
    if (assicatedEntity == null) {
      LOG.error("Search; Cannot find EntityMetadata for " + masterColumn.getJavaType().getName());
      return;
    }
    int maxLevel = Math.min(iemb.depth(), maxDepth);
    Map<String, ColumnMetadata> nested = getSearchFields(repo, assicatedEntity, maxLevel);
    for (Map.Entry<String, ColumnMetadata> me : nested.entrySet()) {
      ret.put(masterColumn.getName() + "." + me.getKey(), masterColumn);
    }
  }

  private static EntityMetadata getAssocatedJavaType(ColumnMetadata masterColumn)
  {
    EntityMetadata targetEnt = masterColumn.getTargetEntity();
    Class<?> javaType = masterColumn.getJavaType();
    if (Collection.class.isAssignableFrom(javaType) == true || Map.class.isAssignableFrom(javaType) == true) {
      if (targetEnt == null) {
        LOG.error("Have to define attribute targetEntity in anntotation to support search: "
            + masterColumn.getShortDeclaration());
      }
    }
    return targetEnt;
  }

}
