/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   23.02.2008
// Copyright Micromata 23.02.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.collection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.util.collections.MergeUtils;
import de.micromata.genome.util.types.Pair;
import de.micromata.genome.util.types.Triple;

public class MergeUtilsTest {
	@Test
	public void testMerge() {
		List<String> oldList = new ArrayList<String>();
		oldList.add("A");
		oldList.add("B");
		oldList.add("D");
		List<String> newList = new ArrayList<String>();
		newList.add("B");
		newList.add("C");
		Comparator<String> comp = new Comparator<String>() {

			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		};
		Triple<List<String>, List<Pair<String, String>>, List<String>> t = MergeUtils
				.mergeLists(oldList, newList, comp);
		Assert.assertEquals(1, t.getLeft().size());
		Assert.assertEquals("C", t.getLeft().get(0));
		Assert.assertEquals(1, t.getMiddle().size());
		Assert.assertEquals("B", t.getMiddle().get(0).getFirst());
		Assert.assertEquals(2, t.getRight().size());
		Assert.assertEquals("A", t.getRight().get(0));
	}
}
