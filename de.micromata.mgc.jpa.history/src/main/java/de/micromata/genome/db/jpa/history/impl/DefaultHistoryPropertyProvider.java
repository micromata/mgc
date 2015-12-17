package de.micromata.genome.db.jpa.history.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.micromata.genome.db.jpa.history.api.HistProp;
import de.micromata.genome.db.jpa.history.api.HistoryProperty;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyConverter;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyProvider;
import de.micromata.genome.db.jpa.history.api.NoHistory;
import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.metainf.ColumnMetadata;
import de.micromata.genome.jpa.metainf.EntityMetadata;
import de.micromata.genome.jpa.metainf.JpaMetadataRepostory;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.util.bean.PrivateBeanUtils;

/**
 * Use the default properties of the entity.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class DefaultHistoryPropertyProvider implements HistoryPropertyProvider
{
  private static final Logger LOG = Logger.getLogger(DefaultHistoryPropertyProvider.class);

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public void getProperties(IEmgr<?> emgr, HistoryMetaInfo historyMetaInfo, Object entity, Map<String, HistProp> ret)
  {

    //      BeanInfo beanInfo = Introspector.getBeanInfo(entity.getClass(), Object.class);
    //      PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
    JpaMetadataRepostory mrepo = emgr.getEmgrFactory().getMetadataRepository();
    EntityMetadata entityMetadata = mrepo.findEntityMetadata(entity.getClass());
    if (entityMetadata == null) {
      LOG.error("Cannot find Entity Metadata for class: " + entity.getClass().getName());
      return;
    }

    for (Map.Entry<String, ColumnMetadata> me : entityMetadata.getColumns().entrySet()) {
      ColumnMetadata pd = me.getValue();
      if (ignoreForHistory(historyMetaInfo, entity, pd) == true) {
        continue;
      }
      HistoryPropertyConverter conv = getPropertyConverter(entity, pd);
      List<HistProp> values = conv.convert(emgr, historyMetaInfo, entity, pd);
      for (HistProp hp : values) {
        String key;
        if (StringUtils.isNotBlank(hp.getName()) == true) {
          key = pd.getName() + '.' + hp.getName();
        } else {
          key = pd.getName();
        }
        ret.put(key, hp);
      }
    }

  }

  /**
   * Gets the property converter.
   *
   * @param entity the entity
   * @param pd the pd
   * @return the property converter
   */
  protected HistoryPropertyConverter getPropertyConverter(Object entity, ColumnMetadata pd)
  {
    HistoryProperty annot = pd.findAnnoation(HistoryProperty.class);
    if (annot != null) {
      return PrivateBeanUtils.createInstance(annot.converter());
    }
    Class<?> pclazz = pd.getJavaType();

    if (DbRecord.class.isAssignableFrom(pclazz) == true) {
      return new DbRecordPropertyConverter();
    } else if (Map.class.isAssignableFrom(pclazz) == true) {
      LOG.fatal("Currenty not supported Map for History: " + entity.getClass() + "." + pd.getName());
    } else if (Collection.class.isAssignableFrom(pclazz) == true) {
      return new CollectionPropertyConverter();
    }

    return new SimplePropertyConverter();

  }

  /**
   * Read property value.
   *
   * @param entity the entity
   * @param pd the pd
   * @return the object
   */
  protected Object readPropertyValue(Object entity, PropertyDescriptor pd)
  {
    Method method = pd.getReadMethod();

    try {
      Object value = method.invoke(entity);
      return value;
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
      GLog.warn(GenomeLogCategory.Jpa, "Hist; Cannot read property: " + ex.getMessage(), new LogExceptionAttribute(ex));
      return null;
    }

  }

  /**
   * Ignore for history.
   *
   * @param historyMetaInfo the history meta info
   * @param entity the entity
   * @param pd the pd
   * @return true, if successful
   */
  protected boolean ignoreForHistory(HistoryMetaInfo historyMetaInfo, Object entity, ColumnMetadata pd)
  {
    if (historyMetaInfo.ignoreProperty(pd.getName()) == true) {
      return true;
    }
    if (isStandardIgnoreField(pd) == true) {
      return true;
    }
    if (pd.findAnnoation(Transient.class) != null) {
      return true;
    }
    if (pd.findAnnoation(NoHistory.class) != null) {
      return true;
    }
    return false;
  }

  /**
   * Checks if is standard ignore field.
   *
   * @param pd the pd
   * @return true, if is standard ignore field
   */
  protected boolean isStandardIgnoreField(ColumnMetadata pd)
  {
    String name = pd.getName();
    if (name.equals("updateCounter") == true ||
        name.equals("modifiedAt") == true ||
        name.equals("modifiedBy") == true) {
      return true;
    }
    return false;
  }

}
