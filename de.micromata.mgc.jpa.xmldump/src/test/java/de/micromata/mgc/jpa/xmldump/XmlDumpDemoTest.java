package de.micromata.mgc.jpa.xmldump;

import java.io.File;

import de.micromata.genome.db.jpa.xmldump.api.JpaXmlDumpService;
import de.micromata.genome.db.jpa.xmldump.api.JpaXmlDumpService.RestoreMode;
import de.micromata.genome.db.jpa.xmldump.impl.JpaXmlDumpServiceImpl;

public class XmlDumpDemoTest
{
  public void testDemo()
  {
    JpaXmlDumpService xmlDumpService = new JpaXmlDumpServiceImpl();
    XmlDumpEntityMgrFactory emfac = XmlDumpEntityMgrFactory.get();
    // do some inserts
    // ...

    File file = new File("target/db-dump1.xml");
    // dump all tables of the persistence context into an xml file
    xmlDumpService.dumpToXml(emfac, file);

    // empty all tables of the persistence context
    emfac.getJpaSchemaService().clearDatabase();
    // load dump back into persistence context
    xmlDumpService.restoreDb(emfac, file, RestoreMode.InsertAll);
  }
}
