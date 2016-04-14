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

package de.micromata.genome.logging.spi.log4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A list which grows to maxSize and then is a queue.
 *
 * @author roger
 * @param <T> the generic type
 */
public class RoundList<T> implements List<T>, Iterable<T>
{

  /**
   * The max size.
   */
  private int maxSize = 1000;

  /**
   * The cur idx.
   */
  private int curIdx = 0;

  /**
   * The list.
   */
  private ArrayList<T> list;

  /**
   * Instantiates a new round list.
   */
  public RoundList()
  {
    initList();
  }

  /**
   * Instantiates a new round list.
   *
   * @param maxSize the max size
   */
  public RoundList(int maxSize)
  {
    this.maxSize = maxSize;
    initList();
  }

  /**
   * Inits the list.
   */
  private void initList()
  {
    list = new ArrayList<T>(maxSize);
  }

  @Override
  public boolean add(T o)
  {
    if (list.size() < maxSize) {
      list.add(o);
      // ++curSize;
    } else {
      list.set(curIdx, o);
      ++curIdx;
      if (curIdx >= maxSize) {
        curIdx = 0;
      }
    }
    return true;
  }

  /**
   * Cacl internal index.
   *
   * @param idx the idx
   * @return the int
   */
  public int caclInternalIndex(int idx)
  {
    int idxr = idx + curIdx;
    if (idxr < maxSize) {
      return idxr;
    }
    idxr = idxr - maxSize;
    if (idxr > maxSize) {
      throw new IndexOutOfBoundsException();
    }
    return idxr;
  }

  /**
   * Calc external index.
   *
   * @param idx the idx
   * @return the int
   */
  public int calcExternalIndex(int idx)
  {
    if (idx - curIdx < 0) {
      return curIdx - idx;
    } else {
      return idx - curIdx;
    }
  }

  @Override
  public void add(int index, T element)
  {
    index = caclInternalIndex(index);
    list.add(index, element);
  }

  @Override
  public boolean addAll(Collection< ? extends T> c)
  {
    for (T e : c) {
      add(e);
    }
    return true;
  }

  @Override
  public boolean addAll(int index, Collection< ? extends T> c)
  {
    index = caclInternalIndex(index);
    for (T e : c) {
      add(index, e);
    }
    return true;
  }

  @Override
  public void clear()
  {
    list.clear();
  }

  @Override
  public boolean contains(Object o)
  {
    return list.contains(o);
  }

  @Override
  public boolean containsAll(Collection< ? > c)
  {
    return list.containsAll(c);
  }

  @Override
  public T get(int index)
  {
    index = caclInternalIndex(index);
    return list.get(index);
  }

  @Override
  public int indexOf(Object o)
  {
    int idx = list.indexOf(o);
    if (idx == -1) {
      return -1;
    }
    return calcExternalIndex(idx);
  }

  @Override
  public boolean isEmpty()
  {
    return size() == 0;
  }

  /**
   * The Class Itr.
   *
   * @param <T> the generic type
   */
  public static class Itr<T> implements Iterator<T>, ListIterator<T>
  {

    /**
     * The rlist.
     */
    private RoundList<T> rlist;

    /**
     * The itr index.
     */
    private int itrIndex = 0;

    /**
     * Instantiates a new itr.
     *
     * @param rlist the rlist
     */
    public Itr(RoundList<T> rlist)
    {
      this.rlist = rlist;
    }

    /**
     * Instantiates a new itr.
     *
     * @param rlist the rlist
     * @param itrIndex the itr index
     */
    public Itr(RoundList<T> rlist, int itrIndex)
    {
      this.rlist = rlist;
      this.itrIndex = itrIndex;
    }

    @Override
    public boolean hasNext()
    {
      if (itrIndex >= rlist.size()) {
        return false;
      }
      return true;
    }

    @Override
    public T next()
    {
      return rlist.get(itrIndex++);

    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public void add(T o)
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPrevious()
    {
      return itrIndex > 0;
    }

    @Override
    public int nextIndex()
    {

      return itrIndex + 1;
    }

    @Override
    public T previous()
    {
      return rlist.get(itrIndex--);
    }

    @Override
    public int previousIndex()
    {
      return itrIndex - 1;
    }

    @Override
    public void set(T o)
    {
      rlist.set(itrIndex, o);
    }
  }

  @Override
  public Iterator<T> iterator()
  {
    return new Itr<T>(this);
  }

  @Override
  public int lastIndexOf(Object o)
  {
    int idx = list.lastIndexOf(o);
    return calcExternalIndex(idx);
  }

  @Override
  public ListIterator<T> listIterator()
  {
    return new Itr<T>(this);
  }

  @Override
  public ListIterator<T> listIterator(int index)
  {
    index = caclInternalIndex(index);
    return new Itr<T>(this, index);
  }

  @Override
  public boolean remove(Object o)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public T remove(int index)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(Collection< ? > c)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean retainAll(Collection< ? > c)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public T set(int index, T element)
  {
    index = caclInternalIndex(index);
    return list.set(index, element);
  }

  @Override
  public int size()
  {
    return list.size();
  }

  @Override
  public List<T> subList(int fromIndex, int toIndex)
  {
    fromIndex = caclInternalIndex(fromIndex);
    toIndex = caclInternalIndex(toIndex);
    return list.subList(fromIndex, toIndex);
  }

  @Override
  public Object[] toArray()
  {
    return list.toArray();
  }

  @Override
  public <X> X[] toArray(X[] a)
  {
    return list.toArray(a);
  }

}
