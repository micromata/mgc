package de.micromata.mgc.javafx;

import de.micromata.genome.util.validation.ValContext;
import de.micromata.mgc.javafx.launcher.gui.Controller;

/**
 * Controller for handling Models.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 * 
 */
public interface ModelController<M>extends Controller
{

  void setModel(M model);

  M getModel();

  void initializeWithModel();

  /**
   * Grab the values from the model object and put them into the form.
   * 
   * @param modelObject the model object, normally a pojo.
   */
  void fromModel();

  /**
   * Push all form data to the model object.
   * 
   * @param modelObject the model object, normally a pojo.
   */
  void toModel();

  /**
   * Reads the validation context and tries to tell the result to components where validation errors happened.
   * 
   * @param ctx the validation context.
   */
  public default void mapValidationMessagesToGui(ValContext ctx)
  {

  }

  /**
   * Add input control with message receivers for validation messages.
   */
  void registerValMessageReceivers();
}
