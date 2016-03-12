package de.micromata.mgc.email;

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
}
