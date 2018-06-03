package de.fhl.swtlibrary;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import de.fhl.swtlibrary.model.Book;
import de.fhl.swtlibrary.model.Category;
import de.fhl.swtlibrary.model.User;
import de.fhl.swtlibrary.util.Paths;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.query.Result;
import org.jooby.test.JoobyRule;
import org.jooby.test.MockRouter;
import org.junit.*;

import javax.sql.DataSource;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import static io.restassured.RestAssured.form;
import static io.restassured.RestAssured.get;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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

  @Test
  public void testCategoryCount() throws Exception {
    final HtmlPage page = webClient.getPage(HOSTNAME + Paths.BOOK_CATEGORIES);

    EntityStore<Persistable, Category> categoryStore = DatabaseUtil.getData();
    List<Category> categories = categoryStore.select(Category.class)
      .orderBy(Category.NAME.asc())
      .get()
      .toList();

    // Get all Cards which represent a category and all of its books
    List<DomNode> cards = page.querySelectorAll(".card-header-title");

    // Check if category count from database equals category count on the web page
    assertTrue(cards.size() == categories.size());
  }

  @Test
  public void testCategoryPageName() throws Exception {
    final HtmlPage page = webClient.getPage(HOSTNAME + Paths.BOOK_CATEGORIES);

    EntityStore<Persistable, Category> categoryStore = DatabaseUtil.getData();
    List<Category> categories = categoryStore.select(Category.class)
      .orderBy(Category.NAME.asc())
      .get()
      .toList();

    // Get all Cards which represent a category and all of its books
    List<DomNode> cards = page.querySelectorAll(".card-header-title");

    // Check if category 5 from the sorted database list equals category 5 from the web page
    assertTrue(categories.get(5).getName().contains(cards.get(5).getTextContent().trim()));
  }

  @Test
  public void testCategoryPageContent() throws Exception {
    final HtmlPage page = webClient.getPage(HOSTNAME + Paths.BOOK_CATEGORIES);

    EntityStore<Persistable, Category> categoryStore = DatabaseUtil.getData();
    List<Category> categories = categoryStore.select(Category.class)
      .orderBy(Category.NAME.asc())
      .get()
      .toList();

    EntityStore<Persistable, Book> bookStore = DatabaseUtil.getData();
    List<Book> books = bookStore.select(Book.class)
      .where(Book.CATEGORY.eq(categories.get(10)))
      .get()
      .toList();

    // Get all Cards which represent a category and all of its books
    List<DomNode> cards = page.querySelectorAll(".card-header-title");

    // Check if the books from the database show up on the webpage in the right category
    assertTrue(books.stream().map(book -> book.getTitle()).parallel().anyMatch(cards.get(10).getTextContent()::contains));
  }
}
