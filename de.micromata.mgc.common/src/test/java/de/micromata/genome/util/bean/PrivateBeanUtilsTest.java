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
