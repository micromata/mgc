/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   04.07.2009
// Copyright Micromata 04.07.2009
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.matcher;

/**
 * Builder static methods.
 * 
 * @author roger@micromata.de
 * 
 */
public class CommonMatchers
{

  /**
   * Or.
   *
   * @param <T> the generic type
   * @param left the left
   * @param right the right
   * @return the matcher
   */
  public static <T> Matcher<T> or(Matcher<T> left, Matcher<T> right)
  {
    return new OrMatcher<T>(left, right);
  }

  /**
   * Or.
   *
   * @param <T> the generic type
   * @param elements the elements
   * @return the matcher
   */
  public static <T> Matcher<T> or(Matcher<T>... elements)
  {
    if (elements.length == 0) {
      throw new RuntimeException("CommonMatcherBuilder.or needs at least one arg");
    }
    if (elements.length == 1) {
      return elements[0];
    }
    return new OrMatcher<T>(elements[0], or(1, elements));
  }

  /**
   * Or.
   *
   * @param <T> the generic type
   * @param offset the offset
   * @param elements the elements
   * @return the matcher
   */
  public static <T> Matcher<T> or(int offset, Matcher<T>... elements)
  {
    if (elements.length == offset) {
      throw new RuntimeException("CommonMatcherBuilder.and needs at least one arg");
    }
    if (elements.length - offset == 1) {
      return elements[offset];
    }
    return new OrMatcher<T>(elements[offset], or(++offset, elements));
  }

  /**
   * And.
   *
   * @param <T> the generic type
   * @param elements the elements
   * @return the matcher
   */
  public static <T> Matcher<T> and(Matcher<T>... elements)
  {
    if (elements.length == 0) {
      throw new RuntimeException("CommonMatcherBuilder.and needs at least one arg");
    }
    if (elements.length == 1) {
      return elements[0];
    }
    return new AndMatcher<T>(elements[0], and(1, elements));
  }

  /**
   * And.
   *
   * @param <T> the generic type
   * @param offset the offset
   * @param elements the elements
   * @return the matcher
   */
  public static <T> Matcher<T> and(int offset, Matcher<T>... elements)
  {
    if (elements.length == offset) {
      throw new RuntimeException("CommonMatcherBuilder.and needs at least one arg");
    }
    if (elements.length - offset == 1) {
      return elements[offset];
    }
    return new AndMatcher<T>(elements[offset], and(++offset, elements));
  }

  /**
   * Not.
   *
   * @param <T> the generic type
   * @param nested the nested
   * @return the matcher
   */
  public static <T> Matcher<T> not(Matcher<T> nested)
  {
    return new NotMatcher<T>(nested);
  }

  /**
   * Group.
   *
   * @param <T> the generic type
   * @param nested the nested
   * @return the matcher
   */
  public static <T> Matcher<T> group(Matcher<T> nested)
  {
    return new GroupMatcher<T>(nested);
  }

  /**
   * Instance of.
   *
   * @param cls the cls
   * @return the matcher
   */
  public static Matcher<Object> instanceOf(Class<?> cls)
  {
    return new BeanInstanceOfMatcher(cls);
  }

  /**
   * Equal.
   *
   * @param <T> the generic type
   * @param obj the obj
   * @return the matcher
   */
  public static <T> Matcher<T> equal(T obj)
  {
    return new EqualsMatcher<T>(obj);
  }

  // Bitte nicht mehr einkommentieren
  //  public static <T> Matcher<T> equals(T other)
  //  {
  //    return new EqualsMatcher<T>(other);
  //  }

  /**
   * Never.
   *
   * @param <T> the generic type
   * @return the matcher
   */
  public static <T> Matcher<T> never()
  {
    return new NoneMatcher<T>();
  }

  /**
   * Always.
   *
   * @param <T> the generic type
   * @return the matcher
   */
  public static <T> Matcher<T> always()
  {
    return new EveryMatcher<T>();
  }
}
