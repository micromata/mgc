package de.micromata.genome.jpa.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.micromata.genome.jpa.StdRecordDO;

@Entity
@Table(name = "TB_TEST_JPAT2", //
    uniqueConstraints = { //
        @UniqueConstraint(columnNames = { "firstName" }, name = "IX_TEST_JPAT2_FIRSTNAME") })
@SequenceGenerator(name = "SQ_TEST_JPAT2_PK", sequenceName = "SQ_TEST_JPAT2_PK")
public class GenomeJpaTest2TableDO extends StdRecordDO<Long>
{

  private static final long serialVersionUID = 7394333962220924260L;

  private Long pk;

  private String firstName;

  @Override
  @Id
  @Column(name = "TEST_JPAT2_PK")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TEST_JPAT2_PK")
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

}
