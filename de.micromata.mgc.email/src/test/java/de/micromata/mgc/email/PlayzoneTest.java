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

package de.micromata.mgc.email;

import java.util.List;

import javax.mail.Flags;
import javax.mail.Provider;
import javax.mail.search.FlagTerm;

import org.junit.Test;

import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.Log4JInitializer;
import de.micromata.genome.util.runtime.StdLocalSettingsLoader;

public class PlayzoneTest
{
  static {
    LocalSettings.localSettingsLoaderFactory = () -> {
      StdLocalSettingsLoader ret = new StdLocalSettingsLoader();
      ret.setLocalSettingsPrefixName("local-settings-dev");
      return ret;
    };
    Log4JInitializer.initializeLog4J();
  }

  //  @Test
  public void testProviders()
  {
    MailReceiveService service = MailReceiverServiceManager.get().getMailReceiveService();
    service.getProviders(Provider.Type.STORE);

  }

  @Test
  public void readMails()
  {
    if (LocalSettings.localSettingsExists() == false) {
      return;
    }
    MailReceiveService service = MailReceiverServiceManager.get().getMailReceiveService();
    FlagTerm term = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
    List<ReceivedMail> messages = service.getNewMessages(term, false);
    messages.size();
    for (ReceivedMail rm : messages) {
      System.out.println("Received: " + rm);
    }
  }
}
