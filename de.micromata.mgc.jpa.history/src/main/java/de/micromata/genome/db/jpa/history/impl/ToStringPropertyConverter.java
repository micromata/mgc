package de.micromata.genome.db.jpa.history.impl;

import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.List;

import de.micromata.genome.db.jpa.history.api.HistProp;

/**
 * Reads property an use toString() on the return value.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ToStringPropertyConverter extends SimplePropertyConverter
{

  @Override
  public List<HistProp> convert(Object entity, PropertyDescriptor pd)
  {
    Object bo = super.convert(entity, pd);
    if (bo == null) {

      return Collections.singletonList(new HistProp(null, String.class.getName(), null));
    }
    return Collections.singletonList(new HistProp(null, String.class.getName(), bo.toString()));
  }

}
