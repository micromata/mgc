package de.micromata.genome.db.jpa.xmldump.impl;

import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.core.ReferenceByIdUnmarshaller;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class XStreamReferenceByIdUnmarshaller extends ReferenceByIdUnmarshaller
{

  public XStreamReferenceByIdUnmarshaller(Object root, HierarchicalStreamReader reader, ConverterLookup converterLookup,
      Mapper mapper)
  {
    super(root, reader, converterLookup, mapper);
  }

  public Object getCurrentId()
  {
    return getCurrentReferenceKey();
  }
}
