package de.micromata.genome.db.jpa.history.impl;

import java.util.Collections;
import java.util.List;

import de.micromata.genome.db.jpa.history.api.HistProp;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyConverter;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.metainf.ColumnMetadata;
import de.micromata.genome.util.strings.converter.StandardStringConverter;
import de.micromata.genome.util.strings.converter.StringConverter;

/**
 * The Class SimplePropertyConverter.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class SimplePropertyConverter implements HistoryPropertyConverter
{

  /**
   * The string converter.
   */
  private static StringConverter stringConverter = StandardStringConverter.get();

  @Override
  public List<HistProp> convert(IEmgr<?> emgr, HistoryMetaInfo historyMetaInfo, Object entity, ColumnMetadata pd)
  {
    Object value = pd.getGetter().get(entity);
    return Collections.singletonList(convertInternal(value, pd));
  }

  /**
   * Convert.
   *
   * @param value the value
   * @return the string
   */
  protected HistProp convertInternal(Object value, ColumnMetadata pd)
  {
    HistProp ret = new HistProp();
    ret.setName("");
    ret.setType(pd.getJavaType().getName());

    ret.setValue(convertToString(value, pd));
    return ret;
  }

  protected String convertToString(Object value, ColumnMetadata pd)
  {
    if (value instanceof Enum<?>) {
      return ((Enum<?>) value).name();
    }
    return stringConverter.asString(value);

  }
}
