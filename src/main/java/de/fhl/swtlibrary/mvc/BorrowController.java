package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.BookCopy;
import de.fhl.swtlibrary.model.User;
import de.fhl.swtlibrary.util.AuthenticationChecker;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;

@Path("/book")
public class BorrowController {

  private Request req;
  private EntityStore<Persistable, User> userEntityStore;
  private EntityStore<Persistable, BookCopy> bookCopyEntityStore;

  @Inject
  public BorrowController(Request req,
                          EntityStore<Persistable, User> userEntityStore,
                          EntityStore<Persistable, BookCopy> bookCopyEntityStore) {

    this.req = req;
    this.userEntityStore = userEntityStore;
    this.bookCopyEntityStore = bookCopyEntityStore;
  }

  @GET
  @Path("/borrow")
  public Result getBorrowBookCopy() {
    // Check if logged in
    if (!AuthenticationChecker.isLoggedIn(req)) {
      return Results.redirect("/user/session/login");
    }

    User user = AuthenticationChecker.getLoggedInUser(userEntityStore, req);

    if (user == null) {
      return Results.redirect("/user/session/login");
    }

    // Check if user is an employee
    if (!user.isEmployee()) {
      return Results.redirect("/user/dashboard");
    }

    return Results.html("pages/borrow");
  }

  @POST
  @Path("/borrow")
  public Result postBorrowBookCopy() {
    // Check if logged in
    if (!AuthenticationChecker.isLoggedIn(req)) {
      return Results.redirect("/user/session/login");
    }

    User user = AuthenticationChecker.getLoggedInUser(userEntityStore, req);

    if (user == null) {
      return Results.redirect("/user/session/login");
    }

    // Check if user is an employee
    if (!user.isEmployee()) {
      return Results.redirect("/user/dashboard");
    }

    return Results.ok();

  }
}
