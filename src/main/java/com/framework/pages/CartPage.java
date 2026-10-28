package com.framework.pages;

import com.framework.base.BasePage;
import com.framework.constants.Constants.Routes;
import com.framework.constants.Locators;
import com.microsoft.playwright.Locator;

public class CartPage extends BasePage {

    private static final CartPage INSTANCE = new CartPage();

    private CartPage() {
    }

    public static CartPage getInstance() {
        return INSTANCE;
    }


    private Locator cartInfo() {
        return page().locator(Locators.Cart.CART_INFO);
    }

    private Locator proceedToCheckoutButton() {
        return page().locator(Locators.Cart.PROCEED_TO_CHECKOUT);
    }

    public boolean isCartPageDisplayed() {
        waitForVisible(cartInfo());
        return currentUrl().contains(
                Routes.VIEW_CART) && cartInfo().isVisible();
    }

    public CheckoutPage proceedToCheckout() {
        safeClick(proceedToCheckoutButton());
        return CheckoutPage.getInstance();
    }
}
