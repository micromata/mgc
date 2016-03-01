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
