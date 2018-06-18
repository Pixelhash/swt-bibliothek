package de.fhl.swtlibrary;

import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import de.fhl.swtlibrary.model.BookCopy;
import de.fhl.swtlibrary.util.Paths;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.test.JoobyRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReturnControllerTest {
  @ClassRule
  public static JoobyRule app = new JoobyRule(new App());
  @BeforeClass
  public static void setup() throws Exception{
    TestUtil.setup();
  }

  @Test
  public void testReturnBorrowedBook() throws Exception {
    TestUtil.borrowBookWithTestUser(2, 1);

    EntityStore<Persistable, BookCopy> bookCopyEntityStore = DatabaseUtil.getData();
    BookCopy bookCopy = bookCopyEntityStore.select(BookCopy.class)
      .where(BookCopy.ID.eq(1))
      .get()
      .firstOrNull();

    // Check that the book is borrowed.
    assertNotNull(bookCopy);
    assertNotNull(bookCopy.getBorrower());
    assertNotNull(bookCopy.getBorrowedOn());
    assertNotNull(bookCopy.getReturnOn());

    // Login as employee
    //final HtmlPage dashboardPage = TestUtil.loginWithTestUser(1, "Test1234");
    // Now check if dashboard is reachable
    //assertEquals(Paths.USER_DASHBOARD, dashboardPage.getBaseURL().getPath());

    // Head to return page
    final HtmlPage returnPage = TestUtil.webClient.getPage(TestUtil.HOSTNAME + Paths.BOOK_RETURN);

    // Get return form
    final HtmlForm returnForm = returnPage.getFormByName("return_form");

    // Get input fields
    final HtmlInput userIdInput = returnForm.getInputByName("user_id");
    final HtmlInput bookCopyIdInput = returnForm.getInputByName("bookcopy_id");

    // Fill out input fields
    userIdInput.type("2");
    bookCopyIdInput.type("1");

    // Get submit button
    final HtmlButton submitButton = returnForm.getButtonByName("return_btn");
    // Now submit
    final HtmlPage resultPage = submitButton.click();

    // Check if result page is the current page page
    assertEquals(Paths.BOOK_RETURN, resultPage.getBaseURL().getPath());

    // Run query again
    bookCopy = bookCopyEntityStore.select(BookCopy.class)
      .where(BookCopy.ID.eq(1))
      .get()
      .firstOrNull();

    // Finally check the book copy object
    assertNull(bookCopy.getBorrower());
    assertNull(bookCopy.getBorrowedOn());
    assertNull(bookCopy.getReturnOn());
  }
}
