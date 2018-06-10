package de.fhl.swtlibrary;

import de.fhl.swtlibrary.model.Models;
import de.fhl.swtlibrary.model.User;
import de.fhl.swtlibrary.model.UserRole;
import de.fhl.swtlibrary.mvc.*;
import de.fhl.swtlibrary.util.AuthenticationManager;
import de.fhl.swtlibrary.util.Paths;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.sql.TableCreationMode;
import org.jooby.FlashScope;
import org.jooby.Jooby;
import org.jooby.RequestLogger;
import org.jooby.Results;
import org.jooby.assets.Assets;
import org.jooby.banner.Banner;
import org.jooby.handlers.CsrfHandler;
import org.jooby.jdbc.Jdbc;
import org.jooby.json.Jackson;
import org.jooby.mail.CommonsEmail;
import org.jooby.pebble.Pebble;
import org.jooby.requery.Requery;
import org.jooby.whoops.Whoops;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class App extends Jooby {

  public static Logger LOG = LoggerFactory.getLogger(App.class);

  {
    {
      boolean isInTestingMode = Boolean.valueOf(System.getProperty("application.testing"));
      if (isInTestingMode) {
        LOG.info("Application started in TESTING mode!");
        conf("application-testing.conf");
      }
    }

    /* Connection Pool: */
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

    /* Email Engine: */
    use(new CommonsEmail());

    /* Flash Attributes: */
    use(new FlashScope());

    /* CSRF Check: */
    use("*", new CsrfHandler());

    /* Enable pretty error pages in development mode: */
    on("dev", () -> {
      /* Pretty Error page: */
      use(new Whoops());
    }).orElse(() -> {

      err(404, (req, rsp, err) -> rsp.send(Results.html("errors/404")));

      // Not really error 500. Could be anything, but 404.
      err((req, rsp, err) -> rsp.send(Results.html("errors/500")));

    });

    /* Enable some extras like logging in production: */
    on("prod", () -> {
      /* ASCII banner: */
      use(new Banner("SWT-II Library").font("doom"));

      /* Log all requests to file: */
      use("*", new RequestLogger());
    });

    /* Set some attributes for each request: */
    before((req, res) -> {
      req.set("session", req.session()); // Current session
      req.set("Paths", new Paths()); // All available URL base paths
      if (AuthenticationManager.isLoggedIn(req)) { // Set user if logged in
        EntityStore<Persistable, User> userStore = require(EntityStore.class);
        User user = AuthenticationManager.getLoggedInUser(userStore, req);

        req.set("user", user);
      }
    });

    /* Check if user is authenticated: */
    use("*", "*", (req, res, chain) -> {
        Boolean needsLogin = (Boolean) req.route().attributes().get("needsLogin"); // Reference boolean used here for null check

        // If route needs login and user isn't authenticated, redirect him to the login page
        if (needsLogin != null && needsLogin && !AuthenticationManager.isLoggedIn(req)) {
          res.redirect(Paths.USER_LOGIN);
          return;
        }

        // User is authenticated, continue
        chain.next(req, res);
    });

    /* Check if user has a specific role: */
    use("*", "*", (req, res, chain) -> {
      UserRole role = (UserRole) req.route().attributes().get("role");

      // If route needs a specific role and the roles don't match redirect the user to the dashboard
      if (role != null && ((User) req.get("user")).getRole() != role) {
        res.redirect(Paths.USER_DASHBOARD);
        return;
      }

      // Roles match, continue
      chain.next(req, res);
    });

    /* Search Routes: */
    use(SearchController.class);

    /* Login Routes: */
    use(LoginController.class);

    /* Routes that require an authenticated user: */
    with(() -> {

      /* Logout Route: */
      use(LogoutController.class);

      /* Dashboard Routes: */
      use(DashboardController.class);

      /* Edit User Data Routes: */
      use(EditUserDataController.class);

    }).attr("needsLogin", true);

    /* Routes that require the user to be an employee: */
    with(() -> {

      /* Borrow BookCopy Routes: */
      use(BorrowController.class);

      /* Return BookCopy Routes: */
      use(ReturnController.class);

    }).attr("role", UserRole.MITARBEITER).attr("needsLogin", true);

  }

  public static void main(final String[] args) {
    run(App::new, args);
  }

}
