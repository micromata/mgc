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
package de.micromata.genome.logging.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class FormUrlEncodedBodyWriterTest
{
  @Parameterized.Parameter(0)
  public Map<String, String[]> parameterMap;

  @Parameterized.Parameter(1)
  public String encoding;

  @Parameterized.Parameter(2)
  public String expectedBody;

  @Parameterized.Parameters(name = "{2}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {

       //UrlEncode as UTF-8
      {
        new HashMap<String, String[]>()
        {{
          put("testA", new String[]{"WertÄ"});
        }},
        "UTF-8",
        "testA=Wert%C3%84"
      },

      //UrlEncode as ISO-8859-1
      {
        new HashMap<String, String[]>()
        {{
          put("testA", new String[]{"WertÄ"});
        }},
        "ISO-8859-1",
        "testA=Wert%C4"
      },

      //No Parameter
      {
        new HashMap<String, String[]>(),
        "ISO-8859-1",
        ""
      },

      //Parametername is null
      {
        new HashMap<String, String[]>()
        {{
          put(null, new String[]{"WertX"});
          put("testA", new String[]{"WertY"});
        }},
        "ISO-8859-1",
        "testA=WertY"
      },

      //Parametervalue is null
      {
        new HashMap<String, String[]>()
        {{
          put("testA", new String[]{"WertX", null});
          put("testB", new String[]{null, "WertY"});
        }},
        "ISO-8859-1",
        "testB=WertY&testA=WertX"
      },


      //Parameterkey or value is empty
      {
        new HashMap<String, String[]>()
        {{
          put("", new String[]{"WertX"});
          put("testB", new String[]{""});
        }},
        "ISO-8859-1",
        ""
      },

      //Some Keys and Values
      {
        new HashMap<String, String[]>()
        {{
          put("testA", new String[]{"WertX", "WertY"});
          put("testB", new String[]{"WertZ"});
        }},
        "UTF-8",
        "testB=WertZ&testA=WertX&testA=WertY"
      }
    });
  }

  @Test
  public void testLoadDefault()
  {
    FormUrlEncodedBodyWriter writer = new FormUrlEncodedBodyWriter(parameterMap, encoding);
    String body = writer.createBody();

    assertEquals(expectedBody, body);
  }
}
