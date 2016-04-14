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

package de.micromata.genome.util.matcher;

/**
 * Thrown if a grammar in a matcher expression is invalid.
 * 
 * @author roger
 * 
 */
public class InvalidMatcherGrammar extends RuntimeException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 2572461712815733388L;

  /**
   * Instantiates a new invalid matcher grammar.
   */
  public InvalidMatcherGrammar()
  {
    super();
  }

  /**
   * Instantiates a new invalid matcher grammar.
   *
   * @param message the message
   * @param cause the cause
   */
  public InvalidMatcherGrammar(String message, Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Instantiates a new invalid matcher grammar.
   *
   * @param message the message
   */
  public InvalidMatcherGrammar(String message)
  {
    super(message);
  }

  /**
   * Instantiates a new invalid matcher grammar.
   *
   * @param cause the cause
   */
  public InvalidMatcherGrammar(Throwable cause)
  {
    super(cause);
  }

}
