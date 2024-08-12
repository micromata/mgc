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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import jakarta.mail.Flags;
import jakarta.mail.MessagingException;
import jakarta.mail.Provider;
import jakarta.mail.Session;
import jakarta.mail.search.SearchTerm;

import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.validation.ValContext;

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

    return ret;
  };

  @Override
  public List<String> getProviders(Provider.Type type)
  {
    MailReceiverLocalSettingsConfigModel cfg = configModelSuplier.get();
    try (MailAccount mailAccount = new MailAccount(cfg)) {
      Session session = mailAccount.createSession();
      Provider[] providers = mailAccount.getSession().getProviders();
      List<String> ret = new ArrayList<>();
      for (int i = 0; i < providers.length; ++i) {
        Provider pr = providers[i];
        if (pr.getType() == type) {
          ret.add(pr.getProtocol());
        }
      }
      return ret;
    }
  }

  /**
   * 
   * @param searchTerm the term to search for
   * @param markRecentMailsAsSeen mark the mail as seen
   * @return Number of new imported messages.
   */
  @Override
  public synchronized List<ReceivedMail> getNewMessages(SearchTerm searchTerm, boolean markRecentMailsAsSeen)
  {
    List<ReceivedMail> mails = new ArrayList<>();

    MailReceiverLocalSettingsConfigModel cfg = configModelSuplier.get();
    if (cfg == null || cfg.getHost() == null) {
      // No mail account configured.
      return mails;
    }
    // TODO RK check valid
    List<ReceivedMail> allMails = new ArrayList<>(); 
    try (MailAccount mailAccount = new MailAccount(cfg)) {
      // If mark messages as seen is set then open mbox read-write.
      boolean connected = mailAccount.connect("INBOX", markRecentMailsAsSeen);
      mails = mailAccount.getMails(searchTerm);
      
      for (ReceivedMail mail : mails) {
//        allMails.add(mail);
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
        allMails.add(entry);
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

      return allMails;
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

  @Override
  public List<String> testConnection(MailReceiverLocalSettingsConfigModel config, ValContext ctx)
  {
    MailAccount mailAccount = new MailAccount(config);
    try {
      return mailAccount.testConnect();

    } catch (MessagingException e) {
      ctx.directError("", e.getMessage(), e);
      return Collections.emptyList();
    }
  }
}
