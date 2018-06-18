package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.BookCopy;
import de.fhl.swtlibrary.model.Reservation;
import de.fhl.swtlibrary.model.User;
import de.fhl.swtlibrary.util.Paths;
import de.fhl.swtlibrary.util.RenderUtil;
import de.fhl.swtlibrary.util.Tuple;
import de.fhl.swtlibrary.util.Validation;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;

import java.time.LocalDateTime;
import java.util.Optional;

@Path(Paths.BOOK_BORROW)
public class BorrowController {

  private Request req;
  private EntityStore<Persistable, User> userEntityStore;
  private EntityStore<Persistable, BookCopy> bookCopyEntityStore;
  private EntityStore<Persistable, Reservation> reservationEntityStore;

  @Inject
  public BorrowController(Request req,
                          EntityStore<Persistable, User> userEntityStore,
                          EntityStore<Persistable, BookCopy> bookCopyEntityStore,
                          EntityStore<Persistable, Reservation> reservationEntityStore) {

    this.req = req;
    this.userEntityStore = userEntityStore;
    this.bookCopyEntityStore = bookCopyEntityStore;
    this.reservationEntityStore = reservationEntityStore;
  }

  @GET
  public Result getBorrowBookCopy() {
    return Results.html("pages/borrow");
  }

  @POST
  public Result postBorrowBookCopy() {

    String userIdStr = req.param("user_id").value();
    String bookCopyIdStr = req.param("bookcopy_id").value();

    Tuple<Boolean, Integer> validUserId = Validation.isValidInt(userIdStr);
    Tuple<Boolean, Integer> validBookCopyId = Validation.isValidInt(bookCopyIdStr);

    if (!validUserId.getFirstValue() || !validBookCopyId.getFirstValue()) {
      return RenderUtil.error(req, Paths.BOOK_BORROW, "ERROR_INVALID_BORROW_DATA");
    }

    int userId = validUserId.getSecondValue();
    int bookCopyId = validBookCopyId.getSecondValue();

    User targetUser = userEntityStore.select(User.class)
      .where(User.ID.eq(userId))
      .get()
      .firstOrNull();

    BookCopy bookCopy = bookCopyEntityStore.select(BookCopy.class)
      .where(BookCopy.ID.eq(bookCopyId))
      .get()
      .firstOrNull();

    if (targetUser == null) {
      return RenderUtil.error(req, Paths.BOOK_BORROW, "ERROR_USER_NOT_FOUND");
    } else if (bookCopy == null) {
      return RenderUtil.error(req, Paths.BOOK_BORROW, "ERROR_BOOKCOPY_NOT_FOUND");
    }
    // Both objects exist

    // Check if book copy already borrowed
    if (bookCopy.getBorrower() != null) {
      return RenderUtil.error(req, Paths.BOOK_BORROW, "ERROR_BOOKCOPY_BORROWED");
    }

    // Check if already reserved by other customer
    if (bookCopy.getReservation() != null && bookCopy.getReservation().getUser().getId() != targetUser.getId()) {
      return RenderUtil.error(req, Paths.BOOK_BORROW, "ERROR_BOOKCOPY_RESERVED");
    } else if (bookCopy.getReservation() != null && bookCopy.getReservation().getUser().getId() == targetUser.getId()) {
      reservationEntityStore.delete(bookCopy.getReservation()); // Delete reservation if it's the correct customer
    }

    // Borrow book
    LocalDateTime now = LocalDateTime.now();

    bookCopy.setBorrower(targetUser);
    bookCopy.setBorrowedOn(now);
    bookCopy.setReturnOn(now.plusDays(30));

    bookCopyEntityStore.update(bookCopy);

    return RenderUtil.success(req, Paths.BOOK_BORROW, "SUCCESS_BOOK_BORROWED");
  }
}
