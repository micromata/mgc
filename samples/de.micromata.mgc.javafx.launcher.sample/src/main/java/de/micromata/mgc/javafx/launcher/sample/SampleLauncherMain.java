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

package de.micromata.mgc.javafx.launcher.sample;

import de.micromata.mgc.javafx.launcher.MgcLauncher;
import de.micromata.mgc.javafx.launcher.gui.generic.GenericMainWindow;

/**
 * Sample Main class to start the application.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SampleLauncherMain extends MgcLauncher<SampleLocalSettingsConfigModel>
{
  /**
   * Launch via standard java main.
   * 
   * @param args passed from command line.
   */
  public static void main(String[] args)
  {
    SampleLauncherMain el = new SampleLauncherMain();
    el.launch(args);
  }

  public SampleLauncherMain()
  {
    // configure the Launcher with application and the main JavaFX window.
    super(new SampleLauncherApplication(), (Class) GenericMainWindow.class);
  }
}
