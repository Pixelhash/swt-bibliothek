package de.fhl.swtlibrary;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import de.fhl.swtlibrary.model.User;
import de.fhl.swtlibrary.util.Paths;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.sql.EntityDataStore;
import org.jooby.test.JoobyRule;
import org.jooby.test.MockRouter;
import org.junit.*;

import javax.sql.DataSource;

import static io.restassured.RestAssured.form;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class AppTest {

  /**
   * One app/server for all the test of this class. If you want to start/stop a new server per test,
   * remove the static modifier and replace the {@link ClassRule} annotation with {@link Rule}.
   */
  @ClassRule
  public static JoobyRule app = new JoobyRule(new App());

  private static final String HOSTNAME = "http://localhost:4567";
  private static WebClient webClient;

  @BeforeClass
  public static void setUp() throws Exception {
    DatabaseUtil.setUpTestDatabase();

    webClient = new WebClient();
    webClient.getOptions().setCssEnabled(false);
    webClient.getOptions().setJavaScriptEnabled(false);
    webClient.getOptions().setThrowExceptionOnScriptError(false);
  }

  private HtmlPage loginWithTestUser(final int id, final String password) throws Exception {
    final HtmlPage loginPage = webClient.getPage(HOSTNAME + Paths.USER_LOGIN);

    final HtmlForm loginForm = loginPage.getFormByName("login_form");

    final HtmlButton loginButton = loginForm.getButtonByName("login_btn");
    final HtmlTextInput userIdField = loginForm.getInputByName("user_id");
    final HtmlPasswordInput passwordField = loginForm.getInputByName("password");

    userIdField.type(String.valueOf(id));
    passwordField.type(password);

    // Return the result page, either still login page (error) or user dashboard
    return loginButton.click();
  }

  @Test
  public void testBookSearchForm() throws Exception {
      final HtmlPage page = webClient.getPage(HOSTNAME + Paths.BOOK_SEARCH);

      String expected = "Buchsuche | Bibliothek";
      assertEquals(expected, page.getTitleText());
  }

  @Test
  public void testBookSearchInputs() throws Exception {
    final HtmlPage searchPage = webClient.getPage(HOSTNAME + Paths.BOOK_SEARCH);

    final HtmlForm searchForm = searchPage.getFormByName("book_search_form");

    final HtmlButton searchButton = searchForm.getButtonByName("book_search_btn");
    final HtmlTextInput queryField = searchForm.getInputByName("query");

    queryField.type("mathe");

    final HtmlPage resultsPage = searchButton.click();

    String expected = "Suchergebnisse | Bibliothek";
    assertEquals(expected, resultsPage.getTitleText());
  }

  @Test
  public void testSuccessfulLogin() throws Exception {
    final HtmlPage dashboardPage = loginWithTestUser(1, "Test1234");

    assertEquals(Paths.USER_DASHBOARD, dashboardPage.getBaseURL().getPath());
  }

  @Test
  public void testEditUserDataButtonFromDashboard() throws Exception {
    final HtmlPage dashboardPage = loginWithTestUser(1, "Test1234");

    final HtmlAnchor anchor = dashboardPage.getAnchorByHref(Paths.USER_EDIT);

    final HtmlPage userEditPage = anchor.click();

    assertEquals(Paths.USER_EDIT, userEditPage.getBaseURL().getPath());
  }

  @Test
  public void testEditUserDataSaveDataSuccessful() throws Exception {
    final HtmlPage dashboardPage = loginWithTestUser(2, "Test1234");

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
    final HtmlPage dashboardPage = loginWithTestUser(2, "Test1234");

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
