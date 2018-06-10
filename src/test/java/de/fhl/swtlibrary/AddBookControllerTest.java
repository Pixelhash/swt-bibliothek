package de.fhl.swtlibrary;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.gargoylesoftware.htmlunit.html.*;
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

import static org.junit.Assert.*;

public class AddBookControllerTest {
  @ClassRule
  public static JoobyRule app = new JoobyRule(new App());
  @BeforeClass
  public static void setup() throws Exception{
    TestUtil.setup();
  }

  @Test
  public void testAddBookButtonFromDashboard() throws Exception {
    final HtmlPage dashboardPage = TestUtil.loginWithTestUser(1, "Test1234");
    final HtmlAnchor anchor = dashboardPage.getAnchorByHref(Paths.BOOK_ADD);

    final HtmlPage bookAddPage = anchor.click();

    assertEquals(Paths.BOOK_ADD, bookAddPage.getBaseURL().getPath());
  }

  @Test
  public void testAddBookSuccessful() throws Exception {
    final HtmlPage dashboardPage = TestUtil.loginWithTestUser(1, "Test1234");

    final HtmlAnchor anchor = dashboardPage.getAnchorByHref(Paths.BOOK_ADD);

    final HtmlPage bookAddPage = anchor.click();

    final HtmlForm bookAddForm = bookAddPage.getFormByName("book_add");

    // Get all required fields
    final HtmlButton bookAddButton = bookAddForm.getButtonByName("book_add_btn");
    final HtmlInput titleField = bookAddForm.getInputByName("title");
    final HtmlInput isbnField = bookAddForm.getInputByName("isbn");
    final HtmlInput releaseYearField = bookAddForm.getInputByName("release_year");
    final HtmlInput locationField = bookAddForm.getInputByName("location");
    final HtmlSelect categorySelection = bookAddForm.getSelectByName("category");
    final HtmlSelect publisherSelection = bookAddForm.getSelectByName("publisher");

    // Setup demo data for assertion
    String title = "Deutsch für die 5. Klasse";
    String isbn = "290115228313";
    String releaseYear = "2016";
    String location = "R2";
    String category = "17";
    String publisher = "4";

    // Type demo data into fields
    titleField.setValueAttribute(title);
    isbnField.setValueAttribute(isbn);
    releaseYearField.setValueAttribute(releaseYear);
    locationField.setValueAttribute(location);
    categorySelection.getOptionByValue(category).setSelected(true);
    publisherSelection.getOptionByValue(publisher).setSelected(true);

    final HtmlPage resultPage = bookAddButton.click();

    EntityStore<Persistable, Book> bookStore = DatabaseUtil.getData();

    Book book = bookStore.select(Book.class)
      .where(Book.ISBN.equal(isbn))
      .get().firstOrNull();

    assertNotNull(book);
  }
  @Test
  public void testAddBookDuplicateISBN() throws Exception {
    final HtmlPage dashboardPage = TestUtil.loginWithTestUser(1, "Test1234");

    final HtmlAnchor anchor = dashboardPage.getAnchorByHref(Paths.BOOK_ADD);

    final HtmlPage bookAddPage = anchor.click();

    final HtmlForm bookAddForm = bookAddPage.getFormByName("book_add");

    // Get all required fields
    final HtmlButton bookAddButton = bookAddForm.getButtonByName("book_add_btn");
    final HtmlInput titleField = bookAddForm.getInputByName("title");
    final HtmlInput isbnField = bookAddForm.getInputByName("isbn");
    final HtmlInput releaseYearField = bookAddForm.getInputByName("release_year");
    final HtmlInput locationField = bookAddForm.getInputByName("location");
    final HtmlSelect categorySelection = bookAddForm.getSelectByName("category");
    final HtmlSelect publisherSelection = bookAddForm.getSelectByName("publisher");

    // Setup demo data for assertion
    String title = "Mathematik";
    String isbn = "9783642449185";
    String releaseYear = "2017";
    String location = "R13";
    String category = "2";
    String publisher = "2";

    // Type demo data into fields
    titleField.setValueAttribute(title);
    isbnField.setValueAttribute(isbn);
    releaseYearField.setValueAttribute(releaseYear);
    locationField.setValueAttribute(location);
    categorySelection.getOptionByValue(category).setSelected(true);
    publisherSelection.getOptionByValue(publisher).setSelected(true);

    final HtmlPage resultPage = bookAddButton.click();

    assertNotNull(resultPage.querySelector("div.is-danger")); //Checks if error message is getting displayed
  }
}
