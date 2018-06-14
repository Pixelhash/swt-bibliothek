package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.Book;
import de.fhl.swtlibrary.model.Category;
import de.fhl.swtlibrary.model.User;
import de.fhl.swtlibrary.util.Paths;
import de.fhl.swtlibrary.util.RenderUtil;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.POST;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Path("/user/")
public class ActivationController {

  private Request req;
  private EntityStore<Persistable, User> userEntityStore;

  @Inject
  public ActivationController(Request req, EntityStore<Persistable, User> userEntityStore) {
    this.req = req;
    this.userEntityStore = userEntityStore;
  }

  @GET
  @Path("/activate")
  public Result getActivationPage() {
    long diff = 0;

    String activationTokenString = req.queryString().get().split("\\?")[0].substring(17);
    String timeStampLinkString = req.queryString().get().split("\\?")[1].substring(10);

    User user = userEntityStore.select(User.class)
      .where(User.ACTIVATION_TOKEN.eq(activationTokenString))
      .get()
      .firstOrNull();

    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
      Date timeStampLink = sdf.parse(timeStampLinkString);
      Date timeStampNow = sdf.parse(sdf.format(new Date()));

      long diffInMillies = Math.abs(timeStampNow.getTime() - timeStampLink.getTime());
      diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    if (user == null || diff > 0) {
      return Results.html("pages/index");
    } else {
      user.setActivation_token(null);
      userEntityStore.update(user);
      return Results.html("pages/activate");
    }
  }
}
