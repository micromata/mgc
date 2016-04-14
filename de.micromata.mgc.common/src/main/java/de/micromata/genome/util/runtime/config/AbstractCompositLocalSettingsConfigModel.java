//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.util.runtime.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.util.bean.FieldMatchers;
import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.matcher.CommonMatchers;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.validation.ValContext;

/**
 * Using fields and nested of this instance to cast.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class AbstractCompositLocalSettingsConfigModel extends AbstractLocalSettingsConfigModel
    implements CastableLocalSettingsConfigModel
{

  public static <M extends LocalSettingsConfigModel> M castTo(LocalSettingsConfigModel orig, Class<M> targetClass)
  {
    if (targetClass.isAssignableFrom(orig.getClass()) == true) {
      return (M) orig;
    }
    if (orig instanceof CastableLocalSettingsConfigModel) {
      return ((CastableLocalSettingsConfigModel) orig).castTo(targetClass);
    }
    return null;
  }

  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    super.fromLocalSettings(localSettings);
    List<Field> found = PrivateBeanUtils.findAllFields(getClass(),
        CommonMatchers.and(FieldMatchers.hasNotModifier(Modifier.STATIC),
            FieldMatchers.assignableTo(LocalSettingsConfigModel.class)));
    for (Field field : found) {
      LocalSettingsConfigModel nested = (LocalSettingsConfigModel) PrivateBeanUtils.readField(this, field);
      nested.fromLocalSettings(localSettings);
    }
  }

  @Override
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    super.toProperties(writer);
    List<Field> found = PrivateBeanUtils.findAllFields(getClass(),
        CommonMatchers.and(FieldMatchers.hasNotModifier(Modifier.STATIC),
            FieldMatchers.assignableTo(LocalSettingsConfigModel.class)));
    for (Field field : found) {
      LocalSettingsConfigModel nested = (LocalSettingsConfigModel) PrivateBeanUtils.readField(this, field);

      nested.toProperties(writer);
    }
    return writer;
  }

  @Override
  public void initializeConfiguration()
  {
    List<Field> found = PrivateBeanUtils.findAllFields(getClass(),
        CommonMatchers.and(FieldMatchers.hasNotModifier(Modifier.STATIC),
            FieldMatchers.assignableTo(LocalSettingsConfigModel.class)));
    for (Field field : found) {
      LocalSettingsConfigModel nested = (LocalSettingsConfigModel) PrivateBeanUtils.readField(this, field);
      nested.initializeConfiguration();
    }
  }

  @Override
  public void validate(ValContext ctx)
  {
    List<Field> found = PrivateBeanUtils.findAllFields(getClass(),
        CommonMatchers.and(FieldMatchers.hasNotModifier(Modifier.STATIC),
            FieldMatchers.assignableTo(LocalSettingsConfigModel.class)));
    for (Field field : found) {
      LocalSettingsConfigModel nested = (LocalSettingsConfigModel) PrivateBeanUtils.readField(this, field);
      ValContext sctx = ctx.createSubContext(nested, "");
      nested.validate(sctx);
    }

  }

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

  @Override
  public <T extends LocalSettingsConfigModel> List<T> castToCollect(Class<T> other)
  {
    List<T> ret = new ArrayList<>();
    List<Field> found = PrivateBeanUtils.findAllFields(getClass(),
        CommonMatchers.and(FieldMatchers.hasNotModifier(Modifier.STATIC),
            FieldMatchers.assignableTo(other)));
    if (found.isEmpty() == false) {
      ret.add((T) PrivateBeanUtils.readField(this, found.get(0)));
    }
    found = PrivateBeanUtils.findAllFields(getClass(),
        CommonMatchers.and(FieldMatchers.hasNotModifier(Modifier.STATIC),
            FieldMatchers.assignableTo(CastableLocalSettingsConfigModel.class)));
    for (Field f : found) {
      CastableLocalSettingsConfigModel ct = (CastableLocalSettingsConfigModel) PrivateBeanUtils.readField(this, f);
      List<T> sret = ct.castToCollect(other);
      ret.addAll(sret);
    }
    return ret;
  }
}
