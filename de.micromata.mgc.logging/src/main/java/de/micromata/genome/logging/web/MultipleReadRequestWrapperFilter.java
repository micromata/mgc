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

import org.apache.commons.lang3.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * This Filter wraps a HttpServletRequest into MultipleReadRequestWrapper
 *
 * That makes it possible to get the request body from the cache of the MultipleReadRequestWrapper
 * after it is already read
 *
 * This Filter should be at an early position in the Filter Pipeline
 */
public class MultipleReadRequestWrapperFilter implements Filter
{
  private Integer maxCacheSize;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException
  {
    String param = filterConfig.getInitParameter("maxCachedContentInBytes");
    if(StringUtils.isBlank(param) == false){
      maxCacheSize = Integer.parseInt(param);
    }
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
    throws IOException, ServletException
  {
    ServletRequest wrappedRequest = servletRequest;
    if (servletRequest instanceof HttpServletRequest) {
      wrappedRequest = new MultipleReadRequestWrapper((HttpServletRequest)servletRequest, maxCacheSize);
    }

    filterChain.doFilter(wrappedRequest, servletResponse);
  }

  @Override
  public void destroy()
  {
  }
}
