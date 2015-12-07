package de.micromata.genome.util.runtime;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test effects on change current path.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class CwdTest
{
  @Test
  public void testCwd()
  {
    String oldpwd = System.getProperty("user.dir");
    try {
      File curFile = new File("pom.xml");
      String pom1 = FileUtils.readFileToString(curFile);
      // Ausgabe: ./pom.xml: C:\Users\roger\d\dhl\genome\genome-commons\pom.xml
      System.out.println("./pom.xml: " + curFile.getAbsolutePath());
      File parentDir = new File("./..");
      // setze user dir auf parent file
      System.setProperty("user.dir", parentDir.getCanonicalFile().getAbsolutePath());
      File pafile = new File("pom.xml");
      // das gibt den namen parent pom aus!!!!
      // Ausgabe: new cwd: (..)./pom.xml: C:\Users\roger\d\dhl\genome\pom.xml
      System.out.println("new cwd: (..)./pom.xml: " + pafile.getAbsolutePath());
      // !!!!!!
      // das liest trotzdem das pom1 aus!!!
      String pom2 = FileUtils.readFileToString(pafile);
      Assert.assertEquals(pom1, pom2);
      // das fixt das: 
      File pafile2 = pafile.getAbsoluteFile();
      String pom3 = FileUtils.readFileToString(pafile2);
      // jetzt tatsaechlich das parent pom
      Assert.assertNotEquals(pom1, pom3);
    } catch (IOException ex) {
      ex.printStackTrace();
      ;
    } finally {
      System.setProperty("user.dir", oldpwd);
    }
  }
}
