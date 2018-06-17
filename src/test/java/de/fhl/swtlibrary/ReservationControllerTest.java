package de.fhl.swtlibrary;

import com.gargoylesoftware.htmlunit.html.*;
import de.fhl.swtlibrary.model.Book;
import de.fhl.swtlibrary.model.Reservation;
import de.fhl.swtlibrary.util.Paths;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.query.Expression;
import org.jooby.test.JoobyRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReservationControllerTest {
  @ClassRule
  public static JoobyRule app = new JoobyRule(new App());
  @BeforeClass
  public static void setup() throws Exception{
    TestUtil.setup();
  }


  @Test
  public void testreservation() throws Exception {
    final HtmlPage dashboardPage = TestUtil.loginWithTestUser(2, "Test1234");

    final HtmlAnchor anchore = dashboardPage.getAnchorByHref(Paths.BOOK_SEARCH);


    final HtmlPage searchPage = TestUtil.webClient.getPage(TestUtil.HOSTNAME + Paths.BOOK_SEARCH);

    final HtmlPage bookAddPage = anchore.click();
    final HtmlForm searchForm = bookAddPage.getFormByName("book_search_form");

    final HtmlButton searchButton = searchForm.getButtonByName("book_search_btn");

    final HtmlTextInput queryField = searchForm.getInputByName("query");

    queryField.type("mathe");

    final HtmlPage resultsPage = searchButton.click();

    final HtmlAnchor reserveBook = resultsPage.getAnchorByHref("/reserve/3");


    final HtmlPage addreservation = reserveBook.click();

    final HtmlForm bookReserveForm = addreservation.getFormByName("book_reservation");
    final HtmlInput bookId = bookReserveForm.getInputByName("book_id");


    final HtmlButton reserveSubmit = bookReserveForm.getButtonByName("reserve_submit_btn");

     final HtmlPage btn=  reserveSubmit.click();




    EntityStore<Persistable, Reservation> reservationEntityStore = DatabaseUtil.getData();


     Reservation reservation = reservationEntityStore.select(Reservation.class)
      .where(Reservation.BOOK_COPY_ID.equal(Integer.valueOf(bookId.getValueAttribute()))).get().firstOrNull();



















  }

}
