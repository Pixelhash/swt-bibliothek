package de.fhl.swtlibrary;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import de.fhl.swtlibrary.model.User;
import de.fhl.swtlibrary.util.Paths;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.test.JoobyRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

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
    final HtmlPage dashboardPage = TestUtil.registerWithTestUser("moin@test.de");
    assertEquals(Paths.USER_REGISTER, dashboardPage.getBaseURL().getPath());
  }

  @Test
  public void testSuccessfulUser() throws Exception {
    final HtmlPage dashboardPage = TestUtil.registerWithTestUser("test@test.com");

    String text = dashboardPage.getBody().querySelector(".is-success").getTextContent().trim();
    int startIndex = text.indexOf("ID:");
    String userid = text.substring(startIndex + 4).trim();

    EntityStore<Persistable, User> userStore = DatabaseUtil.getData();
    User user = userStore.select(User.class)
      .where(User.ID.eq(Integer.parseInt(userid)))
      .get()
      .firstOrNull();

    assertTrue(user.getEmail().equals("test@test.com"));
  }
}
