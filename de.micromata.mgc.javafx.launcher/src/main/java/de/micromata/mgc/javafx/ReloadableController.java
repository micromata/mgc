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

package de.micromata.mgc.javafx;

/**
 * Controllers that implements this interface are interested in being reloaded. Reloading is done by
 * {@link ControllerService#reloadStage(Class, boolean)}.
 * 
 * Reloading resets the state of the view. If you like to get the same view, provide a model object with method
 * {@link #getModel()}.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 * @param <M> type of the model object.
 */
public interface ReloadableController<M>
{
  /**
   * Model object that stores the state of the view (form controls etc.)
   * 
   * Used by the reloading process to restore the view. If you return null here, the view is reseted to its initial
   * state.
   * 
   * @return model object.
   */
  M getModel();

  /**
   * Callback Method. Do additional work here before the view is reloaded.
   */
  void onBeforeReload();
}
