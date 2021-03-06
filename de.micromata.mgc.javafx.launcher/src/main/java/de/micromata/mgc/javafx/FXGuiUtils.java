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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.util.bean.FieldMatchers;
import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.matcher.CommonMatchers;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.javafx.launcher.gui.AbstractModelController;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Helper Methods for GUI Layouting.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class FXGuiUtils
{

  /**
   * Computes the required height of a text for a given font and a wrapping width. Wrapping width defines when a text
   * needs to be wrapped if its {@link Label#wrapTextProperty()} is true.
   * 
   * @param font font of the label.
   * @param text text to render.
   * @param wrappingWidth wrapping width.
   * @return height.
   */
  public static double computeTextHeight(Font font, String text, double wrappingWidth)
  {
    final Text helper = new Text();
    helper.setText(text);
    helper.setFont(font);

    helper.setWrappingWidth((int) wrappingWidth);
    return helper.getLayoutBounds().getHeight();
  }

  /**
   * Computes the required height of the label to fully render its text.
   * 
   * @param width used if the {@link Label#wrapTextProperty()} is true. Then this value is the max line width when the
   *          text needs to be wrapped.
   * @param msg the label.
   * @return required height.
   */
  public static double computeLabelHeight(double width, Label msg)
  {
    final Font font = msg.getFont();
    final String str = msg.getText();
    return computeTextHeight(font, str,
        msg.isWrapText() ? width : 0);
  }

  /**
   * Creates a "safe" CSS Selector from a String. That means replacing all empty spaces with a unqiue value, because
   * spaces in CSS Selectors are interpreted as the beginning of a new selector.
   * 
   * @param value value to convert.
   * @return a safe CSS Selector.
   */
  public static String cssSafeSelectorId(final String value)
  {
    return StringUtils.replace(value, " ", "$BLANK$");
  }

  /**
   * CSS Class for Controls with validation errors.
   */
  private static final String CSS_CLASS_VALID = "formControlVALID";

  /**
   * CSS Class for Controls without validation errors.
   */
  private static final String CSS_CLASS_ERROR = "formControlERROR";

  public static void resetErroneousFields(AbstractModelController<?> controller)
  {
    List<Field> fields = PrivateBeanUtils.findAllFields(controller.getClass(),
        CommonMatchers.and(FieldMatchers.hasNotModifier(Modifier.STATIC), FieldMatchers.assignableTo(Node.class)));
    for (Field field : fields) {
      if (field.getName().equals("thisNode") == true) {
        continue;
      }
      Node node = (Node) PrivateBeanUtils.readField(controller, field);
      FXCssUtil.replaceStyleClass(CSS_CLASS_ERROR, CSS_CLASS_VALID, node);
    }

  }

  public static void markErroneousField(AbstractModelController<?> controller, Node node, ValMessage msg)
  {
    FXCssUtil.replaceStyleClass(CSS_CLASS_VALID, CSS_CLASS_ERROR, node);
  }

}
