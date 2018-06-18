package de.fhl.swtlibrary;

import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import de.fhl.swtlibrary.util.Paths;
import org.jooby.test.JoobyRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.*;

public class LogoutControllerTest {
  @ClassRule
  public static JoobyRule app = new JoobyRule(new App());
  @BeforeClass
  public static void setup() throws Exception{
    TestUtil.setup();
  }

  @Test
  public void testLogout() throws Exception {
    // First login
    final HtmlPage dashboardPage = TestUtil.loginWithTestUser(2, "Test1234");
    // Now check if dashboard is reachable
    assertEquals(Paths.USER_DASHBOARD, dashboardPage.getBaseURL().getPath());

    // Get logout form
    final HtmlForm logoutForm = dashboardPage.getFormByName("logout_form");
    // Get submit button
    final HtmlButton submitButton = logoutForm.getButtonByName("logout_btn");
    // Now logout
    final HtmlPage loginPage = submitButton.click();
    // Check if redirect page is login page
    assertEquals(Paths.USER_LOGIN, loginPage.getBaseURL().getPath());

    // Finally try again to reach the dashboard page
    final HtmlPage redirLoginPage = TestUtil.webClient.getPage(TestUtil.HOSTNAME + Paths.USER_DASHBOARD);
    assertEquals(Paths.USER_LOGIN, redirLoginPage.getBaseURL().getPath());
  }
}
