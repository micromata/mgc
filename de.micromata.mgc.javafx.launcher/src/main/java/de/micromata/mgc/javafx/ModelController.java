package de.micromata.mgc.javafx;

import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

/**
 * Controller for handling Models.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 * 
 */
public interface ModelController<M>
{
  default void initWithModel(LocalSettingsConfigModel model)
  {
    initializeWithModel((M) model);
  }

  void initializeWithModel(M model);

  /**
   * Grab the values from the model object and put them into the form.
   * 
   * @param modelObject the model object, normally a pojo.
   */
  void fromModel(M modelObject);

  /**
   * Push all form data to the model object.
   * 
   * @param modelObject the model object, normally a pojo.
   */
  void toModel(final M modelObject);

  /**
   * Reads the validation context and tries to tell the result to components where validation errors happened.
   * 
   * @param ctx the validation context.
   */
  public default void mapValidationMessagesToGui(ValContext ctx)
  {

  }
}
