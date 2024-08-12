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

import jakarta.mail.Provider;
import jakarta.mail.search.SearchTerm;

import de.micromata.genome.util.validation.ValContext;

/**
 * The Interface ReceiveMailService.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface MailReceiveService
{
  public interface MailReceiveCallback
  {
    void receivedEmails(ReceivedMail mailReceiver);
  }

  List<String> getProviders(Provider.Type type);

  List<ReceivedMail> getNewMessages(SearchTerm searchTerm, boolean markRecentMailsAsSeen);

  /**
   * 
   * @param config the mail config
   * @param ctx the validation context
   * @return the list of boxes.
   */
  List<String> testConnection(MailReceiverLocalSettingsConfigModel config, ValContext ctx);
}
