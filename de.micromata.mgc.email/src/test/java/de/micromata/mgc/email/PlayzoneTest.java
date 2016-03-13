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
