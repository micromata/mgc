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
import org.apache.commons.lang3.Validate;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This Stream wraps an existing InputStream and caches the read content into a ByteArray.
 */
public class CachedInputStream extends ServletInputStream
{
  /**
   * Wraps the original input stream
   */
  private final ServletInputStream wrappedInputStream;

  /**
   * Read content is written to that cache until cacheSize exceeds
   */
  private final ByteArrayOutputStream cache;

  /**
   * maximal size of cache
   */
  private final int cacheSize;

  /**
   * handler that is invoked if the cacheSize is exceeded
    */
  private final CacheSizeExceededHandler cacheSizeExceededHandler;

  private boolean cacheSizeReached = false;

  /**
   * Instantiates a new CachedInputStream.
   * @param wrappedInputStream the wrapped InputStream
   * @param cacheSize the maximal cacheSize
   */
  public CachedInputStream(ServletInputStream wrappedInputStream, int cacheSize)
  {
    this(wrappedInputStream, cacheSize, null);
  }

  /**
   * Instantiates a new CachedInputStream.
   * @param wrappedInputStream  the wrapped InputStream
   * @param cacheSize the maximal cacheSize
   * @param cacheSizeExceededHandler handler that is invoked if the cacheSize is exceeded
   */
  public CachedInputStream(ServletInputStream wrappedInputStream, int cacheSize, CacheSizeExceededHandler cacheSizeExceededHandler)
  {
    Validate.notNull(wrappedInputStream, "wrappedInputStream is null");

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
   * @throws IOException is thrown when the cache conversion to {@link String} is failing
   */
  public String getCacheAsString(String characterEncoding) throws IOException
  {
    return IOUtils.toString(getCacheAsByteArray(), characterEncoding);
  }
}
