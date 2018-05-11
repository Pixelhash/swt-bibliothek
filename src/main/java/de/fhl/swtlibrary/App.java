package de.fhl.swtlibrary;

import de.fhl.swtlibrary.model.Book;
import de.fhl.swtlibrary.model.Models;
import de.fhl.swtlibrary.model.User;
import de.fhl.swtlibrary.mvc.*;
import de.fhl.swtlibrary.util.AuthenticationChecker;
import de.fhl.swtlibrary.util.Paths;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.sql.TableCreationMode;
import org.h2.tools.RunScript;
import org.jooby.FlashScope;
import org.jooby.Jooby;
import org.jooby.RequestLogger;
import org.jooby.assets.Assets;
import org.jooby.handlers.CsrfHandler;
import org.jooby.jdbc.Jdbc;
import org.jooby.json.Jackson;
import org.jooby.pebble.Pebble;
import org.jooby.requery.Requery;
import org.jooby.whoops.Whoops;

@SuppressWarnings("unchecked")
public class App extends Jooby {

  private static boolean TESTING = false;

  {
    {
      TESTING = Boolean.valueOf(System.getProperty("application.testing"));
      if (TESTING) {
        System.out.println("---------- Application started in TESTING mode! ----------");
        conf("application-testing.conf");
      }
    }

    /* Connection pool: */
    use(new Jdbc("db"));

    /* Requery: */
    use(new Requery(Models.DEFAULT)
      .schema(TableCreationMode.CREATE_NOT_EXISTS));

    /* JSON: */
    use(new Jackson());

    /* Assets (e.g. CSS, Js, ...): */
    use(new Assets());

    /* Template Engine: */
    use(new Pebble("templates", ".peb"));

    use(new FlashScope());

    use("*", new CsrfHandler());

    on("dev", () -> {
      /* Pretty Error page: */
      use(new Whoops());
    });

    on("prod", () -> {
      /* Log all requests to file: */
      use("*", new RequestLogger());
    });

    before((req, res) -> {
      EntityStore<Persistable, User> userStore = require(EntityStore.class);

      if (AuthenticationChecker.isLoggedIn(req)) {
        final User user = AuthenticationChecker.getLoggedInUser(userStore, req);
        if (user != null) req.set("user", user);
      }

      req.set("session", req.session());
      req.set("Paths", new Paths());
    });

    /* Search Routes: */
    use(SearchController.class);

    /* Authentication Routes: */
    use(AuthenticationController.class);

    /* Dashboard Routes: */
    use(DashboardController.class);

    /* Borrow BookCopy Routes: */
    use(BorrowController.class);

    /* Return BookCopy Routes: */
    use(ReturnController.class);
  }

  public static void main(final String[] args) {
    run(App::new, args);
  }

}
