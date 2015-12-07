package de.micromata.genome.util.matcher;

import de.micromata.genome.util.bean.PrivateBeanUtils;

/**
 * A matcher, which checks if on a given bean the Field is matching.
 * 
 * It used PrivateBeanUtils to read the field.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <BEAN> the generic type
 * @param <FIELDTYPE> the generic type
 */
public class BeanPrivatePropMatcher<BEAN, FIELDTYPE>extends MatcherBase<BEAN>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -5031870129869115761L;

  /**
   * The field matcher.
   */
  private final Matcher<FIELDTYPE> fieldMatcher;

  /**
   * The field name.
   */
  private final String fieldName;

  /**
   * Instantiates a new bean private prop matcher.
   *
   * @param fieldName the field name
   * @param fieldMatcher the field matcher
   */
  public BeanPrivatePropMatcher(String fieldName, Matcher<FIELDTYPE> fieldMatcher)
  {
    this.fieldMatcher = fieldMatcher;
    this.fieldName = fieldName;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean match(BEAN object)
  {
    FIELDTYPE value = (FIELDTYPE) PrivateBeanUtils.readField(object, fieldName);
    return fieldMatcher.match(value);
  }

  @Override
  public String toString()
  {
    return "matchProp(" + fieldName + ": " + fieldMatcher.toString() + ")";
  }
}
