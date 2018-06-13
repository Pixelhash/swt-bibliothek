package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.Book;
import de.fhl.swtlibrary.model.BookCopy;
import de.fhl.swtlibrary.model.Reservation;
import de.fhl.swtlibrary.model.User;

import de.fhl.swtlibrary.util.*;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;

import java.time.LocalDateTime;
import java.util.List;

@Path(Paths.BOOK_RESERVE)


public class ReservationController {

  private Request req;
  private EntityStore<Persistable, User> userEntityStore;
  private EntityStore<Persistable, Book> bookEntityStore;
  private EntityStore<Persistable, Reservation> reservationEntityStore;

  @Inject
  public ReservationController(Request req,
                               EntityStore<Persistable, User> userEntityStore,
                               EntityStore<Persistable, Book> bookEntityStore,
                               EntityStore<Persistable,Reservation> reservationEntityStore){

    this.req = req;
    this.userEntityStore = userEntityStore;
    this.bookEntityStore = bookEntityStore;
    this.reservationEntityStore = reservationEntityStore;

  }


  @GET
  @Path("/:book_id")
  public Result getReserveBookCopy() {


    String bookIdParam = req.param("book_id").value();
    Tuple<Boolean, Integer> validBookId = Validation.isValidInt(bookIdParam);
    if (!validBookId.getFirstValue()) {
      return RenderUtil.error(req, Paths.BOOK_RESERVE, "ERROR_INVALID_BORROW_DATA");}

    Book book = bookEntityStore.select(Book.class)
      .where(BookCopy.ID.eq(validBookId.getSecondValue()))
      .get()
      .firstOrNull();
    if(book== null)
      return RenderUtil.error(req, Paths.BOOK_RESERVE, "ERROR_INVALID_Reservation_DAT");

    bookEntityStore.refresh(book, Book.AUTHORS);
    req.set("book", book);

    return Results.html("pages/reservation");
  }


  @POST
  @Path("/submit")
  public Result postReserveBookCopy() {

    User user = AuthenticationManager.getLoggedInUser(userEntityStore, req);
    String bookIdStr = req.param("book_id").value();

    Tuple<Boolean, Integer> validBookId = Validation.isValidInt(bookIdStr);

    if (!validBookId.getFirstValue()) {
      return RenderUtil.error(req, Paths.BOOK_RESERVE, "ERROR_INVALID_BORROW_DATA");
    }

    int bookId = validBookId.getSecondValue();



    Book book = bookEntityStore.select(Book.class)
      .where(BookCopy.ID.eq(bookId))
      .get()
      .firstOrNull();

    if (book == null) {
      return RenderUtil.error(req, Paths.BOOK_RESERVE, "ERROR_RESERVE_NOT_FOUND");
    }
      List<BookCopy> bookCopyList = book.getCopies();
    if(bookCopyList.isEmpty()){
      return RenderUtil.error(req, Paths.BOOK_RESERVE, "ERROR_INVALID_Reservation_DATA");
    }


    // Reserve book
    LocalDateTime now = LocalDateTime.now();

    Reservation reservation = new Reservation();
    reservation.setReservedUntil(now.plusDays(3));
    reservation.setUser(user);
    reservation.setBookCopy(bookCopyList.get(0));

    reservationEntityStore.insert(reservation);

    return RenderUtil.success(req, Paths.USER_DASHBOARD, "SUCCESS_BOOK_RESERVED");
  }

}
