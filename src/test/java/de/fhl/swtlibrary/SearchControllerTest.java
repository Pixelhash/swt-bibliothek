package de.fhl.swtlibrary;

import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import de.fhl.swtlibrary.util.Paths;
import org.jooby.test.JoobyRule;
import org.junit.*;

import static org.junit.Assert.assertEquals;

public class SearchControllerTest {
  @ClassRule
  public static JoobyRule app = new JoobyRule(new App());
  @BeforeClass
  public static void setup() throws Exception{
    TestUtil.setup();
  }


  @Test
  public void testBookSearchForm() throws Exception {
    final HtmlPage page = TestUtil.webClient.getPage(TestUtil.HOSTNAME + Paths.BOOK_SEARCH);

    String expected = "Buchsuche | Bibliothek";
    assertEquals(expected, page.getTitleText());
  }

  @Test
  public void testBookSearchInputs() throws Exception {
    final HtmlPage searchPage = TestUtil.webClient.getPage(TestUtil.HOSTNAME + Paths.BOOK_SEARCH);

    final HtmlForm searchForm = searchPage.getFormByName("book_search_form");

    final HtmlButton searchButton = searchForm.getButtonByName("book_search_btn");
    final HtmlTextInput queryField = searchForm.getInputByName("query");

    queryField.type("mathe");

    final HtmlPage resultsPage = searchButton.click();

    String expected = "Suchergebnisse | Bibliothek";
    assertEquals(expected, resultsPage.getTitleText());
  }
}
