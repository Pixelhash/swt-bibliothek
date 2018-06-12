package de.fhl.swtlibrary;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.typesafe.config.Config;
import de.fhl.swtlibrary.model.User;
import de.fhl.swtlibrary.util.Paths;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.test.JoobyRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
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
      final HtmlPage dashboardPage = TestUtil.registerWithTestUser("test@test.com");

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
}
