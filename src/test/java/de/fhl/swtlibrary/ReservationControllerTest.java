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

import java.util.List;

import static org.junit.Assert.*;

public class ReservationControllerTest {
  @ClassRule
  public static JoobyRule app = new JoobyRule(new App());
  @BeforeClass
  public static void setup() throws Exception{
    TestUtil.setup();
  }

  @Test
  public void testReserveBook() throws Exception {
    EntityStore<Persistable, Reservation> reservationEntityStore = DatabaseUtil.getData();
    List<Reservation> reservations = reservationEntityStore.select(Reservation.class)
      .get()
      .toList();
    assertTrue(reservations.isEmpty());

    // First login
    final HtmlPage dashboardPage = TestUtil.loginWithTestUser(2, "Test1234");

    final HtmlPage reservationPage = TestUtil.webClient.getPage(TestUtil.HOSTNAME + Paths.BOOK_RESERVE + "/2");
    final HtmlForm reservationForm = reservationPage.getFormByName("book_reservation");
    final HtmlButton submitButton = reservationForm.getButtonByName("reserve_submit_btn");

    final HtmlPage resultPage = submitButton.click();

    // If success, you're headed to the dashboard
    assertEquals(Paths.USER_DASHBOARD, resultPage.getBaseURL().getPath());

    reservations = reservationEntityStore.select(Reservation.class)
      .get()
      .toList();

    assertEquals(1, reservations.size());

    int reservationId = reservations.get(0).getId();

    // Now try to delete the reservation
    final HtmlPage deleteReservationPage = TestUtil.webClient.getPage(TestUtil.HOSTNAME + Paths.BOOK_RESERVE_DELETE + "/" + reservationId);
    final HtmlForm reservationDeleteForm = deleteReservationPage.getFormByName("book_reservation_delete");
    final HtmlButton submitDeleteButton = reservationDeleteForm.getButtonByName("reserve_delete_submit_btn");

    final HtmlPage resultDeletePage = submitDeleteButton.click();

    // If success, you're headed to the dashboard
    assertEquals(Paths.USER_DASHBOARD, resultDeletePage.getBaseURL().getPath());

    reservations = reservationEntityStore.select(Reservation.class)
      .get()
      .toList();

    assertEquals(0, reservations.size());
  }

}
