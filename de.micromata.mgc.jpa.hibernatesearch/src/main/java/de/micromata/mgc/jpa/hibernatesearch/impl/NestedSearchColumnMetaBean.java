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

package de.micromata.mgc.jpa.hibernatesearch.impl;

import org.hibernate.search.annotations.IndexedEmbedded;

import de.micromata.genome.jpa.metainf.ColumnMetadata;

/**
 * Represents an IndexedEmbedded field.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class NestedSearchColumnMetaBean extends SearchColumnMetadataBean
{
  private boolean resolved = false;
  private IndexedEmbedded indexEmbedded;

  public NestedSearchColumnMetaBean()
  {
    super();
  }

  public NestedSearchColumnMetaBean(String name, ColumnMetadata columnMetadata, IndexedEmbedded indexEmbedded)
  {
    super(name, columnMetadata);
    this.indexEmbedded = indexEmbedded;
  }

  public IndexedEmbedded getIndexEmbedded()
  {
    return indexEmbedded;
  }

  public void setIndexEmbedded(IndexedEmbedded indexEmbedded)
  {
    this.indexEmbedded = indexEmbedded;
  }

  public boolean isResolved()
  {
    return resolved;
  }

  public void setResolved(boolean resolved)
  {
    this.resolved = resolved;
  }

}
