package de.fhl.swtlibrary.mvc;

import com.github.benmanes.caffeine.cache.Cache;
import com.google.inject.Inject;
import de.fhl.swtlibrary.model.User;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

@Path("/user/session")
public class AuthenticationController {

  private Request req;
  private EntityStore<Persistable, User> userEntityStore;
  private Cache<String, User> userCache;

  @Inject
  public AuthenticationController(Request req, EntityStore<Persistable, User> userEntityStore,
                                  Cache<String, User> userCache) {
    this.req = req;
    this.userEntityStore = userEntityStore;
    this.userCache = userCache;
  }

  @GET
  @Path("/login")
  public Result getUserLogin() {
    return Results.html("pages/login");
  }

  @POST
  @Path("/login")
  public Result postUserLogin() {
    int userId = req.param("user_id").intValue(-1);
    String password = req.param("password").value();

    if (userId != -1 || !password.trim().isEmpty()) {
      List<User> users = userEntityStore.select(User.class)
        .where(User.ID.eq(userId))
        .get()
        .toList();
      if (!users.isEmpty()) {
        User user = users.get(0);
        if (BCrypt.checkpw(password, user.getPassword())) {
          System.out.println("Correct password!");
          userCache.put("user", user);
        }
      }
    }

    req.flash("error", true)
      .flash("error_message", "ERROR_INVALID_LOGIN");
    return Results.redirect("/user/session/login");
  }

  @POST
  @Path("/logout")
  public Result postUserLogout() {
    User currentUser = userCache.getIfPresent("user");
    if (currentUser != null) {
      userCache.invalidate("user");
    }

    return Results.redirect("/");
  }

}
