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

package de.micromata.genome.db.jpa.normsearch.entities;

import de.micromata.genome.jpa.StdRecordDO;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * Base table for normalized search.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@MappedSuperclass
public abstract class NormSearchDO extends StdRecordDO<Long> {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1411599511298796724L;

	/**
	 * pk to do which is searched.
	 */
	private Long parent;

	/**
	 * The col name.
	 */
	private String colName;

	/**
	 * The value.
	 */
	private String value;

	/**
	 * Gets the col name.
	 *
	 * @return the col name
	 */
	@Column(name = "COLNAME", length = 30)
	public String getColName() {
		return colName;
	}

	/**
	 * Sets the col name.
	 *
	 * @param colName
	 *            the new col name
	 */
	public void setColName(String colName) {
		this.colName = colName;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	@Column(name = "VALUE", length = 50)
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value
	 *            the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	public Long getParent() {
		return parent;
	}

	/**
	 * Sets the parent.
	 *
	 * @param parent
	 *            the new parent
	 */
	public void setParent(Long parent) {
		this.parent = parent;
	}

}
