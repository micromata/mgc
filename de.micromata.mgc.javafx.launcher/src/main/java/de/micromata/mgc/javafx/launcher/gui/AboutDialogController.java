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

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.application.MgcApplication;
import de.micromata.mgc.application.MgcApplicationInfo;
import de.micromata.mgc.javafx.SystemService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class AboutDialogController extends AbstractModelController<MgcApplication<?>>
{
  @FXML
  private ImageView logo;
  @FXML
  private Pane appInfo;
  @FXML
  private Pane appDetails;
  @FXML
  private Button okButton;

  @Override
  public void initializeWithModel()
  {
    MgcApplicationInfo ai = model.getApplicationInfo();
    okButton.setOnAction(event -> getStage().close());

    String name = ai.getName() + " " + ai.getVersion();
    Text text1 = new Text(name);
    text1.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
    Text text2 = new Text("\n\n" + ai.getCopyright() + "\n");
    TextFlow apptext = new TextFlow(text1, text2);
    appInfo.getChildren().add(apptext);

    if (ai.getLogoLargePath() != null) {
      logo.setImage(new Image(this.getClass().getResource(ai.getLogoLargePath()).toString()));
    }

    String sdetailText = ai.getDetailInfo();
    if (StringUtils.isNotBlank(ai.getLicense()) == true) {
      sdetailText += "\n\nLicense: " + ai.getLicense();
    }
    TextFlow detailText = new TextFlow();
    detailText.getChildren().add(new Text(sdetailText));

    if (StringUtils.isNotBlank(ai.getHomeUrl()) == true) {
      detailText.getChildren().add(new Text("\n\nHomepage: "));
      Hyperlink hlink = new Hyperlink(ai.getHomeUrl());
      hlink.setOnAction(event -> SystemService.get().openUrlInBrowser(ai.getHomeUrl()));
      detailText.getChildren().add(hlink);
    }
    appDetails.getChildren().add(detailText);

  }

  @Override
  public void fromModel()
  {

  }

  @Override
  public void toModel()
  {

  }

  @Override
  public void addToFeedback(ValMessage msg)
  {

  }

}
