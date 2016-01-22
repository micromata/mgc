package de.micromata.mgc.jpa.xmldump;

import java.util.List;

import org.junit.Test;

import de.micromata.genome.jpa.metainf.EntityMetadata;
import de.micromata.genome.jpa.metainf.MetaInfoUtils;
import de.micromata.mgc.common.test.MgcTestCase;

public class DumpDepHierarchieTest extends MgcTestCase
{
  XmlDumpEntityMgrFactory emfac = XmlDumpEntityMgrFactory.get();

  @Test
  public void testDump()
  {
    List<EntityMetadata> tables = emfac.getMetadataRepository().getTableEntities();
    System.out.println(MetaInfoUtils.dumpMetaTableReferenceByTree(tables, true));
  }

}
