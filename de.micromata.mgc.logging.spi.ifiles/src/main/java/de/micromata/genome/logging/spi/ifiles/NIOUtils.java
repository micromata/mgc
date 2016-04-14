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

package de.micromata.genome.logging.spi.ifiles;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class NIOUtils
{
  private static boolean unmapNotAvailable = false;
  private static Method unmapMethod = null;

  private static Method getUnmapMethod(FileChannel fc) throws NoSuchMethodException, SecurityException
  {
    if (unmapMethod != null) {
      return unmapMethod;
    }
    Class<?> fcClass = fc.getClass();
    unmapMethod = fcClass.getDeclaredMethod("unmap",
        new Class[] { java.nio.MappedByteBuffer.class });
    unmapMethod.setAccessible(true);
    return unmapMethod;
  }

  public static void unmap(FileChannel fc, MappedByteBuffer bb)
  {
    if (unmapNotAvailable == true) {
      return;
    }
    try {
      Method method = getUnmapMethod(fc);
      method.invoke(null, new Object[] { bb });
    } catch (Exception ex) {
      unmapNotAvailable = true;
    }
  }

  public static void write(MappedByteBuffer buffer, int pos, String asciString)
  {
    byte[] bytes = asciString.getBytes();
    for (int i = 0; i < bytes.length; ++i) {
      buffer.put(pos + i, bytes[i]);
    }
  }

  public static void getBytes(MappedByteBuffer buffer, int pos, byte[] target)
  {
    for (int i = 0; i < target.length; ++i) {
      target[i] = buffer.get(pos + i);
    }
  }

  public static String readAsciiString(MappedByteBuffer buffer, int pos, int length)
  {
    byte[] target = new byte[length];
    getBytes(buffer, pos, target);
    return new String(target);
  }

  /**
   * reads from current position.
   * 
   * @param channel
   * @param length
   * @param charset
   * @return
   */
  public static String readString(DataInput input, int length, Charset charset) throws IOException
  {
    byte[] data = new byte[length];
    input.readFully(data);
    return new String(data, charset);
  }
}
