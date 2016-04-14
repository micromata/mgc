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

package de.micromata.mgc.javafx.launcher.gui;

/**
 * Icons to show in Systemtray.
 * 
 * @author Roger Kommer (roger.kommer.extern@micromata.de)
 * 
 */
public enum SystemtrayIcons
{

  /**
   * The Standard.
   */
  Standard("/style/systray_ok.gif"),

  /**
   * The Error.
   */
  Error("/style/systray_error.gif");

  /**
   * The icon.
   */
  private final String icon;

  /**
   * Instantiates a new systemtray icons.
   * 
   * @param icon the icon
   */
  private SystemtrayIcons(String icon)
  {
    this.icon = icon;
  }

  public String getIcon()
  {
    return icon;
  }
}
