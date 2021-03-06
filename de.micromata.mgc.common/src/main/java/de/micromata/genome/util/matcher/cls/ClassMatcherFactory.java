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

package de.micromata.genome.util.matcher.cls;

import java.lang.annotation.Annotation;

import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.matcher.MatcherFactory;
import de.micromata.genome.util.matcher.string.SimpleWildcardMatcherFactory;

/**
 * Create class Matcher
 * 
 * extends:fqClassName Matches if class implements or extends class anot:fqClassName Matches if class has anotation of
 * given class anotprop:fqClassName@attr=value Matches if class has annotation an Matches Value can be a class, string,
 * array from class or array from string. In case of array it matches, if expression can be found in array.
 * 
 * @author roger@micromata.de
 * 
 */
public class ClassMatcherFactory implements MatcherFactory<Class<?>>
{

  /**
   * The Constant EXTENDS_PREFIX.
   */
  protected static final String EXTENDS_PREFIX = "extends:";

  /**
   * The Constant ANNOT_PREFIX.
   */
  protected static final String ANNOT_PREFIX = "annot:";

  /**
   * The Constant ANNOTPROP_PREFIX.
   */
  protected static final String ANNOTPROP_PREFIX = "annotprop:";

  /**
   * The string matcher factory.
   */
  private MatcherFactory<String> stringMatcherFactory = new SimpleWildcardMatcherFactory<String>();

  @Override
  @SuppressWarnings("unchecked")
  public Matcher<Class<?>> createMatcher(String pattern)
  {
    if (pattern.startsWith(EXTENDS_PREFIX) == true) {
      String cn = pattern.substring(EXTENDS_PREFIX.length());
      try {
        Class<?> extendsFrom = Class.forName(cn);
        return new ExtendsClassMatcher(extendsFrom);
      } catch (Exception ex) {
        throw new RuntimeException("Cannot find class: " + cn, ex);
      }
    }
    if (pattern.startsWith(ANNOT_PREFIX) == true) {
      String cn = pattern.substring(ANNOT_PREFIX.length());
      try {
        Class<?> anot = Class.forName(cn);
        return new AnnotationClassMatcher((Class<? extends Annotation>) anot);
      } catch (Exception ex) {
        throw new RuntimeException("Cannot find class or not a annotation: " + cn, ex);
      }
    }
    if (pattern.startsWith(ANNOTPROP_PREFIX) == true) {
      String cn = pattern.substring(ANNOTPROP_PREFIX.length());
      return createAnnotPropMatcher(cn);
    }
    return new ClassNameMatcher(stringMatcherFactory.createMatcher(pattern));
  }

  /**
   * Creates a new ClassMatcher object.
   *
   * @param expr the expr
   * @return the matcher
   */
  public Matcher<Class<?>> createAnnotPropMatcher(String expr)
  {
    int idx = expr.indexOf('@');
    if (idx == -1) {
      throw new RuntimeException("Cannot find Attribute '@' divider: " + expr);
    }
    String cn = expr.substring(0, idx);
    String rp = expr.substring(idx + 1);
    idx = rp.indexOf('=');
    if (idx == -1) {
      throw new RuntimeException("Cannot find Attribute '=' divider: " + expr);
    }
    String fieldName = rp.substring(0, idx);
    String fieldValue = rp.substring(idx + 1);
    Class<?> anot;
    try {
      anot = Class.forName(cn);
    } catch (Exception ex) {
      throw new RuntimeException("Cannot find class or not a annotation: " + cn, ex);
    }
    return new AnnotationAttributeClassMatcher((Class<? extends Annotation>) anot, fieldName, fieldValue);
  }

  @Override
  public String getRuleString(Matcher<Class<?>> matcher)
  {
    if ((matcher instanceof ExtendsClassMatcher) == true) {
      return EXTENDS_PREFIX + ((ExtendsClassMatcher) matcher).getSuperClass().getCanonicalName();
    }

    if ((matcher instanceof ClassNameMatcher) == true) {
      return stringMatcherFactory.getRuleString(((ClassNameMatcher) matcher).getNameMatcher());
    }
    if ((matcher instanceof AnnotationClassMatcher) == true) {
      AnnotationClassMatcher am = (AnnotationClassMatcher) matcher;
      return ANNOT_PREFIX + am.getAnnotationClass().getCanonicalName();
    }
    if ((matcher instanceof AnnotationAttributeClassMatcher) == true) {
      AnnotationAttributeClassMatcher am = (AnnotationAttributeClassMatcher) matcher;
      return ANNOTPROP_PREFIX + am.getAnnotationClass().getCanonicalName() + "@" + am.getFieldName() + "="
          + am.getFieldValue();
    }

    return "<unknown matcher>";
  }

}
