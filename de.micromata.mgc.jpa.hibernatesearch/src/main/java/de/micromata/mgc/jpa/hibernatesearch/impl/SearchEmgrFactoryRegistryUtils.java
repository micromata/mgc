package de.micromata.mgc.jpa.hibernatesearch.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.metadata.FieldDescriptor;
import org.hibernate.search.metadata.IndexedTypeDescriptor;
import org.hibernate.search.metadata.PropertyDescriptor;

import de.micromata.genome.jpa.metainf.ColumnMetadata;
import de.micromata.genome.jpa.metainf.ColumnMetadataBean;
import de.micromata.genome.jpa.metainf.EntityMetadata;
import de.micromata.genome.jpa.metainf.JpaMetadataRepostory;
import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.runtime.ClassUtils;
import de.micromata.mgc.jpa.hibernatesearch.api.HibernateSearchFieldInfoProvider;
import de.micromata.mgc.jpa.hibernatesearch.api.HibernateSearchInfo;
import de.micromata.mgc.jpa.hibernatesearch.api.ISearchEmgr;
import de.micromata.mgc.jpa.hibernatesearch.api.SearchColumnMetadata;
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

  public static void initJpaMetadataRepostory(SearchEmgrFactory<?> emf)
  {
    emf.runWoTrans((emgr) -> {

      JpaMetadataRepostory repo = emf.getMetadataRepository();
      Map<Class<?>, Map<String, SearchColumnMetadata>> entitiesWithSearchFields = new HashMap<>();
      for (EntityMetadata entm : repo.getEntities().values()) {
        Map<String, SearchColumnMetadata> sf = getSearchFields(emgr, repo, entm, Integer.MAX_VALUE);
        entitiesWithSearchFields.put(entm.getJavaType(), sf);
      }
      repo.getServiceCustomAttributes().put(SearchEmgrFactory.REPO_ENTITY_SEARCHFIELDS, entitiesWithSearchFields);
      return null;
    });
  }

  private static Map<String, SearchColumnMetadata> getSearchFields(ISearchEmgr<?> emgr, JpaMetadataRepostory repo,
      EntityMetadata entm,
      int maxDepth)
  {
    //    if (entm.getJavaType().getName().indexOf("PfHistoryMasterDO") != -1) {
    //      System.out.println("historymaster");
    //    }
    Map<String, SearchColumnMetadata> ret = new HashMap<>();
    addHibernateSearchOwnFields(emgr, ret, entm);
    addCustomSearchFields(emgr, entm, ret);
    //    for (ColumnMetadata cm : entm.getColumns().values()) {
    //
    //      {
    //        Field field = cm.findAnnoation(Field.class);
    //        if (field != null) {
    //          String name = field.name();
    //          if (StringUtils.isBlank(name) == true) {
    //            name = cm.getName();
    //          }
    //          ret.put(name, cm);
    //        }
    //      }
    //      {
    //        SortableField sfield = cm.findAnnoation(SortableField.class);
    //        if (sfield != null) {
    //          String name = sfield.forField();
    //          if (StringUtils.isBlank(name) == true) {
    //            name = cm.getName();
    //          }
    //          ret.put(name, cm);
    //        }
    //      }
    //      {
    //        Fields fields = cm.findAnnoation(Fields.class);
    //        if (fields != null) {
    //          for (Field field : fields.value()) {
    //            String name = field.name();
    //            if (StringUtils.isBlank(name) == true) {
    //              name = cm.getName();
    //            }
    //            ret.put(name, cm);
    //          }
    //        }
    //      }
    //      {
    //        SortableFields sfields = cm.findAnnoation(SortableFields.class);
    //        if (sfields != null) {
    //          for (SortableField field : sfields.value()) {
    //            String name = field.forField();
    //            if (StringUtils.isBlank(name) == true) {
    //              name = cm.getName();
    //            }
    //            ret.put(name, cm);
    //          }
    //        }
    //      }
    //      {
    //        ContainedIn ci = cm.findAnnoation(ContainedIn.class);
    //        if (ci != null) {
    //          IndexedEmbedded iemb = cm.findAnnoation(IndexedEmbedded.class);
    //          if (iemb == null) {
    //            iemb = DEFAULT_IndexedEmbedded;
    //          }
    //          addNestedSearchFields(emgr, repo, cm, iemb, maxDepth - 1, ret);
    //        } else {
    //          IndexedEmbedded iemb = cm.findAnnoation(IndexedEmbedded.class);
    //          if (iemb != null) {
    //            addNestedSearchFields(emgr, repo, cm, iemb, maxDepth - 1, ret);
    //          }
    //        }
    //      }
    //    }
    //    TODO RK addCustomSearchFields(ret, entm);

    return ret;
  }

  private static void addHibernateSearchOwnFields(ISearchEmgr<?> emgr, Map<String, SearchColumnMetadata> ret,
      EntityMetadata entm)
  {
    IndexedTypeDescriptor tddesc = emgr.getFullTextEntityManager().getSearchFactory()
        .getIndexedTypeDescriptor(entm.getJavaType());
    if (tddesc == null) {
      return;
    }
    for (PropertyDescriptor idesc : tddesc.getIndexedProperties()) {
      String name = idesc.getName();
      if (ret.containsKey(name) == true) {
        continue;
      }
      System.out.println("Found index: " + entm.getJavaType().getName() + "." + name);
      ColumnMetadata coldesc = entm.findColumn(name);

      if (coldesc == null) {
        ColumnMetadataBean dcoldesc = new ColumnMetadataBean(entm);
        dcoldesc.setName(name);
        dcoldesc.setJavaType(String.class);
        coldesc = dcoldesc;
      }
      Set<FieldDescriptor> indexedfields = idesc.getIndexedFields();
      indexedfields.size();

      if (idesc.getIndexedFields().isEmpty() == true) {
        LOG.warn("Field has no indexed fields: " + entm.getJavaType().getName() + "." + name);
      }
      boolean isId = idesc.isId();
      for (FieldDescriptor fdesc : idesc.getIndexedFields()) {
        SearchColumnMetadataBean nb = new SearchColumnMetadataBean(fdesc.getName(), coldesc);
        switch (fdesc.getType()) {
          case BASIC:
            nb.setIndexType(String.class);
            break;
          case NUMERIC:
            nb.setIndexType(Long.class);
            break;
          case SPATIAL:
            nb.setIndexType(void.class);
            break;
        }
        nb.setIdField(isId);
        nb.setIndexed(fdesc.getIndex() == Index.YES);
        nb.setAnalyzed(fdesc.getAnalyze() == Analyze.YES);
        nb.setStored(fdesc.getStore() == Store.YES);
        ret.put(fdesc.getName(), nb);
      }

    }
  }

  private static void addCustomSearchFields(ISearchEmgr<?> emgr, EntityMetadata entm,
      Map<String, SearchColumnMetadata> ret)
  {
    List<HibernateSearchInfo> cil = ClassUtils.findClassAnnotations(entm.getJavaType(), HibernateSearchInfo.class);
    for (HibernateSearchInfo ci : cil) {
      HibernateSearchFieldInfoProvider fip = PrivateBeanUtils.createInstance(ci.fieldInfoProvider());
      Map<String, SearchColumnMetadata> sm = fip.getAdditionallySearchFields(entm, ci.param());
      ret.putAll(sm);
    }
  }

  //  private static void addNestedSearchFields(ISearchEmgr<?> emgr, JpaMetadataRepostory repo, ColumnMetadata masterColumn,
  //      IndexedEmbedded iemb, int maxDepth,
  //      Map<String, ColumnMetadata> ret)
  //  {
  //    if (maxDepth < 1) {
  //      return;
  //    }
  //    String[] paths = iemb.includePaths();
  //    if (paths.length > 0) {
  //      for (String path : paths) {
  //        ret.put(masterColumn.getName() + "." + path, masterColumn);
  //      }
  //      return;
  //    }
  //    EntityMetadata assicatedEntity = getAssocatedJavaType(masterColumn);
  //    if (assicatedEntity == null) {
  //      LOG.error("Search; Cannot find EntityMetadata for " + masterColumn.getJavaType().getName());
  //      return;
  //    }
  //    int maxLevel = Math.min(iemb.depth(), maxDepth);
  //    // 
  //    //    Map<String, ColumnMetadata> nested = getSearchFields(emgr, repo, assicatedEntity, maxLevel);
  //    //    for (Map.Entry<String, ColumnMetadata> me : nested.entrySet()) {
  //    //      ret.put(masterColumn.getName() + "." + me.getKey(), masterColumn);
  //    //    }
  //  }

  //  private static EntityMetadata getAssocatedJavaType(ColumnMetadata masterColumn)
  //  {
  //    EntityMetadata targetEnt = masterColumn.getTargetEntity();
  //    Class<?> javaType = masterColumn.getJavaType();
  //    if (Collection.class.isAssignableFrom(javaType) == true || Map.class.isAssignableFrom(javaType) == true) {
  //      if (targetEnt == null) {
  //        LOG.error("Have to define attribute targetEntity in anntotation to support search: "
  //            + masterColumn.getShortDeclaration());
  //      }
  //    }
  //    return targetEnt;
  //  }

}
