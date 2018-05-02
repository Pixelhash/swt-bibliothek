package de.fhl.swtlibrary.mvc;

import com.github.benmanes.caffeine.cache.Cache;
import com.google.inject.Inject;
import de.fhl.swtlibrary.model.User;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;

@Path("/user/dashboard")
public class DashboardController {

  private Request req;
  private Cache<String, User> userCache;

  @Inject
  public DashboardController(Request req, Cache<String, User> userCache) {
    this.req = req;
    this.userCache = userCache;
  }

  @GET
  public Result getUserDashboard() {
    User currentUser = userCache.getIfPresent("user");
    if (currentUser == null) {
      return Results.redirect("/user/session/login");
    }
    System.out.println("Role: " + currentUser.getRole());
    return Results.html("pages/dashboard");
  }

}
