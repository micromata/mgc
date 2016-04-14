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

package de.micromata.genome.logging;

/**
 * Interface for a notification to user.
 * 
 * Either a directMessage or an I18N key can be set.
 * 
 * 
 * @author roger@micromata.de
 * 
 */
public interface UserNotification
{

  /**
   * Direct message to user.
   *
   * @return the direct message
   */
  public String getDirectMessage();

  /**
   * I18N-Key for user
   * 
   * @return
   */
  public String getI18NKey();

  /**
   * Only used if I18N will be used
   * 
   * @return may return null
   */
  public String[] getMessageArgs();

  /**
   * For web frameworks, etc. Place to show notification
   * 
   * @return
   */
  public String getFormName();
}
