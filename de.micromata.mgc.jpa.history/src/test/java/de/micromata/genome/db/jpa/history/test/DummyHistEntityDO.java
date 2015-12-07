package de.micromata.genome.db.jpa.history.test;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.micromata.genome.db.jpa.history.api.WithHistory;
import de.micromata.genome.jpa.StdRecordDO;

/**
 * An Entity should write
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@WithHistory
@Entity()
@Table(name = "TST_DUMMY_HIST_ENTITY",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "longValue" },
            name = "IX_TST_DUMMY_HIST_ENTITY_LV") })
public class DummyHistEntityDO extends StdRecordDO
{

  private String stringValue;
  private Date dateValue;

  private int intValue;
  private long longValue;
  private boolean booleanValue;

  @Id
  @Column(name = "PK")
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Override
  public Long getPk()
  {
    return pk;
  }

  public String getStringValue()
  {
    return stringValue;
  }

  public void setStringValue(String stringValue)
  {
    this.stringValue = stringValue;
  }

  public Date getDateValue()
  {
    return dateValue;
  }

  public void setDateValue(Date dateValue)
  {
    this.dateValue = dateValue;
  }

  public int getIntValue()
  {
    return intValue;
  }

  public void setIntValue(int intValue)
  {
    this.intValue = intValue;
  }

  public long getLongValue()
  {
    return longValue;
  }

  public void setLongValue(long longValue)
  {
    this.longValue = longValue;
  }

  public boolean isBooleanValue()
  {
    return booleanValue;
  }

  public void setBooleanValue(boolean booleanValue)
  {
    this.booleanValue = booleanValue;
  }
}
