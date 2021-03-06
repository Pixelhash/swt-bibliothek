package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.User;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
      user.setActivationToken(null);
      userEntityStore.update(user);
      return Results.html("pages/activate");
    }
  }
}
