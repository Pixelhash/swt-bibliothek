package de.fhl.swtlibrary.util;

import de.fhl.swtlibrary.model.User;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.Request;
import org.jooby.Session;

public final class AuthenticationChecker {

  public static User getLoggedInUser(final EntityStore<Persistable, User> userStore, final Request req) {
    final int userId = getUserIdFromSession(req.session());

    if (userId == -1) {
      return null;
    }

    return userStore.select(User.class)
      .where(User.ID.eq(userId))
      .get()
      .firstOrNull();
  }

  public static boolean isLoggedIn(final Request req) {
    final int userId = getUserIdFromSession(req.session());
    return userId != -1;
  }

  private static int getUserIdFromSession(final Session session) {
    return session.get("userId").intValue(-1);
  }

}
