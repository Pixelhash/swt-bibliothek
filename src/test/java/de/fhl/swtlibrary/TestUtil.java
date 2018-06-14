package de.fhl.swtlibrary;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import de.fhl.swtlibrary.util.Paths;

public class TestUtil {

  protected static WebClient webClient;
  protected static final String HOSTNAME = "http://localhost:4567";

  protected static void setup() throws Exception{
    DatabaseUtil.setUpTestDatabase();

    if(webClient != null) webClient.close(); //Close the WebClient if it was open previously

    webClient = new WebClient();
    webClient.getOptions().setCssEnabled(false);
    webClient.getOptions().setJavaScriptEnabled(false);
    webClient.getOptions().setThrowExceptionOnScriptError(false);
  }

  protected static HtmlPage loginWithTestUser(final int id, final String password) throws Exception {
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

  protected static HtmlPage registerWithTestUser(String email) throws Exception {
    final HtmlPage loginPage = webClient.getPage(HOSTNAME + Paths.USER_REGISTER);

    final HtmlForm registerForm = loginPage.getFormByName("register_form");

    final HtmlButton registerButton = registerForm.getButtonByName("user_register_btn");
    final HtmlTextInput nameField = registerForm.getInputByName("name");
    final HtmlTextInput surnameField = registerForm.getInputByName("surname");
    final HtmlEmailInput emailField = registerForm.getInputByName("email");
    final HtmlNumberInput phoneField = registerForm.getInputByName("phone");
    final HtmlTextInput birthdayField = registerForm.getInputByName("birthday");
    final HtmlTextInput locationField = registerForm.getInputByName("location");
    final HtmlNumberInput plzField = registerForm.getInputByName("plz");
    final HtmlTextInput streetField = registerForm.getInputByName("street");
    final HtmlNumberInput houseNumberField = registerForm.getInputByName("housenumber");
    final HtmlPasswordInput passwordField = registerForm.getInputByName("password");
    final HtmlPasswordInput passwordCorrectField = registerForm.getInputByName("passwordcorrect");

    nameField.type("Max");
    surnameField.type("Mustermann");
    emailField.type(email);
    phoneField.type(123456789);
    birthdayField.type("01.01.2000");
    locationField.type("Musterhausen");
    plzField.type("12345");
    streetField.type("Musterstraße");
    houseNumberField.type("123");
    passwordField.type("Test1234");
    passwordCorrectField.type("Test1234");

    // Return the result page, either still register page (error) or user dashboard
    return registerButton.click();
  }
}
