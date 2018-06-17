package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import de.fhl.swtlibrary.model.User;
import de.fhl.swtlibrary.util.*;
import io.requery.EntityStore;
import io.requery.Persistable;
import javafx.scene.shape.VertexFormat;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Path("/user/password/reset")
public class PasswordResetController {

  private Request req;
  private EntityStore<Persistable, User> userEntityStore;
  private SimpleEmail resetMail;
  private Config config;

  @Inject
  public PasswordResetController(Request req,
                                 EntityStore<Persistable, User> userEntityStore,
                                 SimpleEmail resetMail,
                                 Config config) {
    this.req = req;
    this.userEntityStore = userEntityStore;
    this.resetMail = resetMail;
    this.config = config;
  }

  @POST
  @Path("/request")
  public Result requestPasswordReset() throws EmailException {
    String email = req.param("email").value();

    if (!Validation.isNonEmptyString(email)) {
      return RenderUtil.error(req, Paths.USER_LOGIN, "ERROR_MISSING_FIELDS");
    }

    User user = userEntityStore.select(User.class)
      .where(User.EMAIL.eq(email))
      .get()
      .firstOrNull();

    if (user != null) {
      String resetToken = VerificationUtil.getRandomString(64);
      String resetTokenHash = VerificationUtil.hashSha256(resetToken);
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date());
      cal.add(Calendar.DAY_OF_WEEK, 1);
      Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
      String signature = VerificationUtil.doHmacSha256AsHex(config.getString("keys.passwordReset"), email + timestamp.getTime() + resetToken);

      user.setPasswordResetToken(resetTokenHash);
      userEntityStore.update(user);

      String passwordResetLink = buildPasswordResetLink(email, String.valueOf(timestamp.getTime()), resetToken, signature);

      String message = "Sehr geeehrte(r) " + user.getFullName() + ",\n\num Ihr Passwort zurückzusetzen, klicken Sie auf den nachfolgenden Link (24 Stunden gültig):\n\n" + passwordResetLink + "\n\nMit freundlichen Grüßen\nIhr Bibliotheksteam";
      String subject = "[Bibliothek] Passwort zurücksetzen";

      resetMail
        .setSubject(subject)
        .setMsg(message)
        .addTo(email)
        .send();
    }

    return RenderUtil.success(req, Paths.USER_LOGIN, "PASSWORD_RESET_SEND");
  }

  @GET
  @Path("/set/:email/:timestamp/:reset_token/:signature")
  public Result getPasswordReset() {
    req.set("email", req.param("email").value())
      .set("timestamp", req.param("timestamp").value())
      .set("reset_token", req.param("reset_token").value())
      .set("signature", req.param("signature").value());
    return Results.html("pages/password_reset");
  }

  @POST
  @Path("/set")
  public Result postPasswordReset() {
    String password = req.param("password").value();
    String confirmPassword = req.param("confirm_password").value();
    String email = req.param("email").value();
    String timestamp = req.param("timestamp").value();
    String resetToken = req.param("reset_token").value();
    String signature = req.param("signature").value();

    Tuple<Boolean, Long> timestampTuple = Validation.isValidLong(timestamp);

    if (!Validation.isNonEmptyString(password)
      || !Validation.isNonEmptyString(confirmPassword)
      || !Validation.isNonEmptyString(email)
      || !timestampTuple.getFirstValue()
      || !Validation.isNonEmptyString(resetToken)
      || !Validation.isNonEmptyString(signature)) {
      return RenderUtil.error(req, buildPasswordResetLink(email, timestamp, resetToken, signature), "ERROR_INVALID_FIELDS");
    }

    // Check if passwords match
    if (!password.equals(confirmPassword)) {
      return RenderUtil.error(req, buildPasswordResetLink(email, timestamp, resetToken, signature), "PASSWORD_RESET_DONT_MATCH");
    }

    // Check password length
    if (password.length() < 6) {
      return RenderUtil.error(req, buildPasswordResetLink(email, timestamp, resetToken, signature), "PASSWORD_RESET_SHORT_LENGTH");
    }

    // Check if signatures match
    if (!signature.equals(VerificationUtil.doHmacSha256AsHex(config.getString("keys.passwordReset"), email + timestamp + resetToken))) {
      return RenderUtil.error(req, buildPasswordResetLink(email, timestamp, resetToken, signature), "PASSWORD_RESET_INVALID_SIGNATURE");
    }

    // Check timestamp
    if (System.currentTimeMillis() > timestampTuple.getSecondValue()) {
      return RenderUtil.error(req, buildPasswordResetLink(email, timestamp, resetToken, signature), "PASSWORD_RESET_EXPIRED");
    }

    User user = userEntityStore.select(User.class)
      .where(User.PASSWORD_RESET_TOKEN.eq(VerificationUtil.hashSha256(resetToken)).and(User.EMAIL.eq(email)))
      .get()
      .firstOrNull();

    // Check if user exist
    if (user == null) {
      return RenderUtil.error(req, buildPasswordResetLink(email, timestamp, resetToken, signature), "PASSWORD_RESET_TOKEN_INVALID");
    }

    user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
    user.setPasswordResetToken(null);
    userEntityStore.update(user);

    return RenderUtil.success(req, Paths.USER_LOGIN, "PASSWORD_RESET_SUCCESS");
  }

  private String buildPasswordResetLink(String email, String timestamp, String token, String signature) {
    return String.format("http://%s:%d/user/password/reset/set/%s/%s/%s/%s",
      config.getString("application.host"),
      config.getInt("application.port"),
      email,
      timestamp,
      token,
      signature);
  }

}
