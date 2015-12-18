package de.micromata.genome.jpa.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import de.micromata.genome.jpa.MarkDeletableRecord;
import de.micromata.genome.jpa.StdRecordDO;

@Entity
@Table(name = "TB_TEST_JPAT")
@SequenceGenerator(name = "SQ_TEST_JPAT_PK", sequenceName = "SQ_TEST_JPAT_PK")
public class GenomeJpaTestTableDO extends StdRecordDO<Long> implements MarkDeletableRecord<Long>
{

  private static final long serialVersionUID = 7394333962220924260L;

  private String firstName;
  private boolean deleted;

  @Override
  @Id
  @Column(name = "TEST_JPAT_PK")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TEST_JPAT_PK")
  public Long getPk()
  {
    return pk;
  }

  @Column(name = "FIRSTNAME", nullable = false, length = 50)
  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName(String textField)
  {
    this.firstName = textField;
  }

  @Override
  public void setPk(Long pk)
  {
    this.pk = pk;
  }

  @Override
  public boolean isDeleted()
  {
    return deleted;
  }

  @Override
  public void setDeleted(boolean deleted)
  {
    this.deleted = deleted;
  }

}
