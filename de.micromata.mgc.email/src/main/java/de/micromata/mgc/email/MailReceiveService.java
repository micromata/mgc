package de.micromata.mgc.email;

import java.util.List;

import javax.mail.Provider;
import javax.mail.search.SearchTerm;

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
   * @param config
   * @param ctx
   * @return the list of boxes.
   */
  List<String> testConnection(MailReceiverLocalSettingsConfigModel config, ValContext ctx);
}
