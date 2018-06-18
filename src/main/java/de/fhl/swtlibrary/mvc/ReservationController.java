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
  private EntityStore<Persistable, BookCopy> bookCopyEntityStore;
  private EntityStore<Persistable, Reservation> reservationEntityStore;

  @Inject
  public ReservationController(Request req,
                               EntityStore<Persistable, User> userEntityStore,
                               EntityStore<Persistable, Book> bookEntityStore,
                               EntityStore<Persistable, BookCopy> bookCopyEntityStore,
                               EntityStore<Persistable,Reservation> reservationEntityStore){

    this.req = req;
    this.userEntityStore = userEntityStore;
    this.bookEntityStore = bookEntityStore;
    this.bookCopyEntityStore = bookCopyEntityStore;
    this.reservationEntityStore = reservationEntityStore;
  }

  @GET
  @Path("/:book_id")
  public Result getReserveBookCopy() {

    String bookIdParam = req.param("book_id").value();
    Tuple<Boolean, Integer> validBookId = Validation.isValidInt(bookIdParam);

    if (!validBookId.getFirstValue()) {
      return RenderUtil.error(req, Paths.BOOK_RESERVE, "ERROR_INVALID_BORROW_DATA");
    }

    Book book = bookEntityStore.select(Book.class)
      .where(BookCopy.ID.eq(validBookId.getSecondValue()))
      .get()
      .firstOrNull();

    if (book == null) {
      return RenderUtil.error(req, Paths.BOOK_RESERVE, "ERROR_INVALID_BORROW_DATA");
    }

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

    // Check if valid book id
    if (!validBookId.getFirstValue()) {
      return RenderUtil.error(req, Paths.BOOK_RESERVE + "/" + bookIdStr, "ERROR_INVALID_BORROW_DATA");
    }

    int bookId = validBookId.getSecondValue();

    Book book = bookEntityStore.select(Book.class)
      .where(Book.ID.eq(bookId))
      .get()
      .firstOrNull();

    // Check if book with given id exists
    if (book == null) {
      return RenderUtil.error(req, Paths.BOOK_RESERVE + "/" + bookId, "ERROR_RESERVE_NOT_FOUND");
    }

    bookEntityStore.refresh(book, Book.COPIES);
    List<BookCopy> bookCopyList = book.getAvailableCopies();

    if (bookCopyList.isEmpty()){
      return RenderUtil.error(req, Paths.BOOK_RESERVE + "/" + bookId, "ERROR_INVALID_BORROW_DATA");
    }

    // Check if already reserved
    if (user.getReservations().stream().filter(reservation -> reservation.getBookCopy().getBook().getId() == bookId).count() != 0) {
      return RenderUtil.error(req, Paths.BOOK_RESERVE + "/" + bookId, "RESERVATION_ALREADY_RESERVED");
    }

    // Check if user has this book already borrowed
    if (user.getBorrowedBooks().stream().filter(bookCopy -> bookCopy.getBook().getId() == bookId).count() != 0) {
      return RenderUtil.error(req, Paths.BOOK_RESERVE + "/" + bookId, "RESERVATION_ALREADY_BORROWED");
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

  @GET
  @Path("/delete/:reservation_id")
  public Result getReserveBookCopyDelete() {

    User user = AuthenticationManager.getLoggedInUser(userEntityStore, req);
    String reservationId = req.param("reservation_id").value();
    Tuple<Boolean, Integer> validReservationId = Validation.isValidInt(reservationId);

    if (!validReservationId.getFirstValue()) {
      return RenderUtil.error(req, Paths.USER_DASHBOARD, "ERROR_INVALID_RESERVATION_ID");
    }

    Reservation reservation = reservationEntityStore.select(Reservation.class)
      .where(Reservation.ID.eq(validReservationId.getSecondValue()).and(Reservation.USER_ID.eq(user.getId())))
      .get()
      .firstOrNull();

    // Check if reservation for user exists
    if (reservation == null) {
      return RenderUtil.error(req, Paths.USER_DASHBOARD, "ERROR_RESERVATION_DOESNT_EXIST");
    }

    Book book = reservation.getBookCopy().getBook();
    bookEntityStore.refresh(book, Book.AUTHORS);

    req.set("reservation", reservation).set("book", reservation.getBookCopy().getBook());

    return Results.html("pages/reservation_delete");
  }

  @POST
  @Path("/delete")
  public Result postReserveBookCopyDelete() {

    User user = AuthenticationManager.getLoggedInUser(userEntityStore, req);
    String reservationId = req.param("reservation_id").value();
    Tuple<Boolean, Integer> validReservationId = Validation.isValidInt(reservationId);

    if (!validReservationId.getFirstValue()) {
      return RenderUtil.error(req, Paths.USER_DASHBOARD, "ERROR_INVALID_RESERVATION_ID");
    }

    Reservation reservation = reservationEntityStore.select(Reservation.class)
      .where(Reservation.ID.eq(validReservationId.getSecondValue()).and(Reservation.USER_ID.eq(user.getId())))
      .get()
      .firstOrNull();

    // Check if reservation for user exists
    if (reservation == null) {
      return RenderUtil.error(req, Paths.USER_DASHBOARD, "ERROR_RESERVATION_DOESNT_EXIST");
    }

    reservationEntityStore.delete(reservation);

    return RenderUtil.success(req, Paths.USER_DASHBOARD, "RESERVATION_DELETE_SUCCESS");
  }

}
