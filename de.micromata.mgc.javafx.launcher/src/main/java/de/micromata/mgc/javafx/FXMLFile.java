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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation for classes that implements most likely JavaFX Controls or plain Java
 * Objects that are used as JavaFX Controllers.
 * 
 * With this annotation you define the fxml file that should be loaded if the annotated class is constructed.
 * 
 * Actually there are two use-cases: 1. this annotation is part of a plain java object used as a JavaFX Controller class
 * 2. this annotation is part of a JavaFX Control.
 * 
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
