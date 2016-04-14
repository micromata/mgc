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

import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class PosTrackingOutputStream extends OutputStream
{
  int position = 0;
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

  public void flushToBackend() throws IOException
  {
    nested.flush();
  }

  @Override
  public void close() throws IOException
  {
    nested.close();
  }

  public int getPosition()
  {
    return position;
  }

  public void setPosition(int position)
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
