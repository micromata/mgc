package de.micromata.mgc.javafx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation for classes that implements {@link PcController} (most likely JavaFX Controls) or plain Java
 * Objects that are used as JavaFX Controllers.
 * 
 * With this annotation you define the fxml file that should be loaded if the annotated class is constructed.
 * 
 * Actually there are two use-cases: 1. this annotation is part of a plain java object used as a JavaFX Controller class
 * 2. this annotation is part of a JavaFX Control.
 * 
 * In case 1 load the controller class with {@link ControllerService#load(Class)} or
 * 
 * 
 * In case 2 load the annotated class with {@link ControllerService#loadControl(PcController)}.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FXMLFile {
  /**
   * s. return statement.
   * 
   * @return fxml file.
   */
  String file();
}
