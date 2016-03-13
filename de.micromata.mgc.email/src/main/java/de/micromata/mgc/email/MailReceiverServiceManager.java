package de.micromata.mgc.email;

/**
 * Service manager to receive emails.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MailReceiverServiceManager
{
  private static MailReceiverServiceManager INSTANCE = new MailReceiverServiceManager();

  private MailReceiveService mailReceiveService = new MailReceiveServiceImpl();

  public static MailReceiverServiceManager get()
  {
    return INSTANCE;
  }

  public MailReceiveService getMailReceiveService()
  {
    return mailReceiveService;
  }

  public void setMailReceiveService(MailReceiveService mailReceiveService)
  {
    this.mailReceiveService = mailReceiveService;
  }

}
