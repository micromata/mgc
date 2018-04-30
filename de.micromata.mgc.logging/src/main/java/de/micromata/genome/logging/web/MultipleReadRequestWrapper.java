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

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Map;

/**
 * This wrapper caches the read content into a ByteArray.
 * You can retrieve the content AFTER it was read with the getCacheAsString() Method.
 *
 * The cache can be limited by the ctor parameter maxCacheSize.
 * If the body is accessed by the HttpServletRequest.getParameter* Methods. This
 * class recognize it and return the parametermap as from url encoded.
 *
 * To work correctly it needs a very early Filter that wraps the HttpServletRequest into a MultipleReadRequestWrapper
 * You can use MultipleReadRequestWrapperFilter
 */
public class MultipleReadRequestWrapper extends HttpServletRequestWrapper {

  private static final String CONTENT_TYPE_FORM_URL_ENCODED = "application/x-www-form-urlencoded";
  private int maxCacheSize;
  private CacheSizeExceededHandler cacheSizeExceededHandler = null;
  private CachedInputStream cachedInputStream;
  private String cachedRequestParameters;
  private BufferedReader reader;

  /**
   *  Instantiates a new MultipleReadRequestWrapper
   * @param request the request
   */
  public MultipleReadRequestWrapper(HttpServletRequest request)
  {
    this(request, Integer.MAX_VALUE, null);
  }

  /**
   *  Instantiates a new MultipleReadRequestWrapper
   * @param request the request
   * @param maxCacheSize bytes that are cached
   */
  public MultipleReadRequestWrapper(HttpServletRequest request, int maxCacheSize)
  {
    this(request, maxCacheSize, null);
  }

  /**
   *  Instantiates a new MultipleReadRequestWrapper
   * @param request the request
   * @param maxCacheSize bytes that are cached
   * @param cacheSizeExceededHandler handler that fires if max cache bytes reached
   */
  public MultipleReadRequestWrapper(HttpServletRequest request, int maxCacheSize, CacheSizeExceededHandler cacheSizeExceededHandler)
  {
    super(request);
    this.maxCacheSize = maxCacheSize;
    this.cacheSizeExceededHandler = cacheSizeExceededHandler;
  }

  /**
   * Walks through the wrapped Requests in the HttpServletRequestWrapper and finds the first MultipleReadRequestWrapper
   * @param req the request
   * @return the MultipleReadRequestWrapper or null
   */
  public static MultipleReadRequestWrapper findMultipleReadRequestInWrappedRequests(ServletRequest req)
  {
    ServletRequest currentRequest = req;
    while(currentRequest instanceof HttpServletRequestWrapper){
      HttpServletRequestWrapper wrapper = (HttpServletRequestWrapper)currentRequest;

      if(wrapper instanceof MultipleReadRequestWrapper){
        return (MultipleReadRequestWrapper)wrapper;
      }

      currentRequest = wrapper.getRequest();
    }
    return null;
  }

  @Override
  public ServletInputStream getInputStream() throws IOException
  {
    if (this.cachedInputStream == null) {
      this.cachedInputStream = new CachedInputStream(getRequest().getInputStream(), getCacheSize(), cacheSizeExceededHandler);
    }
    return this.cachedInputStream;
  }

  private int getCacheSize()
  {
    int requestContentLength = Math.max(0, getRequest().getContentLength());

    if(maxCacheSize >= requestContentLength){
      return requestContentLength;
    }

    return maxCacheSize;
  }

  @Override
  public String getCharacterEncoding()
  {
    String enc = super.getCharacterEncoding();
    return (enc != null ? enc : Charset.defaultCharset().name());
  }

  @Override
  public BufferedReader getReader() throws IOException
  {
    if (this.reader == null) {
      this.reader = new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
    }
    return this.reader;
  }

  @Override
  public String getParameter(String name) {
    if (shouldWriteRequestParametersToCachedContent()) {
      cacheRequestParameters();
    }
    return super.getParameter(name);
  }

  protected boolean shouldWriteRequestParametersToCachedContent(){
    return isCacheEmpty() && isFormPostUrlEncoded();
  }

  private boolean isCacheEmpty()
  {
    return cachedInputStream == null || cachedInputStream.getCurrentCacheSize() == 0;
  }

  private boolean isFormPostUrlEncoded() {
    String contentType = getContentType();
    return contentType != null &&
      contentType.contains(CONTENT_TYPE_FORM_URL_ENCODED) &&
      getMethod().equalsIgnoreCase("POST");
  }

  private void cacheRequestParameters() {
    FormUrlEncodedBodyWriter writer = new FormUrlEncodedBodyWriter(super.getParameterMap(), getCharacterEncoding());
    cachedRequestParameters = writer.createBody();
  }

  @Override
  public Map<String, String[]> getParameterMap() {
    if (shouldWriteRequestParametersToCachedContent()) {
      cacheRequestParameters();
    }
    return super.getParameterMap();
  }

  @Override
  public Enumeration<String> getParameterNames() {
    if (shouldWriteRequestParametersToCachedContent()) {
      cacheRequestParameters();
    }
    return super.getParameterNames();
  }

  @Override
  public String[] getParameterValues(String name) {
    if (shouldWriteRequestParametersToCachedContent()) {
      cacheRequestParameters();
    }
    return super.getParameterValues(name);
  }

  public String getCacheAsString() throws IOException
  {
    if(isCacheEmpty()){
      return cachedRequestParameters;
    }
    return cachedInputStream.getCacheAsString(getCharacterEncoding());
  }
}
