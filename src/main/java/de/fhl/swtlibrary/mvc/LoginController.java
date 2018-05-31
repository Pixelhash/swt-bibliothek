package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
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
import org.mindrot.jbcrypt.BCrypt;

@Path("/user/session")
public class LoginController {

  private Request req;
  private EntityStore<Persistable, User> userEntityStore;

  @Inject
  public LoginController(Request req,
                         EntityStore<Persistable, User> userEntityStore) {
    this.req = req;
    this.userEntityStore = userEntityStore;
  }

  @GET
  @Path("/login")
  public Result getUserLogin() {
    return Results.html("pages/login");
  }

  @POST
  @Path("/login")
  public Result postUserLogin() {
    String userIdStr = req.param("user_id").value();
    String password = req.param("password").value();

    Tuple<Boolean, Integer> validUserId = Validation.isValidInt(userIdStr);

    if (!validUserId.getFirstValue() || !Validation.isNonEmptyString(password)) {
      return RenderUtil.error(req, Paths.USER_LOGIN, "ERROR_INVALID_LOGIN");
    }

    int userId = validUserId.getSecondValue();

    if (userId != -1 || !password.trim().isEmpty()) {
      User user = userEntityStore.select(User.class)
        .where(User.ID.eq(userId))
        .get()
        .firstOrNull();
      if (user != null) {
        if (BCrypt.checkpw(password, user.getPassword())) {
          AuthenticationManager.login(req.session(), user);
          return Results.redirect(Paths.USER_DASHBOARD);
        }
      }
    }

    return RenderUtil.error(req, Paths.USER_LOGIN, "ERROR_INVALID_LOGIN");
  }

}
