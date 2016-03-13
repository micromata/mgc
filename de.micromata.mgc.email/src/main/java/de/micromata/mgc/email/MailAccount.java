/////////////////////////////////////////////////////////////////////////////
//
// Project ProjectForge Community Edition
//         www.projectforge.org
//
// Copyright (C) 2001-2014 Kai Reinhard (k.reinhard@micromata.de)
//
// ProjectForge is dual-licensed.
//
// This community edition is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License as published
// by the Free Software Foundation; version 3 of the License.
//
// This community edition is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
// Public License for more details.
//
// You should have received a copy of the GNU General Public License along
// with this program; if not, see http://www.gnu.org/licenses/.
//
/////////////////////////////////////////////////////////////////////////////

package de.micromata.mgc.email;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SearchTerm;

import de.micromata.genome.util.runtime.net.EasySSLSocketFactory;

/**
 * Connects to a mail server and receives mails.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @author Kai Reinhard (k.reinhard@micromata.de)
 */
public class MailAccount implements AutoCloseable
{
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MailAccount.class);

  public static String INBOX = "INBOX";

  private Folder folder;

  private Store store;

  private MailReceiverLocalSettingsConfigModel config;
  private Session session;

  public MailAccount(MailReceiverLocalSettingsConfigModel mailAccountConfig)
  {
    this.config = mailAccountConfig;
  }

  /** Gets the stored email of the given user. */
  public ReceivedMail getReceivedMail(int mailId)
  {
    ReceivedMail mail = new ReceivedMail();
    try {
      setEnvelope(mail, folder.getMessage(mailId));
      mail.setContent(getContent(mail.getMessage()));
      disconnect();
    } catch (IndexOutOfBoundsException ex) {
      log.warn("Message number out of range: " + mailId);
    } catch (MessagingException ex) {
      log.warn("", ex);
    } catch (IOException ex) {
      log.warn("", ex);
    }
    return mail;
  }

  /**
   * Gets a list of all Emails matching the given filter.
   * 
   * @return ArrayList of all found Email.
   */
  public List<ReceivedMail> getMails(SearchTerm searchTerm)
  {
    List<ReceivedMail> table = new ArrayList<ReceivedMail>();
    if (folder == null || folder.isOpen() == false) {
      log.error("Folder is not opened, can't get mails: "
          + this.config.getUser()
          + "@"
          + this.config.getHost()
          + " via "
          + this.config.getProtocol());
      return table;
    }

    try {
      int totalMessages = folder.getMessageCount();
      log.debug("New messages: " + folder.getNewMessageCount());
      log.debug("Total messages: " + totalMessages);
      if (totalMessages == 0) {
        return table;
      }
      // Attributes & Flags for all messages ..
      Message[] msgs;
      if (searchTerm == null) {
        msgs = folder.getMessages();
      } else {
        msgs = folder.search(searchTerm);
      }
      // Use a suitable FetchProfile
      FetchProfile fp = new FetchProfile();
      fp.add(FetchProfile.Item.ENVELOPE);
      fp.add(FetchProfile.Item.FLAGS);
      fp.add("X-ReceivedMailer");
      folder.fetch(msgs, fp);

      for (int i = 0; i < msgs.length; i++) {
        ReceivedMail mail = new ReceivedMail();
        setEnvelope(mail, msgs[i]);
        mail.setContent(getContent(mail.getMessage()));
        // if (filter == null || (mail.isRecent() == true && filter.isRecent() == true)
        // || (mail.isSeen() == true && filter.isSeen() == true)
        // || (mail.isDeleted() == true && filter.isDeleted() == true)) {
        table.add(mail);
        // }
      }
      // No sort the table by date:

      Collections.sort(table, (first, second) -> {
        if (first.messageNumber < second.messageNumber) {
          return -1;
        } else if (first.messageNumber == second.messageNumber) {
          return 0;
        } else {
          return 1;
        }
      });
      return table;
    } catch (javax.mail.MessagingException ex) {
      log.info(ex.getMessage(), ex);
      throw new RuntimeException(ex);
    } catch (IOException ex) {
      log.info(ex.getMessage(), ex);
      throw new RuntimeException(ex);
    }
  }

  public Session createSession()
  {
    // Get a Properties object
    Properties props = new Properties();
    // TODO from config

    String protocol = config.getProtocol();

    props.put("mail." + protocol + ".host", config.getHost());
    props.put("mail." + protocol + ".port", config.getPort());
    props.put("mail." + protocol + ".user", config.getUser());
    props.put("mail." + protocol + ".password", config.getPassword());
    props.put("mail." + protocol + ".auth", config.getAuth());

    props.put("mail." + protocol + ".starttls.enable", config.getEnableTLS());

    props.put("mail.debug", config.getDebug());
    props.put("mail." + protocol + ".debug", config.getDebug());
    // hmm, may be not correct
    boolean self = config.isEnableSelfSignedCerts();
    if (self == true) {
      props
          .put("mail." + protocol + ".socketFactory.class",
              EasySSLSocketFactory.class.getName());
      props.setProperty("mail." + protocol + ".socketFactory.port", "993");
      props.setProperty("mail." + protocol + ".socketFactory.fallback", "false");
      props.setProperty("mail." + protocol + ".auth.plain.disable", "true");

    }
    session = Session.getInstance(props,
        new javax.mail.Authenticator()
        {
          @Override
          protected PasswordAuthentication getPasswordAuthentication()
          {
            return new PasswordAuthentication(config.getUser(), config.getPassword());
          }
        });
    return session;
  }

  List<String> testConnect() throws MessagingException
  {
    session = createSession();
    store = session.getStore(config.getProtocol());
    store.connect();
    folder = store.getDefaultFolder();
    String defaultName = folder.getFullName();
    List<String> ret = new ArrayList<>();
    ret.add(defaultName);
    Folder[] sfl = folder.list("*");
    for (Folder sf : sfl) {
      ret.add(sf.getFullName());
    }
    return ret;
  }

  /**
   * Opens the connection to the mailserver. Don't forget to call disconnect if this method returns true!
   * 
   * @param mbox The folder name to open. If null then the default folder will be opened.
   * @param readwrite If false then the mbox is connected in readonly mode.
   * @return true on success, otherwise false.
   */
  public boolean connect(String mbox, boolean readwrite)
  {
    try {

      session = createSession();

      // Get a Store object
      store = null;
      try {
        store = session.getStore(config.getProtocol());
      } catch (javax.mail.NoSuchProviderException ex) {
        log.error(ex.getMessage(), ex);
        // serverData.setErrorMessageKey("mail.error.noSuchProviderException");
        return false;
      }
      store.connect();
      // Open the Folder

      folder = store.getDefaultFolder();
      if (folder == null) {
        // serverData.setErrorMessageKey("mail.error.noDefaultFolder");
        return false;
      }

      if (mbox != null) {
        folder = folder.getFolder(mbox);
        if (folder == null) {
          // serverData.setErrorMessageKey("mail.error.invalidFolder");
          return false;
        }
      }
      if (readwrite == true) {
        // try to open read/write and if that fails try read-only
        try {
          folder.open(Folder.READ_WRITE);
        } catch (MessagingException ex) {
          log.error(
              "Can't open mbox in read-write mode, try to open folder in read-only mode instead: " + ex.getMessage());
          folder.open(Folder.READ_ONLY);
        }
      } else {
        folder.open(Folder.READ_ONLY);
      }
    } catch (javax.mail.MessagingException ex) {
      // serverData.setErrorMessageKey("mail.error.messagingException");
      // serverData.setOriginalErrorMessage(ex.getMessage());
      log.info(ex.getMessage(), ex);
      return false;
    }
    return true;
  }

  @Override
  public void close()
  {
    disconnect();
  }

  /**
   * Disconnects the folder and store if given and is opened yet.
   * 
   * @return
   */
  public boolean disconnect()
  {
    boolean success = true;
    if (folder != null && folder.isOpen() == true) {
      try {
        folder.close(false);
      } catch (MessagingException ex) {
        log.error("Exception encountered while trying tho close the folder: " + ex, ex);
        success = false;
      }
    }
    if (store != null && store.isConnected() == true) {
      try {
        store.close();
      } catch (MessagingException ex) {
        log.error("Exception encountered while trying to close the store: " + ex, ex);
        success = false;
      }
    }
    return success;
  }

  protected void setEnvelope(ReceivedMail mail, Message message) throws javax.mail.MessagingException
  {
    mail.setMessage(message);
    Address[] addr;
    // ID
    mail.setMessageNumber(message.getMessageNumber());

    // FROM
    StringBuffer buf = new StringBuffer();
    addr = message.getFrom();
    if (addr != null) {
      for (int j = 0; j < addr.length; j++) {
        if (j > 0) {
          buf.append(",");
        }
        buf.append(addr[j].toString());
      }
    }
    mail.setFrom(buf.toString());

    // TO
    addr = message.getRecipients(Message.RecipientType.TO);
    buf = new StringBuffer();
    if (addr != null) {
      for (int j = 0; j < addr.length; j++) {
        if (j > 0) {
          buf.append(",");
        }
        buf.append(addr[j].toString());
      }
    }
    mail.setTo(buf.toString());

    // SUBJECT
    mail.setSubject(message.getSubject());

    // DATE
    Date date = message.getSentDate();
    if (date != null) {
      mail.setDate(date);
    } else { // Needed for compareTo (assume 1.1.1970)
      mail.setDate(new Date(0));
    } // FLAGS
    Flags flags = message.getFlags();
    Flags.Flag[] systemFlags = flags.getSystemFlags(); // get the system flags

    for (int i = 0; i < systemFlags.length; i++) {
      Flags.Flag flag = systemFlags[i];
      if (flag == Flags.Flag.ANSWERED) {
        // Ignore this flag
      } else if (flag == Flags.Flag.DELETED) {
        mail.setDeleted(true);
      } else if (flag == Flags.Flag.DRAFT) {
        // Ignore this flag
      } else if (flag == Flags.Flag.FLAGGED) {
        // Ignore this flag
      } else if (flag == Flags.Flag.RECENT) {
        mail.setRecent(true);
      } else if (flag == Flags.Flag.SEEN) {
        mail.setSeen(true);
      } else {
        // skip it
      }
    }
  }

  private String getContent(Part msg) throws MessagingException, IOException
  {
    StringBuffer buf = new StringBuffer();
    getContent(msg, buf);
    return buf.toString();
  }

  private void getContent(Part msg, StringBuffer buf) throws MessagingException, IOException
  {
    if (log.isDebugEnabled() == true) {
      log.debug("CONTENT-TYPE: " + msg.getContentType());
    }
    String filename = msg.getFileName();
    if (filename != null) {
      log.debug("FILENAME: " + filename);
    }
    // Using isMimeType to determine the content type avoids
    // fetching the actual content data until we need it.
    if (msg.isMimeType("text/plain")) {
      log.debug("This is plain text");
      try {
        buf.append(msg.getContent());
      } catch (UnsupportedEncodingException ex) {
        buf.append("Unsupported charset by java mail, sorry: " + "CONTENT-TYPE=[" + msg.getContentType() + "]");
      }
    } else if (msg.isMimeType("text/html")) {
      log.debug("This is html text");
      buf.append(msg.getContent());
    } else if (msg.isMimeType("multipart/*")) {
      log.debug("This is a Multipart");
      Multipart multiPart = (Multipart) msg.getContent();
      int count = multiPart.getCount();
      for (int i = 0; i < count; i++) {
        if (i > 0) {
          buf.append("\n----------\n");
        }
        getContent(multiPart.getBodyPart(i), buf);
      }
    } else if (msg.isMimeType("message/rfc822")) {
      log.debug("This is a Nested Message");
      buf.append(msg.getContent());
    } else {
      log.debug("This is an unknown type");
      // If we actually want to see the data, and it's not a
      // MIME type we know, fetch it and check its Java type.
      Object obj = msg.getContent();
      if (obj instanceof String) {
        buf.append(obj);
      } else if (obj instanceof InputStream) {
        log.debug("Inputstream");
        buf.append("Attachement: ");
        if (filename != null) {
          buf.append(filename);
        } else {
          buf.append("Unsupported format (not a file).");
        }
      } else {
        log.error("Should not occur");
        buf.append("Unsupported type");
      }
    }
  }

  public Session getSession()
  {
    return session;
  }

}
