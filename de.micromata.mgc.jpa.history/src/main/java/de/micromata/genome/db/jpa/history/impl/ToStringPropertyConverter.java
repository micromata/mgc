package de.micromata.genome.db.jpa.history.impl;

import org.apache.commons.lang.ObjectUtils;

import de.micromata.genome.jpa.metainf.ColumnMetadata;

/**
 * Reads property an use toString() on the return value.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ToStringPropertyConverter extends SimplePropertyConverter
{

  @Override
  protected String convertToString(Object value, ColumnMetadata pd)
  {
    String ret = ObjectUtils.toString(value);
    return ret;
  }
}
