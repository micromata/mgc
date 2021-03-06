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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.apache.log4j.Logger;

import de.micromata.genome.util.bean.FieldMatchers;
import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.matcher.CommonMatchers;
import de.micromata.genome.util.runtime.GenericsUtils;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.javafx.FXEvents;
import de.micromata.mgc.javafx.ModelController;
import de.micromata.mgc.javafx.ModelGuiField;
import de.micromata.mgc.javafx.feedback.FeedbackPanel;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class AbstractModelController<M>extends AbstractController implements ModelController<M>
{
  private static final Logger LOG = Logger.getLogger(AbstractModelController.class);

  protected M model;

  protected void registerValMessageReceiver(Node node, FeedbackPanel feedback, Class<?> referenceType, String property)
  {
    FXEvents.get().registerValMessageReceiver(this, node, referenceType, property);
  }

  public void addToFeedback(ValMessage msg)
  {
    LOG.warn("No feedbackPanel defined in " + getClass().getSimpleName() + "; " + msg.getI18nkey());
  }

  protected void registerValMessage(String fieldName)
  {
    Class<?> type = GenericsUtils.getClassGenericTypeFromSuperClass(getClass(), 0, LocalSettingsConfigModel.class);
    Node node = (Node) PrivateBeanUtils.readField(this, fieldName);
    FXEvents.get().registerValMessageReceiver(this, node, getModel(), fieldName);
  }

  @Override
  public void registerValMessageReceivers()
  {
    Class<?> type = GenericsUtils.getClassGenericTypeFromSuperClass(getClass(), 0, LocalSettingsConfigModel.class);
    List<Field> fields = PrivateBeanUtils.findAllFields(getClass(),
        CommonMatchers.and(FieldMatchers.hasNotModifier(Modifier.STATIC), FieldMatchers.assignableTo(Control.class)));
    for (Field field : fields) {
      Control ctl = (Control) PrivateBeanUtils.readField(this, field);
      FXEvents.get().registerValMessageReceiver(this, ctl, getModel(), field.getName());
    }

  }

  /**
   * Calls fromModel by default
   * 
   * {@inheritDoc}
   *
   */
  @Override
  public void initializeWithModel()
  {
    fromModel();
  }

  @Override
  public void fromModel()
  {
    List<Field> fields = PrivateBeanUtils.findAllFields(getClass(), FieldMatchers.hasAnnotation(ModelGuiField.class));
    for (Field field : fields) {
      copyFieldFromModel(field.getName());
    }
  }

  protected void copyFromModel(String... names)
  {
    for (String name : names) {
      copyFieldFromModel(name);
    }
  }

  protected void copyToModel(String... names)
  {
    for (String name : names) {
      copyFieldToModel(name);
    }
  }

  protected void copyFieldFromModel(String name)
  {
    M model = getModel();
    Object guifield = PrivateBeanUtils.readField(this, name);

    String modelField = (String) PrivateBeanUtils.readField(model, name);
    if (guifield == null) {
      throw new IllegalArgumentException("Cannot find field in controller: " + name);
    }
    if (modelField == null) {
      throw new IllegalArgumentException("Cannot find field in model: " + name);
    }

    if (guifield instanceof TextField) {
      ((TextField) guifield).setText(modelField);
    } else if (guifield instanceof CheckBox) {
      ((CheckBox) guifield).setSelected("true".equals(modelField));
    } else if (guifield instanceof ChoiceBox) {
      ((ChoiceBox) guifield).setValue(modelField);
    } else if (guifield instanceof ComboBox) {
      ((ComboBox) guifield).setValue(modelField);
    } else {
      throw new IllegalArgumentException("Cannot convert gui type to model: " + modelField.getClass().getName());
    }

  }

  protected void copyFieldToModel(String name)
  {
    M model = getModel();
    Object guifield = PrivateBeanUtils.readField(this, name);

    Field modelField = PrivateBeanUtils.findField(model, name);
    if (guifield == null) {
      throw new IllegalArgumentException("Cannot find field in controller: " + name);
    }
    if (modelField == null) {
      throw new IllegalArgumentException("Cannot find field in model: " + name);
    }

    if (guifield instanceof TextField) {
      String text = ((TextField) guifield).getText();
      PrivateBeanUtils.writeField(model, modelField, text);
    } else if (guifield instanceof CheckBox) {
      boolean selected = ((CheckBox) guifield).isSelected();
      PrivateBeanUtils.writeField(model, modelField, Boolean.toString(selected));
    } else if (guifield instanceof ChoiceBox) {
      PrivateBeanUtils.writeField(model, modelField, ((ChoiceBox) guifield).getValue());
    } else if (guifield instanceof ComboBox) {
      PrivateBeanUtils.writeField(model, modelField, ((ComboBox) guifield).getValue());
    } else {
      throw new IllegalArgumentException("Cannot convert gui type to model: " + modelField.getClass().getName());
    }

  }

  @Override
  public void toModel()
  {
    List<Field> fields = PrivateBeanUtils.findAllFields(getClass(), FieldMatchers.hasAnnotation(ModelGuiField.class));
    for (Field field : fields) {
      copyFieldToModel(field.getName());
    }
  }

  @Override
  public M getModel()
  {
    return model;
  }

  @Override
  public void setModel(M model)
  {
    this.model = model;
  }

}
