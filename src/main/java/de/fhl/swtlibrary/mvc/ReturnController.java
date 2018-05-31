package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.BookCopy;
import de.fhl.swtlibrary.model.User;
import de.fhl.swtlibrary.util.RenderUtil;
import de.fhl.swtlibrary.util.Paths;
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

@Path(Paths.BOOK_RETURN)
public class ReturnController {

  private Request req;
  private EntityStore<Persistable, User> userEntityStore;
  private EntityStore<Persistable, BookCopy> bookCopyEntityStore;

  @Inject
  public ReturnController(Request req,
                          EntityStore<Persistable, User> userEntityStore,
                          EntityStore<Persistable, BookCopy> bookCopyEntityStore) {

    this.req = req;
    this.userEntityStore = userEntityStore;
    this.bookCopyEntityStore = bookCopyEntityStore;
  }

  @GET
  public Result getReturnBookCopy() {
    return Results.html("pages/return");
  }

  @POST
  public Result postReturnBookCopy() {

    String userIdStr = req.param("user_id").value();
    String bookCopyIdStr = req.param("bookcopy_id").value();

    Tuple<Boolean, Integer> validUserId = Validation.isValidInt(userIdStr);
    Tuple<Boolean, Integer> validBookCopyId = Validation.isValidInt(bookCopyIdStr);

    if (!validUserId.getFirstValue() || !validBookCopyId.getFirstValue()) {
      return RenderUtil.error(req, Paths.BOOK_RETURN, "ERROR_INVALID_RETURN_DATA");
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
      return RenderUtil.error(req, Paths.BOOK_RETURN, "ERROR_USER_NOT_FOUND");
    } else if (bookCopy == null) {
      return RenderUtil.error(req, Paths.BOOK_RETURN, "ERROR_BOOKCOPY_NOT_FOUND");
    }
    // Both objects exist

    // Check if book copy isn't borrowed
    if (bookCopy.getBorrower() == null) {
      return RenderUtil.error(req, Paths.BOOK_RETURN, "ERROR_BOOKCOPY_NOT_BORROWED");
    }

    // Return book
    bookCopy.setBorrower(null);
    bookCopy.setBorrowedOn(null);
    bookCopy.setReturnOn(null);

    bookCopyEntityStore.update(bookCopy);

    return RenderUtil.success(req, Paths.BOOK_RETURN, "SUCCESS_BOOK_RETURNED");
  }

}
