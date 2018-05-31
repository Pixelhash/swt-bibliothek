package de.fhl.swtlibrary.util;

import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;

public final class RenderUtil {

  public static Result error(final Request req, final String path, final String messageKey) {
    req.flash("error", true)
      .flash("error_message", messageKey);
    return Results.redirect(path);
  }

  public static Result success(final Request req, final String path, final String messageKey) {
    req.flash("success", true)
      .flash("success_message", messageKey);
    return Results.redirect(path);
  }

}
