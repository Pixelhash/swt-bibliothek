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
import javassist.NotFoundException;
import org.jooby.*;
import org.jooby.assets.Assets;
import org.jooby.banner.Banner;
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
    }).orElse(() -> {

      err(404, (req, rsp, err) -> {
        rsp.send(Results.html("errors/404"));
      });

      // Not really error 500. Could be anything, but 404.
      err((req, rsp, err) -> {
        rsp.send(Results.html("errors/500"));
      });

    });

    on("prod", () -> {
      /* ASCII banner: */
      use(new Banner("SWT-II Library").font("doom"));

      /* Log all requests to file: */
      use("*", new RequestLogger());
    });

    before((req, res) -> {
      req.set("session", req.session());
      req.set("Paths", new Paths());
    });

    /* Check if user is authenticated */
    use("*", "*", (req, res, chain) -> {
        Boolean needsLogin = (Boolean) req.route().attributes().get("needsLogin"); // Reference boolean used here for null check
        if (needsLogin == null || !needsLogin) {
          chain.next(req, res);
        } else {
          if (!AuthenticationManager.isLoggedIn(req)) {
            res.redirect(Paths.USER_LOGIN);
            return;
          }

          EntityStore<Persistable, User> userStore = require(EntityStore.class);
          User user = AuthenticationManager.getLoggedInUser(userStore, req);

          req.set("user", user);
          chain.next(req, res);
        }
    });

    /* Check if user has a specific role */
    use("*", (req, res, chain) -> {
      UserRole r = (UserRole) req.route().attributes().get("role");
      if(r == null || ((User) req.get("user")).getRole() == r) {
        chain.next(req, res);
      }
      res.redirect(Paths.USER_DASHBOARD);
    });

    /* Search Routes: */
    use(SearchController.class);

    /* Categories Routes: */
    use(CategoriesController.class);

    /* Login Routes: */
    use(LoginController.class);

    with(() -> {

      /* Logout Route: */
      use(LogoutController.class);

      /* Dashboard Routes: */
      use(DashboardController.class);

      /* Edit User Data Routes: */
      use(EditUserDataController.class);

    }).attr("needsLogin", true);

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
