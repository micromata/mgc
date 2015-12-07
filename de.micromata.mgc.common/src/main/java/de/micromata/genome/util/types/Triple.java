/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   23.02.2008
// Copyright Micromata 23.02.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.types;

import java.io.Serializable;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Like Pair holds three values.
 *
 * @author roger@micromata.de
 * @param <L> the generic type
 * @param <M> the generic type
 * @param <R> the generic type
 */
public class Triple<L, M, R> implements Serializable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -1011236041210348804L;

  /**
   * The left.
   */
  final private L left;

  /**
   * The middle.
   */
  final private M middle;

  /**
   * The right.
   */
  final private R right;

  /**
   * Instantiates a new triple.
   *
   * @param left the left
   * @param middle the middle
   * @param right the right
   */
  public Triple(L left, M middle, R right)
  {
    this.left = left;
    this.middle = middle;
    this.right = right;
  }

  /**
   * Make.
   *
   * @param <L> the generic type
   * @param <M> the generic type
   * @param <R> the generic type
   * @param l the l
   * @param m the m
   * @param r the r
   * @return the triple
   */
  public static <L, M, R> Triple<L, M, R> make(L l, M m, R r)
  {
    return new Triple<L, M, R>(l, m, r);
  }

  public L getLeft()
  {
    return left;
  }

  public M getMiddle()
  {
    return middle;
  }

  public R getRight()
  {
    return right;
  }

  public L getFirst()
  {
    return left;
  }

  public M getSecond()
  {
    return middle;
  }

  public R getThird()
  {
    return right;
  }

  @Override
  public String toString()
  {
    final String result = new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE) //
        .append("1:", left) //
        .append("2:", middle) //
        .append("3:", right) //
        .toString();
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if ((obj instanceof Triple) == false) {
      return false;
    }
    Triple ot = (Triple) obj;
    return ObjectUtils.equals(left, ot.left) &&
        ObjectUtils.equals(middle, ot.middle) &&
        ObjectUtils.equals(right, ot.right);
  }

  @Override
  public int hashCode()
  {
    return (ObjectUtils.hashCode(left) * 31 + ObjectUtils.hashCode(middle)) * 31 + ObjectUtils.hashCode(right);
  }
}
