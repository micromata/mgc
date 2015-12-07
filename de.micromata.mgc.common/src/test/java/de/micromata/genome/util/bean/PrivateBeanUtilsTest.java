/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   02.03.2008
// Copyright Micromata 02.03.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.bean;

import java.util.HashMap;
import java.util.TreeMap;

import org.junit.Test;

public class PrivateBeanUtilsTest {
	private int getSize(Object bean) {
		int size = PrivateBeanUtils.getBeanSize(bean);
		System.out.println("Size of [" + bean + "]: " + size);
		return size;
	}

	@Test
	public void testBeanSize() {
		String s = "asdfasdf";
		getSize(s);
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("a", "asdfasdf");
		hm.put("this", hm);
		getSize(hm);
		TreeMap<String, Object> tm = new TreeMap<String, Object>();
		tm.put("a", "asdfasdf");
		tm.put("this", tm);
		getSize(tm);
	}
}
