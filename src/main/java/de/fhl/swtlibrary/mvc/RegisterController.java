package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.*;
import de.fhl.swtlibrary.util.*;
import io.requery.EntityStore;
import io.requery.Persistable;
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

@Path("/user/")
public class RegisterController {
  private Request req;
  private EntityStore<Persistable, User> userEntityStore;
  private EntityStore<Persistable, Address> addressEntityStore;

  @Inject
  public RegisterController(Request req, EntityStore<Persistable, User> userEntityStore,
                            EntityStore<Persistable, Address> addressEntityStore) {
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
    String birthday = req.param("birthday").value();
    String location = req.param("location").value();
    String plz = req.param("plz").value();
    String street = req.param("street").value();
    String houseNumber = req.param("housenumber").value();
    String password = req.param("password").value();
    String passwordCorrect = req.param("passwordcorrect").value();

    Tuple<Boolean, Integer> validPhoneNumber = Validation.isValidInt(phoneNumber);
    Tuple<Boolean, Integer> validPlz = Validation.isValidInt(plz);
    Tuple<Boolean, Integer> validHouseNumber = Validation.isValidInt(houseNumber);

    // Check if all passed parameters are valid
    if (!Validation.isNonEmptyString(name)
      || !Validation.isNonEmptyString(surname)
      || !Validation.isNonEmptyString(email)
      || !Validation.isNonEmptyString(location)
      || !Validation.isNonEmptyString(birthday)
      || !Validation.isNonEmptyString(street)
      || !validPlz.getFirstValue()
      || !validHouseNumber.getFirstValue()
      || !Validation.isNonEmptyString(password)
      || !Validation.isNonEmptyString(passwordCorrect)
      || !password.equals(passwordCorrect)) {
      return RenderUtil.error(req, Paths.USER_REGISTER, "ERROR_INVALID_FIELDS");
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
    address.setPostcode(validPlz.getSecondValue().toString());
    address.setHouseNumber(validHouseNumber.getSecondValue().toString());

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

    // Adds the address and user to the database
    addressEntityStore.insert(address);
    int userid = userEntityStore.insert(user).getId();

    return RenderUtil.successWithAnswer(req, Paths.USER_REGISTER, "REGISTER_SUCCESS", "userid", userid);
  }
}
