package com.framework.pages;

import com.framework.base.BasePage;
import com.framework.constants.Messages;
import com.framework.constants.Locators;
import com.microsoft.playwright.Locator;

public class LoginPage extends BasePage {

    private static final LoginPage INSTANCE = new LoginPage();

    private LoginPage() {
    }

    public static LoginPage getInstance() {
        return INSTANCE;
    }


    private Locator newUserSignupHeading() {
        return page().locator(Locators.Login.SIGNUP_HEADING);
    }

    private Locator signupNameInput() {
        return page().locator(Locators.Login.SIGNUP_NAME);
    }

    private Locator signupEmailInput() {
        return page().locator(Locators.Login.SIGNUP_EMAIL);
    }

    private Locator signupButton() {
        return page().locator(Locators.Login.SIGNUP_BUTTON);
    }

    private Locator loginEmailInput() {
        return page().locator(Locators.Login.LOGIN_EMAIL);
    }

    private Locator loginPasswordInput() {
        return page().locator(Locators.Login.LOGIN_PASSWORD);
    }

    private Locator loginButton() {
        return page().locator(Locators.Login.LOGIN_BUTTON);
    }

    public boolean isNewUserSignupVisible() {
        waitForVisible(newUserSignupHeading());
        return newUserSignupHeading().textContent().toUpperCase().contains(
                Messages.Ui.NEW_USER_SIGNUP);
    }

    public LoginPage enterNameAndEmail(String name, String email) {
        safeFill(signupNameInput(), name);
        safeFill(signupEmailInput(), email);
        return this;
    }

    public SignupPage clickSignup() {
        safeClick(signupButton());
        return SignupPage.getInstance();
    }

    public HomePage login(String email, String password) {
        safeFill(loginEmailInput(), email);
        safeFill(loginPasswordInput(), password);
        safeClick(loginButton());
        return HomePage.getInstance();
    }
}
