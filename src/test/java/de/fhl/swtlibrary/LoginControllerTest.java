package de.fhl.swtlibrary;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import de.fhl.swtlibrary.util.Paths;
import org.jooby.test.JoobyRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoginControllerTest {
  @ClassRule
  public static JoobyRule app = new JoobyRule(new App());
  @BeforeClass
  public static void setup() throws Exception{
    TestUtil.setup();
  }

  @Test
  public void testSuccessfulLogin() throws Exception {
    final HtmlPage dashboardPage = TestUtil.loginWithTestUser(1, "Test1234");
    assertEquals(Paths.USER_DASHBOARD, dashboardPage.getBaseURL().getPath());
  }

  @Test
  public void testFailingLogin() throws Exception {
    final HtmlPage loginPage = TestUtil.loginWithTestUser(1, "Test6789");
    assertEquals(Paths.USER_LOGIN, loginPage.getBaseURL().getPath());
  }
}
