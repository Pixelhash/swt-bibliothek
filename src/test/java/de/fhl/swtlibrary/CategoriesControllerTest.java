package de.fhl.swtlibrary;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import de.fhl.swtlibrary.model.Book;
import de.fhl.swtlibrary.model.Category;
import de.fhl.swtlibrary.util.Paths;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.test.JoobyRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class CategoriesControllerTest {
  @ClassRule
  public static JoobyRule app = new JoobyRule(new App());
  @BeforeClass
  public static void setup() throws Exception{
    TestUtil.setup();
  }

  @Test
  public void testCategoryCount() throws Exception {
    final HtmlPage page = TestUtil.webClient.getPage(TestUtil.HOSTNAME + Paths.BOOK_CATEGORIES);

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
    final HtmlPage page = TestUtil.webClient.getPage(TestUtil.HOSTNAME + Paths.BOOK_CATEGORIES);

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
    final HtmlPage page = TestUtil.webClient.getPage(TestUtil.HOSTNAME + Paths.BOOK_CATEGORIES);

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
