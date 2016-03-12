package de.micromata.mgc.javafx;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks a FXML field, that it can be converted to the Model by generic methods.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelGuiField {

}
