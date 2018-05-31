package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.util.AuthenticationManager;
import de.fhl.swtlibrary.util.Paths;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;

@Path("/user/session")
public class LogoutController {

  private Request req;

  @Inject
  public LogoutController(Request req) {
    this.req = req;
  }

  @POST
  @Path("/logout")
  public Result postUserLogout() {
    AuthenticationManager.logout(req.session());

    return Results.redirect(Paths.USER_LOGIN);
  }

}
