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

package de.micromata.mgc.email;

/**
 * @author Kai Reinhard (k.reinhard@micromata.de)
 * 
 */
public class MailFilter
{
  private boolean onlyRecent;

  /**
   * If true then only the recent (unseen) messages will get from the mail server. <br/>
   * Default is false.
   */
  public boolean isOnlyRecent()
  {
    return onlyRecent;
  }

  public void setOnlyRecent(boolean onlyRecent)
  {
    this.onlyRecent = onlyRecent;
  }
}
