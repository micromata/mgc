
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
