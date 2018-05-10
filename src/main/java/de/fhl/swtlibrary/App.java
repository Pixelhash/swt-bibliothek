package de.fhl.swtlibrary;

import com.github.benmanes.caffeine.cache.Cache;
import de.fhl.swtlibrary.model.Models;
import de.fhl.swtlibrary.model.User;
import de.fhl.swtlibrary.mvc.*;
import de.fhl.swtlibrary.util.AuthenticationChecker;
import de.fhl.swtlibrary.util.Paths;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.sql.TableCreationMode;
import org.jooby.FlashScope;
import org.jooby.Jooby;
import org.jooby.RequestLogger;
import org.jooby.Session;
import org.jooby.assets.Assets;
import org.jooby.caffeine.CaffeineCache;
import org.jooby.handlers.CsrfHandler;
import org.jooby.jdbc.Jdbc;
import org.jooby.json.Jackson;
import org.jooby.pebble.Pebble;
import org.jooby.requery.Requery;
import org.jooby.whoops.Whoops;

@SuppressWarnings("unchecked")
public class App extends Jooby {

  {
    /* Connection pool: */
    use(new Jdbc("db"));

    /* Requery: */
    use(new Requery(Models.DEFAULT));

    /* JSON: */
    use(new Jackson());

    /* Assets (e.g. CSS, Js, ...): */
    use(new Assets());

    /* Template Engine: */
    use(new Pebble("templates", ".peb"));

    use(new CaffeineCache<Session, User>() {});

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

//      Cache cache = require(Cache.class);
//      User user = (User) cache.getIfPresent(req.session());
//      System.out.println(cache.asMap());
//      System.out.println(user);
//      if (user != null) {
//        System.out.println("User in session: " + user.getEmail());
//        req.set("user", user);
//      }
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
