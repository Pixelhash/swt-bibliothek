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
    String street = req.param("street").value();
    String houseNumber = req.param("house_number").value();
    String postcodeStr = req.param("postcode").value();
    String city = req.param("city").value();

    Tuple<Boolean, Integer> validPostcode = Validation.isValidInt(postcodeStr);

    // Check if all passed parameters are valid
    if (!Validation.isNonEmptyString(forename)
      || !Validation.isNonEmptyString(surname)
      || !Validation.isNonEmptyString(street)
      || !Validation.isNonEmptyString(houseNumber)
      || !Validation.isNonEmptyString(city)
      || !validPostcode.getFirstValue()) {
      return RenderUtil.error(req, Paths.USER_EDIT, "ERROR_MISSING_FIELDS");
    }

    int postcode = validPostcode.getSecondValue();

    // Set new values
    user.setForename(forename);
    user.setSurname(surname);
    user.setPhoneNumber(Validation.isNonEmptyString(phoneNumber) ? phoneNumber : null);
    user.getAddress().setStreet(street);
    user.getAddress().setHouseNumber(houseNumber);
    user.getAddress().setPostcode(postcode);
    user.getAddress().setCity(city);

    // Update user in database
    userEntityStore.update(user);

    return RenderUtil.success(req, Paths.USER_DASHBOARD, "USERDATA_SAVE_SUCCESS");
  }
}
