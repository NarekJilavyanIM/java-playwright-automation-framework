package com.framework.tests;

import com.framework.base.BaseTest;
import com.framework.constants.Messages;
import com.framework.data.AccountData;
import com.framework.pages.CartPage;
import com.framework.pages.CheckoutPage;
import com.framework.pages.ConfirmationPage;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.PaymentPage;
import com.framework.pages.SignupPage;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Automation Exercise")
@Feature("Checkout")
public class PlaceOrderLoginBeforeCheckoutTest extends BaseTest {

    private AccountData account;

    @BeforeEach
    void registerUser() {
        account = AccountData.uniqueUser();
        HomePage homePage = HomePage.getInstance().open();
        LoginPage loginPage = homePage.clickSignupLogin();
        loginPage.enterNameAndEmail(account.name(), account.email());
        SignupPage signupPage = loginPage.clickSignup();
        signupPage.fillTitleNameEmailPasswordAndDateOfBirth(account);
        signupPage.selectNewsletter();
        signupPage.selectOffers();
        signupPage.fillAddressDetails(account);
        signupPage.createAccount().clickContinue().clickLogout();
    }

    @Test
    @Story("Checkout")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Test Case 16: Place Order: Login before Checkout")
    @Description(
            "Register a user, log in, add a product, checkout, confirm"
                    + " the order, then delete that account.")
    void placeOrderLoginBeforeCheckout() {
        HomePage homePage = Allure.step(
                "Navigate to url 'http://automationexercise.com'", () ->
                HomePage.getInstance().open());

        Allure.step("Verify that home page is visible successfully", () ->
                assertTrue(homePage.isHomePageVisible(),
                        Messages.Assertions.HOME_PAGE_VISIBLE));

        LoginPage loginPage = Allure.step(
                "Click 'Signup / Login' button", () ->
                homePage.clickSignupLogin());

        Allure.step("Fill email, password and click 'Login' button", () ->
                loginPage.login(account.email(), account.password()));

        Allure.step("Verify 'Logged in as username' at top", () ->
                assertTrue(homePage.isLoggedInAs(account.name()),
                        Messages.Assertions.loggedInAs(account.name())));

        Allure.step("Add products to cart", () ->
                homePage.addFirstProductToCart());

        CartPage cartPage = Allure.step("Click 'Cart' button", () ->
                homePage.clickViewCartFromModal());

        Allure.step("Verify that cart page is displayed", () ->
                assertTrue(cartPage.isCartPageDisplayed(),
                        Messages.Assertions.CART_PAGE_VISIBLE));

        CheckoutPage checkoutPage = Allure.step(
                "Click Proceed To Checkout", () ->
                cartPage.proceedToCheckout());

        Allure.step("Verify Address Details and Review Your Order", () ->
                assertTrue(checkoutPage.isAddressAndOrderVisible(),
                        Messages.Assertions.ADDRESS_AND_ORDER_VISIBLE));

        PaymentPage paymentPage = Allure.step(
                "Enter description in comment text area and click"
                        + " 'Place Order'",
                () -> checkoutPage.placeOrder());

        Allure.step(
                "Enter payment details: Name on Card, Card Number, CVC,"
                        + " Expiration date",
                () -> paymentPage.fillPaymentDetails());

        Allure.step("Click 'Pay and Confirm Order' button", () ->
                paymentPage.clickPayAndConfirm());

        Allure.step(
                "Verify success message 'Your order has been placed"
                        + " successfully!'",
                () -> assertTrue(paymentPage.isOrderSuccessful(),
                        Messages.Assertions.ORDER_SUCCESS_VISIBLE));

        ConfirmationPage deleted = Allure.step(
                "Click 'Delete Account' button", () ->
                homePage.clickDeleteAccount());

        Allure.step(
                "Verify 'ACCOUNT DELETED!' and click 'Continue' button",
                () -> {
                    assertTrue(deleted.isAccountDeletedVisible(),
                            Messages.Assertions.ACCOUNT_DELETED_VISIBLE);
                    deleted.clickContinue();
                });
    }
}
