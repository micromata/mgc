package de.micromata.genome.db.jpa.xmldump.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.metainf.EntityMetadata;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class XStreamRecordConverter implements Converter
{
  private static final Logger LOG = Logger.getLogger(XStreamRecordConverter.class);

  private final ConverterLookup converterLookup;
  private EmgrFactory<?> emgrfac;
  private Map<EntityMetadata, List<Object>> entities = new HashMap<>();
  private Map<Object, Object> xmlIdToObjectMap = new HashMap<>();

  private Map<Class<?>, EntityMetadata> tableClasses = new HashMap<>();

  public XStreamRecordConverter(XStream xstream, EmgrFactory<?> emgrfac)
  {
    converterLookup = new XStream().getConverterLookup();
    this.emgrfac = emgrfac;
    emgrfac.getMetadataRepository().getTableEntities().forEach((e) -> tableClasses.put(e.getJavaType(), e));
  }

  @Override
  public void marshal(Object arg0, HierarchicalStreamWriter arg1, MarshallingContext arg2)
  {
    converterLookup.lookupConverterForType(arg0.getClass()).marshal(arg0, arg1, arg2);
  }

  @Override
  public Object unmarshal(final HierarchicalStreamReader streamReader, UnmarshallingContext context)
  {
    Object result;
    Class<?> targetType = null;
    try {
      targetType = context.getRequiredType();
      Converter conv = converterLookup.lookupConverterForType(targetType);
      result = conv.unmarshal(streamReader, context);
    } catch (final Exception ex) {
      LOG.warn("Ignore unknown class or property " + targetType + " " + ex.getMessage());
      return null;
    } finally {
    }
    XStreamReferenceByIdUnmarshaller byund = (XStreamReferenceByIdUnmarshaller) context;
    registerObject(byund, result);

    return result;
  }

  private EntityMetadata findTableMetaData(Class<?> clazz)
  {
    for (EntityMetadata md : emgrfac.getMetadataRepository().getTableEntities()) {
      if (md.getJavaType().equals(clazz) == true) {
        return md;
      }
    }
    return null;
  }

  private void registerObject(XStreamReferenceByIdUnmarshaller byidunm, Object result)
  {
    if (result == null) {
      return;
    }

    EntityMetadata entityMetadata = findTableMetaData(result.getClass());
    if (entityMetadata == null) {
      return;
    }
    //    Object id = byidunm.getCurrentId();
    xmlIdToObjectMap.put(System.identityHashCode(result), result);
    List<Object> list = entities.get(entityMetadata);
    if (list == null) {
      list = new ArrayList<>();
      entities.put(entityMetadata, list);
    }
    list.add(result);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean canConvert(final Class clazz)
  {
    return tableClasses.containsKey(clazz);
  }

}
