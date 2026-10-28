package com.framework.tests;

import com.framework.base.BaseTest;
import com.framework.constants.Messages;
import com.framework.data.AccountData;
import com.framework.pages.ConfirmationPage;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.SignupPage;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Automation Exercise")
@Feature("Registration")
public class RegisterUserTest extends BaseTest {

    @Test
    @Story("Registration")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Test Case 1: Register User")
    @Description(
            "Register a new user, confirm the login, then delete the account.")
    void registerUser() {
        AccountData account = AccountData.uniqueUser();

        HomePage homePage = Allure.step(
                "Navigate to url 'http://automationexercise.com'", () ->
                HomePage.getInstance().open());

        Allure.step("Verify that home page is visible successfully", () ->
                assertTrue(homePage.isHomePageVisible(),
                        Messages.Assertions.HOME_PAGE_VISIBLE));

        LoginPage loginPage = Allure.step("Click on 'Signup / Login' button",
                () ->
                homePage.clickSignupLogin());

        Allure.step("Verify 'New User Signup!' is visible", () ->
                assertTrue(loginPage.isNewUserSignupVisible(),
                        Messages.Assertions.NEW_USER_SIGNUP_VISIBLE));

        Allure.step("Enter name and email address", () ->
                loginPage.enterNameAndEmail(account.name(), account.email()));

        SignupPage signupPage = Allure.step("Click 'Signup' button", () ->
                loginPage.clickSignup());

        Allure.step("Verify that 'ENTER ACCOUNT INFORMATION' is visible", () ->
                assertTrue(signupPage.isEnterAccountInformationVisible(),
                        Messages.Assertions.ENTER_ACCOUNT_INFORMATION_VISIBLE));

        Allure.step(
                "Fill details: Title, Name, Email, Password, Date of birth",
                () -> signupPage.fillTitleNameEmailPasswordAndDateOfBirth(
                        account));

        Allure.step("Select checkbox 'Sign up for our newsletter!'", () ->
                signupPage.selectNewsletter());

        Allure.step(
                "Select checkbox 'Receive special offers from our partners!'",
                () -> signupPage.selectOffers());

        Allure.step(
                "Fill details: First name, Last name, Company, Address, "
                        + "Address2, Country, State, City, Zipcode, "
                        + "Mobile Number",
                () -> signupPage.fillAddressDetails(account));

        ConfirmationPage confirmationPage = Allure.step(
                "Click 'Create Account button'",
                () -> signupPage.createAccount());

        Allure.step("Verify that 'ACCOUNT CREATED!' is visible", () ->
                assertTrue(confirmationPage.isAccountCreatedVisible(),
                        Messages.Assertions.ACCOUNT_CREATED_VISIBLE));

        HomePage loggedInHome = Allure.step("Click 'Continue' button", () ->
                confirmationPage.clickContinue());

        Allure.step("Verify that 'Logged in as username' is visible", () ->
                assertTrue(loggedInHome.isLoggedInAs(account.name()),
                        Messages.Assertions.loggedInAs(account.name())));

        ConfirmationPage deleted = Allure.step(
                "Click 'Delete Account' button",
                () -> loggedInHome.clickDeleteAccount());

        Allure.step(
                "Verify that 'ACCOUNT DELETED!' is visible and click "
                        + "'Continue' button", () -> {
                    assertTrue(deleted.isAccountDeletedVisible(),
                            Messages.Assertions.ACCOUNT_DELETED_VISIBLE);
                    deleted.clickContinue();
                });
    }
}
