package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import de.fhl.swtlibrary.model.*;
import de.fhl.swtlibrary.util.*;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;
import org.mindrot.jbcrypt.BCrypt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Path("/user/")
public class RegisterController {
  private Request req;
  private EntityStore<Persistable, User> userEntityStore;
  private EntityStore<Persistable, Address> addressEntityStore;
  private Config config;

  @Inject
  public RegisterController(Config config, Request req, EntityStore<Persistable, User> userEntityStore,
                            EntityStore<Persistable, Address> addressEntityStore) {
    this.config = config;
    this.req = req;
    this.userEntityStore = userEntityStore;
    this.addressEntityStore = addressEntityStore;
  }

  @GET
  @Path("/register")
  public Result getRegisterUser() {
    return Results.html("pages/register");
  }


  @POST
  @Path("/register")
  public Result postRegisterUser() {
    String name = req.param("name").value();
    String surname = req.param("surname").value();
    String email = req.param("email").value();
    String phoneNumber = req.param("phone").value();
    phoneNumber = Validation.isNonEmptyString(phoneNumber) ? phoneNumber : null;
    String birthday = req.param("birthday").value();
    String location = req.param("location").value();
    String plz = req.param("plz").value();
    String street = req.param("street").value();
    String houseNumber = req.param("housenumber").value();
    String password = req.param("password").value();
    String passwordCorrect = req.param("passwordcorrect").value();

    // Check if all passed parameters are valid
    if (!Validation.isNonEmptyStringWithMinMaxLengthAndPatterns(name, 2, 100, Validation.NO_NUMBERS_PATTERN)
      || !Validation.isNonEmptyStringWithMinMaxLengthAndPatterns(surname, 2, 100, Validation.NO_NUMBERS_PATTERN)
      || !Validation.isNonEmptyString(email)
      || !Validation.isNonEmptyStringWithMinMaxLengthAndPatterns(location, 1, 100, Validation.NO_NUMBERS_PATTERN)
      || !Validation.isNonEmptyStringWithExactLengthAndPatterns(birthday, 10, Validation.IS_CORRECT_DATE)
      || !Validation.isNonEmptyStringWithMinMaxLengthAndPatterns(street, 1, 100, Validation.NO_NUMBERS_PATTERN)
      || !Validation.isNonEmptyStringWithExactLengthAndPatterns(plz, 5, Validation.ONLY_NUMBERS_PATTERN)
      || !Validation.isNonEmptyStringWithMinMaxLengthAndPatterns(houseNumber, 1, 8, Validation.IS_HOUSE_NUMBER_PATTERN)
      || !Validation.isNonEmptyString(password)
      || !Validation.isNonEmptyString(passwordCorrect)
      || !password.equals(passwordCorrect)) {
      return RenderUtil.error(req, Paths.USER_REGISTER, "ERROR_INVALID_FIELDS");
    }

    // If the phone number is available, check it too
    if (phoneNumber != null) {
      if (!Validation.isNonEmptyStringWithMinMaxLengthAndPatterns(phoneNumber, 5, 20, Validation.ONLY_NUMBERS_PATTERN)) {
        return RenderUtil.error(req, Paths.USER_EDIT, "ERROR_INVALID_FIELDS");
      }
    }

    // If a User with the same e-mail exists
    User alreadyExists = userEntityStore
      .select(User.class)
      .where(User.EMAIL.equal(email))
      .get()
      .firstOrNull();

    if(alreadyExists != null) {
      return RenderUtil.error(req, Paths.USER_REGISTER, "ERROR_USER_ALREADY_EXISTS");
    }

    // Generate Date Object out of the Birthday
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    Date date = null;

    try {
      date = sdf.parse(birthday);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    // Generate Address Object for the user
    Address address = new Address();
    address.setCity(location);
    address.setStreet(street);
    address.setPostcode(plz);
    address.setHouseNumber(houseNumber);

    // Generate new user
    User user = new User();
    user.setForename(name);
    user.setSurname(surname);
    user.setEmail(email);
    user.setPhoneNumber(phoneNumber);
    user.setRole(UserRole.KUNDE);
    user.setDateOfBirth(date);
    user.setAddress(address);
    user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
    user.setActivationToken(createActivationToken());

    // Adds the address and user to the database
    addressEntityStore.insert(address);
    userEntityStore.insert(user);

    // Generate activation email
    try {
      sendActivationEmail(user);
    } catch (EmailException e) {
      e.printStackTrace();
    }

    return RenderUtil.success(req, Paths.USER_REGISTER, "REGISTER_SUCCESS");
  }

  private void sendActivationEmail(User user) throws EmailException {
    String subject = "[Bibliothek] Aktivieren Sie ihren Account";
    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    String activationLink = "http://" + req.hostname() + ":" + req.port() + "/user/activate?activation_token=" + user.getActivationToken() + "?timestamp=" + timeStamp;
    String message = "Sehr geehrte(r) " + user.getFullName() + ",\n\ndies ist eine automatisch generierte Email. " +
      "Klicken Sie auf den unten erhaltenen Link um " +
      "ihren Account zu aktivieren.\n\n" + activationLink +
      "\n\nDie ID um sich einzuloggen lautet: " + user.getId() +
      "\n\nHinweis: Der Link ist nach einem Tag oder nach erfolgreicher Aktivierung ungültig und führt zur Startseite.";

    Email activationEmail = new SimpleEmail();
    activationEmail.setHostName(config.getString("mail.hostName"));
    activationEmail.setSmtpPort(config.getInt("mail.smtpPort"));
    activationEmail.setStartTLSEnabled(true);
    activationEmail.setAuthentication(config.getString("mail.username"), config.getString("mail.password"));
    activationEmail.setFrom(config.getString("mail.from"));

    activationEmail.setSubject(subject).setMsg(message).addTo(user.getEmail()).send();
  }

  public String createActivationToken() {
    String uuid = UUID.randomUUID().toString() + UUID.randomUUID().toString();
    return uuid.replaceAll("-", "");
  }
}
