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
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


import static org.junit.Assert.assertEquals;

public class CachedInputStreamTest
{
  private static final Charset UTF_8 = StandardCharsets.UTF_8;

  @Test
  public void whenNothingWasRead_ShouldCacheNothing() throws IOException
  {
    CachedInputStream cached = makeCachedInputStream(5, "Hello");
    assertCachedStringIs(cached, "");
  }

  @Test
  public void whenAllWasRead_ShouldCacheAll() throws IOException
  {
    CachedInputStream cached = makeCachedInputStream(5, "Hello");
    String readContent = readToEnd(cached);
    assertCachedStringIs(cached, "Hello");
    assertEquals("Hello", readContent);
  }

  @Test
  public void whenAllWasReadButLimitedViaCacheSize_ShouldOnlyCacheUntilCacheSizeReached() throws IOException
  {
    CachedInputStream cached = makeCachedInputStream(2, "Hello");
    String readContent = readToEnd(cached);
    assertCachedStringIs(cached, "He");
    assertEquals("Hello", readContent);
  }

  @Test
  public void whenAllWasReadButCacheSizeIsZero_ShouldCacheNothing() throws IOException
  {
    CachedInputStream cached = makeCachedInputStream(0, "Hello");
    String readContent = readToEnd(cached);
    assertCachedStringIs(cached, "");
    assertEquals("Hello", readContent);
  }

  @Test
  public void whenReadTwoBytes_ShouldCacheOnlyTwoBytes() throws IOException
  {
    CachedInputStream cached = makeCachedInputStream(5, "Hello");
    cached.read();
    cached.read();
    assertCachedStringIs(cached, "He");
  }

  @Test
  public void whenReadEmpty_ShouldCacheNothing() throws IOException
  {
    CachedInputStream cached = makeCachedInputStream(5, "");
    cached.read();
    assertCachedStringIs(cached, "");
  }

  @Test
  public void whenCacheLimitReached_ShouldFireHandler() throws IOException
  {
    final boolean[] handlerFired = { false };
    CacheSizeExceededHandler handler = (cache, cacheSize) -> handlerFired[0] = true;

    ServletInputStream stream = makeInputStream("Hello");
    CachedInputStream cached =  new CachedInputStream(stream, 2, handler);
    cached.read();
    cached.read();
    cached.read();
    assertCachedStringIs(cached, "He");
    assertEquals(true, handlerFired[0]);
  }

  @Test
  public void whenCacheLimitNotReached_ShouldNotFireHandler() throws IOException
  {
    final boolean[] handlerFired = { false };
    CacheSizeExceededHandler handler = (cache, cacheSize) -> handlerFired[0] = true;

    ServletInputStream stream = makeInputStream("Hello");
    CachedInputStream cached =  new CachedInputStream(stream, 3, handler);
    cached.read();
    cached.read();
    cached.read();
    assertCachedStringIs(cached, "Hel");
    assertEquals(false, handlerFired[0]);
  }

  private CachedInputStream makeCachedInputStream(int cacheSize, String content)
  {
    ServletInputStream stream = makeInputStream(content);
    return new CachedInputStream(stream, cacheSize);
  }

  private void assertCachedStringIs(CachedInputStream cached, String expected) throws IOException
  {
    assertEquals(cached.getCacheAsString(UTF_8.name()), expected);
  }

  private String readToEnd(CachedInputStream cached) throws IOException
  {
    return IOUtils.toString(cached, UTF_8);
  }

  private ServletInputStream makeInputStream(String content)
  {
    InputStream stream = new ByteArrayInputStream(content.getBytes(UTF_8));
    return new MockServletInputStream(stream);
  }

  private static class MockServletInputStream extends ServletInputStream
  {
    private InputStream stream;

    public MockServletInputStream(InputStream stream)
    {
      this.stream = stream;
    }

    @Override
    public int read() throws IOException
    {
      return stream.read();
    }

    @Override
    public boolean isFinished()
    {
      throw new NotImplementedException();
    }

    @Override
    public boolean isReady()
    {
      throw new NotImplementedException();
    }

    @Override
    public void setReadListener(ReadListener readListener)
    {
      throw new NotImplementedException();
    }
  }
}


