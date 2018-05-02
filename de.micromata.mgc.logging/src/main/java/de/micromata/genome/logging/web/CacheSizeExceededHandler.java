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

/**
 * Handler is called when the cacheSize of the {@link CachedInputStream} exceeds
 *
 * Can be registered in a {@link CachedInputStream}
 */
public interface CacheSizeExceededHandler
{
  /**
   * Is called when the cacheSize of the {@link CachedInputStream} exceeds
   * @param cache the current cache
   * @param cacheSize the cacheSize
   */
  void cacheSizeExceeded(byte[] cache, int cacheSize);
}
