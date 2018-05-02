package de.micromata.genome.logging.config;

import de.micromata.genome.logging.Escape;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class EscapeNullBytes
{
  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
      { "MyValue", "MyValue" },
      { "My\u0000Value", "My\\0Value" },
      { "", "" },
      { null, null },
    });
  }

  @Parameterized.Parameter(0)
  public String untrustedData;

  @Parameterized.Parameter(1)
  public String expectedEncodedData;

  @Test()
  public void test()
  {
    String encodedData = Escape.nullBytes(untrustedData);
    assertEquals(expectedEncodedData, encodedData);
  }
}
