package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.*;
import de.fhl.swtlibrary.util.AuthenticationManager;
import de.fhl.swtlibrary.util.Paths;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.GET;
import org.jooby.mvc.Local;
import org.jooby.mvc.Path;

import java.util.ArrayList;
import java.util.List;

@Path("/user/edit")
public class EditUserDataController {

  private Request req;
  private EntityStore<Persistable, User> userEntityStore;

  @Inject
  public EditUserDataController(Request req,
                                EntityStore<Persistable, User> userEntityStore) {
    this.req = req;
    this.userEntityStore = userEntityStore;
  }

  @GET
  public Result getUserDashboard(@Local User user) {
    return Results.html("pages/edit_userdata");
  }
}
