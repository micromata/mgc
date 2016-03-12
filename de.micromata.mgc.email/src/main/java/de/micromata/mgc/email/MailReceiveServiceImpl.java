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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import javax.mail.Flags;
import javax.mail.MessagingException;

import de.micromata.genome.util.runtime.LocalSettings;

/**
 * Gets the messages from a mail account and assigns them to the MEB user's inboxes.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @author Kai Reinhard (k.reinhard@micromata.de)
 * 
 */
public class MailReceiveServiceImpl implements MailReceiveService
{
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MailReceiveServiceImpl.class);

  static final String DATE_FORMAT = "yyyyMMddHHmmss";

  /**
   * Default configuration supplier loads from local settings.
   */
  Supplier<MailReceiverLocalSettingsConfigModel> configModelSuplier = () -> {
    MailReceiverLocalSettingsConfigModel ret = new MailReceiverLocalSettingsConfigModel();
    ret.fromLocalSettings(LocalSettings.get());
    ret.ensureValide();
    return ret;
  };

  /**
   * 
   * @param onlyRecentMails If true then only unseen mail will be got from the mail server and afterwards they will be
   *          set as seen.
   * @return Number of new imported messages.
   */
  public synchronized List<ReceivedMail> getNewMessages(boolean onlyRecentMails, boolean markRecentMailsAsSeen)
  {
    List<ReceivedMail> mails = new ArrayList<>();
    MailFilter filter = new MailFilter();
    if (onlyRecentMails == true) {
      filter.setOnlyRecent(true);
    }
    MailReceiverLocalSettingsConfigModel cfg = configModelSuplier.get();
    if (cfg == null || cfg.getHostname() == null) {
      // No mail account configured.
      return mails;
    }
    MailAccount mailAccount = new MailAccount(cfg);
    try {
      // If mark messages as seen is set then open mbox read-write.
      mailAccount.connect("INBOX", markRecentMailsAsSeen);
      mails = mailAccount.getMails(filter);
      for (ReceivedMail mail : mails) {
        ReceivedMail entry = new ReceivedMail();
        entry.setDate(mail.getDate());
        String content = mail.getContent();
        BufferedReader reader = new BufferedReader(new StringReader(content.trim()));
        try {
          StringBuffer buf = null;
          while (reader.ready() == true) {
            String line = reader.readLine();
            if (line == null) {
              break;
            }
            if (line.startsWith("date=") == true) {
              if (line.length() > 5) {
                String dateString = line.substring(5);
                Date date = parseDate(dateString);
                entry.setDate(date);
              }
            } else if (line.startsWith("sender=") == true) {
              if (line.length() > 7) {
                String sender = line.substring(7);
                entry.setFrom(sender);
              }
            } else if (line.startsWith("msg=") == true) {
              if (line.length() > 4) {
                String msg = line.substring(4);
                buf = new StringBuffer();
                buf.append(msg);
              }
            } else if (buf != null) {
              buf.append(line);
            } else {
              entry.setFrom(line); // First row is the sender.
              buf = new StringBuffer(); // The message follows.
            }
          }
          if (buf != null) {
            entry.setContent(buf.toString().trim());
          }
        } catch (IOException ex) {
          log.fatal("Exception encountered " + ex, ex);
        }
        mails.add(entry);
        if (markRecentMailsAsSeen == true) {
          try {
            mail.getMessage().setFlag(Flags.Flag.SEEN, true);
            //mail.getMessage().saveChanges();
          } catch (MessagingException ex) {
            log.error("Exception encountered while setting message flag SEEN as true: " + ex, ex);
          }
        }
        // log.info(mail);
      }

      return mails;
    } finally {
      mailAccount.disconnect();
    }
  }

  static Date parseDate(final String dateString)
  {
    Date date = null;
    if (dateString.startsWith("20") == true) {
      final DateFormat df = new SimpleDateFormat(DATE_FORMAT);
      try {
        date = df.parse(dateString);
      } catch (final ParseException ex) {
        log.warn("Servlet call for receiving sms ignored because date string is not parseable (format '"
            + DATE_FORMAT
            + "' expected): "
            + dateString);
        return null;
      }
    } else {
      try {
        final long seconds = Long.parseLong(dateString);
        if (seconds < 1274480916 || seconds > 1999999999) {
          log.warn(
              "Servlet call for receiving sms ignored because date string is not parseable (millis since 01/01/1970 or format '"
                  + DATE_FORMAT
                  + "' expected): "
                  + dateString);
          return null;
        }
        date = new Date(seconds * 1000);
      } catch (final NumberFormatException ex) {
        log.warn("Servlet call for receiving sms ignored because date string is not parseable (format '"
            + DATE_FORMAT
            + "' expected): "
            + dateString);
        return null;
      }
    }
    return date;
  }

}
