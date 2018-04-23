//
// Copyright (C) 2010-2018 Micromata GmbH
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

package de.micromata.genome.logging.config;

import de.micromata.genome.logging.Escape;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class EscapeTests
{
  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
      { "MyValue", "MyValue" },
      { "MyValue\r", "MyValue\\r" },
      { "MyValue\n", "MyValue\\n" },
      { "MyValue\r\n", "MyValue\\r\\n" },
      { "My\r\nValue", "My\\r\\nValue" },
      { "My\tValue", "My\\tValue" }
    });
  }

  @Parameterized.Parameter(0)
  public String untrustedData;

  @Parameterized.Parameter(1)
  public String expectedEncodedData;

  @Test()
  public void test()
  {
    String encodedData = Escape.forLog(untrustedData);
    assertEquals(expectedEncodedData, encodedData);
  }
}
