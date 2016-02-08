package de.micromata.genome.db.jpa.normsearch.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import de.micromata.genome.jpa.DbRecordDO;

/**
 * Base table for normalized search.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@MappedSuperclass
public abstract class NormSearchDO extends DbRecordDO<Long> {

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
	@Column(name = "COLNAME", length = 30)
	private String colName;

	/**
	 * The value.
	 */
	@Column(name = "VALUE", length = 50)
	private String value;

	/**
	 * Gets the col name.
	 *
	 * @return the col name
	 */
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
