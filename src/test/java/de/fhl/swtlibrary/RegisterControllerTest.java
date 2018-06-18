package de.fhl.swtlibrary;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import de.fhl.swtlibrary.model.User;
import de.fhl.swtlibrary.util.Paths;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.test.JoobyRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RegisterControllerTest {
  @ClassRule
  public static JoobyRule app = new JoobyRule(new App());
  @BeforeClass
  public static void setup() throws Exception{
    TestUtil.setup();
  }


  @Test
  public void testSuccessfulRegistration() throws Exception {
    try (SimpleSmtpServer dumbster = SimpleSmtpServer.start(2525)) {
      final HtmlPage dashboardPage = TestUtil.registerWithTestUser("moin@test.de");
      assertEquals(Paths.USER_REGISTER, dashboardPage.getBaseURL().getPath());
    }
  }

  @Test
  public void testSuccessfulUser() throws Exception {
    try (SimpleSmtpServer dumbster = SimpleSmtpServer.start(2525)) {
      TestUtil.registerWithTestUser("test@test.com");

      List<SmtpMessage> emails = dumbster.getReceivedEmails();
      SmtpMessage mail = emails.get(0);

      String text = mail.getBody().trim();
      int startIndex = text.indexOf("lautet:");
      String userid = text.substring(startIndex + 8, text.indexOf("Hinweis:")).trim();

      EntityStore<Persistable, User> userStore = DatabaseUtil.getData();
      User user = userStore.select(User.class)
        .where(User.ID.eq(Integer.parseInt(userid)))
        .get()
        .firstOrNull();

      assertTrue(user.getEmail().equals("test@test.com"));
    }
  }

  @Test
  public void testRecieveEmail() throws Exception {
    try (SimpleSmtpServer dumbster = SimpleSmtpServer.start(2525)) {
      TestUtil.registerWithTestUser("test@test.de");

      List<SmtpMessage> emails = dumbster.getReceivedEmails();
      SmtpMessage mail = emails.get(0);

      assertTrue(mail != null);
    }
  }

  @Test
  public void testUnactivatedUser() throws Exception {
    try (SimpleSmtpServer dumbster = SimpleSmtpServer.start(2525)) {
      TestUtil.registerWithTestUser("test@test123.com");

      EntityStore<Persistable, User> userStore = DatabaseUtil.getData();
      User user = userStore.select(User.class)
        .orderBy(User.ID.desc())
        .get()
        .firstOrNull();

      assertTrue(user.getActivationToken() != null);
    }
  }

  @Test
  public void testActivatedUser() throws Exception {
    try (SimpleSmtpServer dumbster = SimpleSmtpServer.start(2525)) {
      TestUtil.registerWithTestUser("test123@test123.com");

      List<SmtpMessage> emails = dumbster.getReceivedEmails();
      SmtpMessage mail = emails.get(0);

      EntityStore<Persistable, User> userStore = DatabaseUtil.getData();
      User user = userStore.select(User.class)
        .orderBy(User.ID.desc())
        .get()
        .firstOrNull();

      String mailText = mail.getBody();
      int index = mailText.indexOf("timestamp") + 12;
      int domainIndexBeginning = mailText.indexOf("http");
      int domainIndexEnding = mailText.indexOf("/user");

      String host = mailText.substring(domainIndexBeginning, domainIndexEnding);
      String timeStamp = mailText.substring(index, index + 14);
      String activationToken = user.getActivationToken();

      URL url = new URL(host + "/user/activate?activation_token=" + activationToken + "?timestamp=" + timeStamp);

      TestUtil.webClient.getPage(url);

      userStore.refresh(user);

      assertNull(user.getActivationToken());
    }
  }
}
