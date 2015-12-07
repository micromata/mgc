package de.micromata.genome.jpa.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import de.micromata.genome.jpa.StdRecordDO;

@Entity
@Table(name = "TB_TEST_DETAIL")
@SequenceGenerator(name = "SQ_TEST_DETAIL_PK", sequenceName = "SQ_TEST_DETAIL_PK")
@org.hibernate.annotations.Table(//
indexes = {//
@Index(name = "IX_TEST_DETAIL_MODAT", columnNames = { "MODIFIEDAT"}),//
    @Index(name = "IX_TEST_DETAIL_PARENT", columnNames = { "PARENT_PK"}) //
}, appliesTo = "TB_TEST_DETAIL")
public class GenomeJpaDetailTableDO extends StdRecordDO
{

  private static final long serialVersionUID = 285376321479286358L;

  private Long pk;

  private GenomeJpaMasterTableDO parent;

  private String location;

  @Override
  @Id
  @Column(name = "TEST_DETAIL_PK")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TEST_DETAIL_PK")
  public Long getPk()
  {
    return pk;
  }

  @ManyToOne(optional = false)
  @JoinColumn(name = "PARENT_PK")
  public GenomeJpaMasterTableDO getParent()
  {
    return parent;
  }

  @Column(name = "LOCATION", nullable = false, length = 50)
  public String getLocation()
  {
    return location;
  }

  public void setLocation(String textField)
  {
    this.location = textField;
  }

  public void setPk(Long pk)
  {
    this.pk = pk;
  }

  public void setParent(GenomeJpaMasterTableDO parent)
  {
    this.parent = parent;
  }
}
