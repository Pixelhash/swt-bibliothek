package de.fhl.swtlibrary;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class ReminderControllerTest {

  @Test
  public void runTest() throws IOException, EmailException {
    try (SimpleSmtpServer dumbster = SimpleSmtpServer.start(2525)) {

      Email email = new SimpleEmail();

      email.setFrom("library@example.com");
      email.setHostName("localhost");
      email.setSmtpPort(2525);
      email.setSubject("[Bibliothek] Erinnerung an Buchrueckgabe!");
      email.setMsg("Test!");
      email.addTo("test@test.com");
      email.send();

      List<SmtpMessage> emails = dumbster.getReceivedEmails();
      assertThat(emails, hasSize(1));
      SmtpMessage mail = emails.get(0);
      assertThat(mail.getHeaderValue("Subject"), is("[Bibliothek] Erinnerung an Buchrueckgabe!"));
      assertThat(mail.getBody(), is("Test!"));
      assertThat(mail.getHeaderValue("To"), is("test@test.com"));
    }
  }
}
