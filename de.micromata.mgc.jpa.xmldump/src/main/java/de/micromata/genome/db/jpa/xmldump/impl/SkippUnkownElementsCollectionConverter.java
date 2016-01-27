package de.micromata.genome.db.jpa.xmldump.impl;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * The Class CollectionConverter.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class SkippUnkownElementsCollectionConverter
    extends com.thoughtworks.xstream.converters.collections.CollectionConverter
{

  private static final Logger LOG = Logger.getLogger(SkippUnkownElementsCollectionConverter.class);

  public SkippUnkownElementsCollectionConverter(Mapper mapper, Class type)
  {
    super(mapper, type);
  }

  public SkippUnkownElementsCollectionConverter(Mapper mapper)
  {
    super(mapper);

  }

  @Override
  protected void addCurrentElementToCollection(HierarchicalStreamReader reader, UnmarshallingContext context,
      Collection collection, Collection target)
  {
    try {
      Object item = readItem(reader, context, collection);
      target.add(item);
    } catch (CannotResolveClassException ex) {
      LOG.warn("Cannot reconstruct nested type: " + ex.getMessage());
    }
  }
}
