package de.micromata.mgc.javafx.launcher.gui.generic;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import de.micromata.genome.util.validation.ValContext;

/**
 * Utility to send a test email.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmailSendTester
{
  private String receiver;
  private String sender;
  private ValContext valContext;

  public EmailSendTester(ValContext valContext, String receiver, String sender)
  {
    super();
    this.receiver = receiver;
    this.sender = sender;
    this.valContext = valContext;
  }

  public boolean testSendEmail(String host, String port, boolean withAuth, String user, String password)
  {
    Properties props = System.getProperties();
    String prefix = "mail.smtp";

    props.put(prefix + ".host", host);
    props.put(prefix + ".port", port);

    props.put(prefix + ".auth", Boolean.toString(withAuth));
    Session session;
    if (withAuth == true) {
      props.put(prefix + ".auth", Boolean.toString(withAuth));

      session = Session.getInstance(props, new Authenticator()
      {
        @Override
        protected PasswordAuthentication getPasswordAuthentication()
        {
          return new PasswordAuthentication(user, password);
        }
      });

    } else {
      session = Session.getInstance(props);
    }
    try {
      sendEmail(session);
      valContext.directInfo(null, "Email send sucessfully");
      return true;
    } catch (Exception ex) {
      valContext.directError(null, "Cannot send Email: " + ex.getMessage(), ex);
      return false;
    }
  }

  private void sendEmail(Session session) throws MessagingException
  {
    Message msg = new MimeMessage(session);
    InternetAddress addressFrom = new InternetAddress(sender);
    msg.setFrom(addressFrom);
    InternetAddress addressTo = new InternetAddress(receiver);
    msg.setRecipients(Message.RecipientType.TO, new InternetAddress[] { addressTo });

    msg.setSubject(getSubject());
    msg.setContent(getEmailText(), "text/plain");
    Transport.send(msg);
  }

  public String getEmailText()
  {
    String text = "This is a test email. Please do not reply.";
    return text;
  }

  public String getSubject()
  {
    String text = "Test Email";
    return text;
  }
}
