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
import org.apache.commons.lang3.Validate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Writes a ParameterMap into a HTTP body form url encoded
 */
public class FormUrlEncodedBodyWriter
{
  private final Map<String, String[]> parameterMap;
  private final String characterEncoding;

  /**
   * Instantiates a new FormUrlEncodedBodyWriter.
   * @param parameterMap map of parameters when null a {@link NullPointerException} is thrown
   * @param characterEncoding the character encoding when null a {@link NullPointerException} is thrown
   */
  public FormUrlEncodedBodyWriter(Map<String, String[]> parameterMap, String characterEncoding)
  {
    Validate.notNull(parameterMap, "parameterMap is null");
    Validate.notNull(characterEncoding, "characterEncoding is null");

    this.parameterMap = parameterMap;
    this.characterEncoding = characterEncoding;
  }

  /**
   * Creates HTTP body form url encoded from the ParameterMap
   * @return the HTTP body form url encoded
   */
  public String createBody() {
    List<String> allParameters = parameterMap
      .entrySet()
      .stream()
      .flatMap(e -> getParametersUrlEncoded(e.getKey(), e.getValue()))
      .collect(Collectors.toList());
    return StringUtils.join(allParameters, "&");
  }

  private Stream<String> getParametersUrlEncoded(final String key, String[] parameters)
  {
    return Arrays
      .stream(parameters)
      .filter(p -> StringUtils.isBlank(key) == false && StringUtils.isBlank(p) == false)
      .map(p -> urlEncode(key) + "=" + urlEncode(p));
  }

  private String urlEncode(String value)
  {
    try{
      return URLEncoder.encode(value, characterEncoding);
    }catch (UnsupportedEncodingException e){
      throw new IllegalArgumentException("UnsupportedEncoding", e);
    }
  }
}
