package de.fhl.swtlibrary;

import com.gargoylesoftware.htmlunit.html.*;
import de.fhl.swtlibrary.util.Paths;
import org.jooby.test.JoobyRule;
import org.junit.*;

import static org.junit.Assert.assertEquals;

public class EditUserDataControllerTest {
  @ClassRule
  public static JoobyRule app = new JoobyRule(new App());
  @BeforeClass
  public static void setup() throws Exception{
    TestUtil.setup();
  }


  @Test
  public void testEditUserDataButtonFromDashboard() throws Exception {
    final HtmlPage dashboardPage = TestUtil.loginWithTestUser(1, "Test1234");

    final HtmlAnchor anchor = dashboardPage.getAnchorByHref(Paths.USER_EDIT);

    final HtmlPage userEditPage = anchor.click();

    assertEquals(Paths.USER_EDIT, userEditPage.getBaseURL().getPath());
  }

  @Test
  public void testEditUserDataSaveDataSuccessful() throws Exception {
    final HtmlPage dashboardPage = TestUtil.loginWithTestUser(2, "Test1234");

    final HtmlAnchor anchor = dashboardPage.getAnchorByHref(Paths.USER_EDIT);

    final HtmlPage userEditPage = anchor.click();

    assertEquals(Paths.USER_EDIT, userEditPage.getBaseURL().getPath());

    final HtmlForm userEditForm = userEditPage.getFormByName("edit_user_data");

    // Get all required fields
    final HtmlButton userEditButton = userEditForm.getButtonByName("edit_user_data_btn");
    final HtmlInput forenameField = userEditForm.getInputByName("forename");
    final HtmlInput surnameField = userEditForm.getInputByName("surname");
    final HtmlInput phoneNumberField = userEditForm.getInputByName("phone_number");
    final HtmlInput streetField = userEditForm.getInputByName("street");
    final HtmlInput houseNumberField = userEditForm.getInputByName("house_number");
    final HtmlInput postcodeField = userEditForm.getInputByName("postcode");
    final HtmlInput cityField = userEditForm.getInputByName("city");

    // Setup demo data for assertion
    String forename = "Bettina";
    String surname = "Röbke";
    String phoneNumber = "01761122334455";
    String street = "Oldesloerstraße";
    String houseNumber = "42";
    String postcode = "23795";
    String city = "Bad Segeberg";

    // Type demo data into fields
    forenameField.setValueAttribute(forename);
    surnameField.setValueAttribute(surname);
    phoneNumberField.setValueAttribute(phoneNumber);
    streetField.setValueAttribute(street);
    houseNumberField.setValueAttribute(houseNumber);
    postcodeField.setValueAttribute(postcode);
    cityField.setValueAttribute(city);

    final HtmlPage resultPage = userEditButton.click();

    assertEquals(Paths.USER_DASHBOARD, resultPage.getBaseURL().getPath());
    assertEquals(forename + " " + surname, resultPage.getElementById("name").getTextContent().trim());
    assertEquals(phoneNumber, resultPage.getElementById("phone_number").getTextContent().trim());
    assertEquals(street + " " + houseNumber, resultPage.getElementById("street").getTextContent().trim());
    assertEquals(postcode + " " + city, resultPage.getElementById("location").getTextContent().trim());


    // Not working yet (database problem!?)
/*    EntityStore<Persistable, User> userStore = DatabaseUtil.getData();
    User user = userStore.select(User.class)
      .where(User.ID.eq(2))
      .get()
      .first();
    userStore.refresh(user, User.ADDRESS);

    assertEquals(Paths.USER_DASHBOARD, resultPage.getBaseURL().getPath());
    assertEquals(forename, user.getForename());
    assertEquals(surname, user.getSurname());
    assertEquals(phoneNumber, user.getPhoneNumber());
    assertEquals(street, user.getAddress().getStreet());
    assertEquals(houseNumber, user.getAddress().getHouseNumber());
    assertEquals(postcode, user.getAddress().getPostcode());
    assertEquals(city, user.getAddress().getCity());*/
  }

  @Test
  public void testEditUserDataSaveDataFail() throws Exception {
    final HtmlPage dashboardPage = TestUtil.loginWithTestUser(2, "Test1234");

    final HtmlAnchor anchor = dashboardPage.getAnchorByHref(Paths.USER_EDIT);

    final HtmlPage userEditPage = anchor.click();

    assertEquals(Paths.USER_EDIT, userEditPage.getBaseURL().getPath());

    final HtmlForm userEditForm = userEditPage.getFormByName("edit_user_data");

    // Get all required fields
    final HtmlButton userEditButton = userEditForm.getButtonByName("edit_user_data_btn");
    final HtmlInput forenameField = userEditForm.getInputByName("forename");
    final HtmlInput surnameField = userEditForm.getInputByName("surname");
    final HtmlInput phoneNumberField = userEditForm.getInputByName("phone_number");
    final HtmlInput streetField = userEditForm.getInputByName("street");
    final HtmlInput houseNumberField = userEditForm.getInputByName("house_number");
    final HtmlInput postcodeField = userEditForm.getInputByName("postcode");
    final HtmlInput cityField = userEditForm.getInputByName("city");

    // Setup demo data
    String forename = "Bettina2";
    String surname = "Röbke3";
    String phoneNumber = "01761122334455a";
    String street = "Oldesloerstraße3";
    String houseNumber = "holz";
    String postcode = "237951";
    String city = "Bad Segeberg1";

    // Type demo data into fields
    forenameField.setValueAttribute(forename);
    surnameField.setValueAttribute(surname);
    phoneNumberField.setValueAttribute(phoneNumber);
    streetField.setValueAttribute(street);
    houseNumberField.setValueAttribute(houseNumber);
    postcodeField.setValueAttribute(postcode);
    cityField.setValueAttribute(city);

    final HtmlPage resultPage = userEditButton.click();

    assertEquals(Paths.USER_EDIT, resultPage.getBaseURL().getPath());
  }
}
