package de.micromata.genome.util.runtime.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import de.micromata.genome.util.bean.FieldMatchers;
import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.matcher.CommonMatchers;

/**
 * Using fields and nested of this instance to cast.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class AbstractCompositLocalSettingsConfigModel implements CastableLocalSettingsConfigModel
{

  @Override
  public <T extends LocalSettingsConfigModel> T castTo(Class<T> other)
  {
    List<Field> found = PrivateBeanUtils.findAllFields(getClass(),
        CommonMatchers.and(FieldMatchers.hasNotModifier(Modifier.STATIC),
            FieldMatchers.assignableTo(other)));
    if (found.isEmpty() == false) {
      return (T) PrivateBeanUtils.readField(this, found.get(0));
    }
    found = PrivateBeanUtils.findAllFields(getClass(),
        CommonMatchers.and(FieldMatchers.hasNotModifier(Modifier.STATIC),
            FieldMatchers.assignableTo(CastableLocalSettingsConfigModel.class)));
    for (Field f : found) {
      CastableLocalSettingsConfigModel ct = (CastableLocalSettingsConfigModel) PrivateBeanUtils.readField(this, f);
      T ret = ct.castTo(other);
      if (ret != null) {
        return ret;
      }
    }
    return null;
  }
}
