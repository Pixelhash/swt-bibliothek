package de.fhl.swtlibrary;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.gargoylesoftware.htmlunit.html.*;
import de.fhl.swtlibrary.model.User;
import de.fhl.swtlibrary.util.Paths;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.test.JoobyRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PasswordResetControllerTest {
  @ClassRule
  public static JoobyRule app = new JoobyRule(new App());
  @BeforeClass
  public static void setup() throws Exception{
    TestUtil.setup();
  }


  @Test
  public void testPasswordResetRequestSuccess() throws Exception {
    EntityStore<Persistable, User> userEntityStore = DatabaseUtil.getData();
    User user = userEntityStore.select(User.class)
      .where(User.ID.eq(1))
      .get()
      .firstOrNull();
    assertNull(user.getPasswordResetToken());
    String oldPassword = user.getPassword();

    try (SimpleSmtpServer dumbster = SimpleSmtpServer.start(2525)) {
      final HtmlPage loginPage = TestUtil.webClient.getPage(TestUtil.HOSTNAME + Paths.USER_LOGIN);
      final HtmlForm passwordResetForm = loginPage.getFormByName("password_reset_form");
      final HtmlInput emailField = passwordResetForm.getInputByName("email");
      final HtmlButton submitButton = loginPage.getElementByName("password_reset_btn");

      emailField.type("peter.meyer@gmail.com");
      final HtmlPage resultPage = submitButton.click();
      assertEquals(Paths.USER_LOGIN, resultPage.getBaseURL().getPath());

      userEntityStore.refresh(user);
      assertNotNull(user.getPasswordResetToken());

      List<SmtpMessage> mails = dumbster.getReceivedEmails();
      assertEquals(1, mails.size());

      SmtpMessage mail = dumbster.getReceivedEmails().get(0);

      assertEquals("peter.meyer@gmail.com", mail.getHeaderValue("To"));

      String[] lines = mail.getBody().split("\n");
      String resetLink = Arrays.stream(lines).filter(l -> l.startsWith("http://")).findFirst().get();

      final HtmlPage resetPage = TestUtil.webClient.getPage(resetLink.replaceAll("=", ""));
      final HtmlForm resetForm = resetPage.getFormByName("password_reset_new_form");
      final HtmlPasswordInput passwordField = resetForm.getInputByName("password");
      final HtmlPasswordInput confirmPasswordInput = resetForm.getInputByName("confirm_password");
      final HtmlButton resetButton = resetForm.getButtonByName("password_reset_btn");

      passwordField.type("Hello1234");
      confirmPasswordInput.type("Hello1234");

      final HtmlPage newLoginPage = resetButton.click();
      assertEquals(Paths.USER_LOGIN, newLoginPage.getBaseURL().getPath());

      userEntityStore.refresh(user);
      assertNotEquals(oldPassword, user.getPassword());
      assertNull(user.getPasswordResetToken());

      final HtmlPage dashboardPage = TestUtil.loginWithTestUser(1, "Hello1234");
      assertEquals(Paths.USER_DASHBOARD, dashboardPage.getBaseURL().getPath());
    }

  }

}
