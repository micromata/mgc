package de.micromata.mgc.javafx.launcher.gui;

import de.micromata.genome.util.i18n.I18NTranslationProvider;
import de.micromata.genome.util.i18n.I18NTranslations;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class DialogBuilder
{
  public static final double DEFAULT_LABEL_WITH = 200;
  public static final double DEFAULT_ROW_HEIGHT = 50;
  protected DialogBuilder parent;
  protected Pane parentPane;
  protected Pane row;

  private I18NTranslationProvider translation = I18NTranslations.noTranslationProvider();

  public DialogBuilder(Pane parentPane)
  {
    this.parentPane = parentPane;
  }

  public DialogBuilder(DialogBuilder parent, Pane parentPane)
  {
    this.parentPane = parentPane;
    this.parent = parent;
    this.translation = parent.getTranslation();
  }

  protected Pane newRow()
  {
    HBox row = new HBox();
    row.setSpacing(10);
    row.setPadding(new Insets(10, 10, 10, 10));

    row.setPrefWidth(Double.MAX_VALUE);
    row.setMaxWidth(Double.MAX_VALUE);
    row.setPrefWidth(Double.MAX_VALUE);
    row.setMaxHeight(Region.USE_PREF_SIZE);
    row.setMinHeight(DEFAULT_ROW_HEIGHT);
    row.setPrefHeight(DEFAULT_ROW_HEIGHT);
    return row;
  }

  protected Pane newCol()
  {
    VBox content = new VBox();
    content.setSpacing(10);
    content.setPadding(new Insets(10, 10, 10, 10));
    content.setMinHeight(DEFAULT_ROW_HEIGHT);
    return content;
  }

  public DialogBuilder addRow()
  {
    row = newRow();
    parentPane.getChildren().add(row);
    return new DialogBuilder(this, row);
  }

  public DialogBuilder addLabeledRow(String labelText)
  {
    row = newRow();
    parentPane.getChildren().add(row);
    Label label = new Label();
    label.setMinWidth(DEFAULT_LABEL_WITH);
    label.setText(translate(labelText));
    row.getChildren().add(label);
    HBox content = new HBox();
    row.getChildren().add(content);
    return new DialogBuilder(this, content);
  }

  public DialogBuilder addTextArea(String id, String text)
  {
    TextArea area = new TextArea();
    area.setMinHeight(300);
    area.setMinWidth(Region.USE_COMPUTED_SIZE);
    area.setMaxWidth(Double.MAX_VALUE);
    area.setId(id);
    area.setText(text);
    parentPane.getChildren().add(area);
    return this;
  }

  public DialogBuilder addTextField(String id, String text)
  {
    TextField textField = new TextField();
    textField.setId(id);
    textField.setText(text);
    parentPane.getChildren().add(textField);
    return this;
  }

  public String translate(String key)
  {
    return getTranslation().translate(key);
  }

  public TextInputControl getTextInputControlById(String id)
  {
    return (TextInputControl) parentPane.lookup("#" + id);
  }

  public I18NTranslationProvider getTranslation()
  {
    return translation;
  }

  public DialogBuilder setTranslation(I18NTranslationProvider translation)
  {
    this.translation = translation;
    return this;
  }

  public Pane getParentPane()
  {
    return parentPane;
  }

  public void setParentPane(Pane parentPane)
  {
    this.parentPane = parentPane;
  }

  public Pane getRow()
  {
    return row;
  }

  public void setRow(Pane row)
  {
    this.row = row;
  }

}
