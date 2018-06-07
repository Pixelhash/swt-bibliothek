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
}
