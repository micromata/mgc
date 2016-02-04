/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   23.02.2008
// Copyright Micromata 23.02.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.micromata.genome.util.types.Pair;
import de.micromata.genome.util.types.Triple;

/**
 * The Class MergeUtils.
 *
 * @author roger@micromata.de
 */
public class MergeUtils
{

  /**
   * Merge lists.
   *
   * @param <LT> List type
   * @param oldList Old or source list
   * @param newList new or target list
   * @param comparator comparator with the sort criteria
   * @return a tripple. left is insert, middle update and right delete items
   */
  public static <LT> Triple<List<LT>, List<Pair<LT, LT>>, List<LT>> mergeLists(List<LT> oldList, List<LT> newList,
      Comparator<LT> comparator)
  {
    Collections.sort(oldList, comparator);
    Collections.sort(newList, comparator);
    List<Pair<LT, LT>> updateAttrs = new ArrayList<Pair<LT, LT>>();
    List<LT> insertAttrs = new ArrayList<LT>();
    List<LT> deleteAttrs = new ArrayList<LT>();
    int oi = 0;
    int ni = 0;
    do {
      if (oldList.size() <= oi && newList.size() <= ni) {
        break;
      }
      if (oldList.size() <= oi) {
        while (newList.size() > ni) {
          insertAttrs.add(newList.get(ni));
          ++ni;
        }
        break;
      }
      if (newList.size() <= ni) {
        while (oldList.size() > oi) {
          deleteAttrs.add(oldList.get(oi));
          ++oi;
        }
        break;
      }
      LT oa = oldList.get(oi);
      LT na = newList.get(ni);
      int c = comparator.compare(oa, na);

      if (c == 0) {
        updateAttrs.add(Pair.make(oa, na));
        ++oi;
        ++ni;
      } else if (c < 0) {
        deleteAttrs.add(oa);
        ++oi;
      } else {
        insertAttrs.add(na);
        ++ni;
      }

    } while (true);
    return Triple.make(insertAttrs, updateAttrs, deleteAttrs);
  }
}
