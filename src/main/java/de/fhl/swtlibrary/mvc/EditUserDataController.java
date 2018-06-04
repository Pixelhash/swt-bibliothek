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
import org.jooby.mvc.Local;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;

import java.util.ArrayList;
import java.util.List;

@Path("/user/edit")
public class EditUserDataController {

  private Request req;
  private EntityStore<Persistable, User> userEntityStore;

  @Inject
  public EditUserDataController(Request req,
                                EntityStore<Persistable, User> userEntityStore) {
    this.req = req;
    this.userEntityStore = userEntityStore;
  }

  @GET
  public Result getEditUserData(@Local User user) {
    return Results.html("pages/edit_userdata");
  }

  @POST
  public Result postEditUserData(@Local User user) {
    String forename = req.param("forename").value();
    String surname = req.param("surname").value();
    String phoneNumber = req.param("phone_number").value();
    phoneNumber = Validation.isNonEmptyString(phoneNumber) ? phoneNumber : null;
    String street = req.param("street").value();
    String houseNumber = req.param("house_number").value();
    String postcode = req.param("postcode").value();
    String city = req.param("city").value();

    // Check if all passed parameters are valid
    if (!Validation.isNonEmptyStringWithMinMaxLengthAndPatterns(forename, 2, 100, Validation.NO_NUMBERS_PATTERN)
      || !Validation.isNonEmptyStringWithMinMaxLengthAndPatterns(surname, 2, 100, Validation.NO_NUMBERS_PATTERN)
      || !Validation.isNonEmptyStringWithMinMaxLengthAndPatterns(street, 1, 100, Validation.NO_NUMBERS_PATTERN)
      || !Validation.isNonEmptyStringWithMinMaxLengthAndPatterns(houseNumber, 1, 8, Validation.IS_HOUSE_NUMBER_PATTERN)
      || !Validation.isNonEmptyStringWithExactLengthAndPatterns(postcode, 5, Validation.ONLY_NUMBERS_PATTERN)
      || !Validation.isNonEmptyStringWithMinMaxLengthAndPatterns(city, 1, 100, Validation.NO_NUMBERS_PATTERN)) {
      return RenderUtil.error(req, Paths.USER_EDIT, "ERROR_INVALID_FIELDS");
    }

    // If the phone number is available, check it too
    if (phoneNumber != null) {
      if (!Validation.isNonEmptyStringWithMinMaxLengthAndPatterns(phoneNumber, 5, 20, Validation.ONLY_NUMBERS_PATTERN)) {
        return RenderUtil.error(req, Paths.USER_EDIT, "ERROR_INVALID_FIELDS");
      }
    }

    // Set new values
    user.setForename(forename);
    user.setSurname(surname);
    user.setPhoneNumber(phoneNumber);
    user.getAddress().setStreet(street);
    user.getAddress().setHouseNumber(houseNumber);
    user.getAddress().setPostcode(postcode);
    user.getAddress().setCity(city);

    // Update user in database
    userEntityStore.update(user);

    return RenderUtil.success(req, Paths.USER_DASHBOARD, "USERDATA_SAVE_SUCCESS");
  }
}
