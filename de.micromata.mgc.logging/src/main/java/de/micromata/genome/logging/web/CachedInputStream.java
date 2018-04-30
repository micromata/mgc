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

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This Stream wraps an existing InputStream and caches the read content into a ByteArray.
 */
public class CachedInputStream extends ServletInputStream
{
  private final ServletInputStream wrappedInputStream;
  private final ByteArrayOutputStream cache;
  private final Integer cacheSize;
  private final CacheSizeExceededHandler cacheSizeExceededHandler;
  private boolean cacheSizeReached = false;

  /**
   * Instantiates a new CachedInputStream.
   * @param wrappedInputStream the wrapped InputStream
   * @param cacheSize the maximal cacheSize
   */
  public CachedInputStream(ServletInputStream wrappedInputStream, Integer cacheSize)
  {
    this(wrappedInputStream, cacheSize, null);
  }

  /**
   * Instantiates a new CachedInputStream.
   * @param wrappedInputStream  the wrapped InputStream
   * @param cacheSize the maximal cacheSize
   * @param cacheSizeExceededHandler handler that is invoked if the cacheSize is exceeded
   */
  public CachedInputStream(ServletInputStream wrappedInputStream, Integer cacheSize, CacheSizeExceededHandler cacheSizeExceededHandler)
  {
    if(wrappedInputStream == null) {
      throw new IllegalArgumentException("wrappedInputStream is null");
    }

    if(cacheSize == null) {
      throw new IllegalArgumentException("cacheSize is null");
    }

    this.wrappedInputStream = wrappedInputStream;
    this.cache = new ByteArrayOutputStream(cacheSize);
    this.cacheSize = cacheSize;
    this.cacheSizeExceededHandler = cacheSizeExceededHandler;
  }

  @Override
  public int read() throws IOException
  {
    int ch = wrappedInputStream.read();
    if(ch == -1 || cacheSizeReached){
      return ch;
    }

    if(cacheIfEnoughSpaceLeft(ch) == false){
      cacheSizeReached = true;
      fireCacheSizeReachedHandler();
    }

    return ch;
  }

  private void fireCacheSizeReachedHandler()
  {
    if(cacheSizeExceededHandler != null) {
      cacheSizeExceededHandler.cacheSizeExceeded(getCacheAsByteArray(), cacheSize);
    }
  }

  private boolean cacheIfEnoughSpaceLeft(int ch)
  {
    if (cache.size() >= cacheSize) {
      return false;
    }
    cache.write(ch);
    return true;
  }

  @Override
  public boolean isFinished()
  {
    return wrappedInputStream.isFinished();
  }

  @Override
  public boolean isReady()
  {
    return wrappedInputStream.isReady();
  }

  @Override
  public void setReadListener(ReadListener readListener)
  {
    wrappedInputStream.setReadListener(readListener);
  }

  /**
   * Gets the current size of the cache
   * @return the current size of the cache
   */
  public int getCurrentCacheSize()
  {
    return this.cache.size();
  }

  /**
   * Gets the current cache as byte array
   * @return cache as byte array
   */
  public byte[] getCacheAsByteArray()
  {
    return this.cache.toByteArray();
  }

  /**
   * Gets the current cache as string
   * @param characterEncoding the characterEncoding
   * @return current cache as string
   * @throws IOException
   */
  public String getCacheAsString(String characterEncoding) throws IOException
  {
    return IOUtils.toString(getCacheAsByteArray(), characterEncoding);
  }
}
