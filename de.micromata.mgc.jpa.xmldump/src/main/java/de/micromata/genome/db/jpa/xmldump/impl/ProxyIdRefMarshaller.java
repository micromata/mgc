package de.micromata.genome.db.jpa.xmldump.impl;

import org.hibernate.proxy.HibernateProxy;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.core.ReferenceByIdMarshaller;
import com.thoughtworks.xstream.core.SequenceGenerator;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ProxyIdRefMarshaller extends ReferenceByIdMarshaller
{
  /** The logger */
  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ProxyIdRefMarshaller.class);

  private Mapper classMapper;

  public ProxyIdRefMarshaller(HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper classMapper,
      IDGenerator idGenerator)
  {
    super(writer, converterLookup, classMapper, idGenerator);

    this.classMapper = classMapper;
  }

  public ProxyIdRefMarshaller(HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper classMapper)
  {
    this(writer, converterLookup, classMapper, new SequenceGenerator(1));
    this.classMapper = classMapper;
  }

  @Override
  public void convertAnother(Object item)
  {
    Class<?> targetClass = item.getClass();
    //    while (Enhancer.isEnhanced(targetClass) == true) {
    //      targetClass = targetClass.getSuperclass();
    //    }

    Converter converter = converterLookup.lookupConverterForType(targetClass);
    Object realItem = unbox(item);

    if (classMapper.isImmutableValueType(realItem.getClass())) {
      // strings, ints, dates, etc... don't bother using references.
      converter.marshal(item, writer, this);
    } else {
      super.convertAnother(realItem);
    }
  }

  /**
   * get the implementation behind object if it proxied, otherwise the object itself
   * 
   * @param <T> the type of the Object
   * @param object an object, might facaded by HibernateProxy
   * @return the initialized object behind the proxy
   */
  @SuppressWarnings("unchecked")
  public static <T> T unbox(T object)
  {
    if (object instanceof HibernateProxy) {
      HibernateProxy proxy = (HibernateProxy) object;
      return (T) proxy.getHibernateLazyInitializer().getImplementation();
    }
    return object;
  }
}
