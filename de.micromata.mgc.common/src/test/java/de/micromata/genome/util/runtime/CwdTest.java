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
      // Ausgabe: ./pom.xml: C:\Users\roger\d\micromata\genome\genome-commons\pom.xml
      System.out.println("./pom.xml: " + curFile.getAbsolutePath());
      File parentDir = new File("./..");
      // setze user dir auf parent file
      System.setProperty("user.dir", parentDir.getCanonicalFile().getAbsolutePath());
      File pafile = new File("pom.xml");
      // das gibt den namen parent pom aus!!!!
      // Ausgabe: new cwd: (..)./pom.xml: C:\Users\roger\d\micromata\genome\pom.xml
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
