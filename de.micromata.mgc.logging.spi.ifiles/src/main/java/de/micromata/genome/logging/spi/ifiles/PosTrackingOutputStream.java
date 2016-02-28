package de.micromata.genome.logging.spi.ifiles;

import java.io.IOException;
import java.io.OutputStream;

public class PosTrackingOutputStream extends OutputStream
{
  long position = 0L;
  private OutputStream nested;

  public PosTrackingOutputStream(OutputStream other)
  {
    this.nested = other;
  }

  @Override
  public void write(int b) throws IOException
  {
    nested.write(b);
    ++position;
  }

  @Override
  public void write(byte b[], int off, int len) throws IOException
  {
    nested.write(b, off, len);
    position += len;
  }

  @Override
  public void write(byte[] b) throws IOException
  {
    nested.write(b);
    position += b.length;
  }

  @Override
  public void flush() throws IOException
  {
    nested.flush();
  }

  @Override
  public void close() throws IOException
  {
    nested.close();
  }

  public long getPosition()
  {
    return position;
  }

  public void setPosition(long position)
  {
    this.position = position;
  }

  public OutputStream getNested()
  {
    return nested;
  }

  public void setNested(OutputStream nested)
  {
    this.nested = nested;
  }

}
