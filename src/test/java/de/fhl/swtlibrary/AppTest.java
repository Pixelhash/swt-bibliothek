package de.fhl.swtlibrary;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import de.fhl.swtlibrary.util.Paths;
import org.jooby.test.JoobyRule;
import org.jooby.test.MockRouter;
import org.junit.*;

import javax.sql.DataSource;

import static io.restassured.RestAssured.form;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

/**
 * @author jooby generator
 */
public class AppTest {

  /**
   * One app/server for all the test of this class. If you want to start/stop a new server per test,
   * remove the static modifier and replace the {@link ClassRule} annotation with {@link Rule}.
   */
  @ClassRule
  public static JoobyRule app = new JoobyRule(new App());

  private static final String HOSTNAME = "http://localhost:4567";
  private static WebClient webClient;

  @BeforeClass
  public static void setUp() throws Exception {
    DatabaseUtil.setUpTestDatabase();

    webClient = new WebClient();
    webClient.getOptions().setCssEnabled(false);
    webClient.getOptions().setJavaScriptEnabled(false);
    webClient.getOptions().setThrowExceptionOnScriptError(false);
  }

  @Test
  public void testBookSearchForm() throws Exception {
      final HtmlPage page = webClient.getPage(HOSTNAME + Paths.BOOK_SEARCH);

      String expected = "Buchsuche | Bibliothek";
      assertEquals(expected, page.getTitleText());
  }

  @Test
  public void testBookSearchInputs() throws Exception {
    final HtmlPage page = webClient.getPage(HOSTNAME + Paths.BOOK_SEARCH);

    final HtmlForm searchForm = page.getFormByName("book_search_form");

    final HtmlButton button = searchForm.getButtonByName("book_search_btn");
    final HtmlTextInput queryField = searchForm.getInputByName("query");

    queryField.type("mathe");

    final HtmlPage resultsPage = button.click();

    String expected = "Suchergebnisse | Bibliothek";
    assertEquals(expected, resultsPage.getTitleText());
  }

}
