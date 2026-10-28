package com.framework.pages;

import com.framework.base.BasePage;
import com.framework.constants.PaymentData;
import com.framework.constants.Locators;
import com.microsoft.playwright.Locator;

public class CheckoutPage extends BasePage {

    private static final CheckoutPage INSTANCE = new CheckoutPage();

    private CheckoutPage() {
    }

    public static CheckoutPage getInstance() {
        return INSTANCE;
    }


    private Locator deliveryAddress() {
        return page().locator(Locators.Checkout.DELIVERY_ADDRESS);
    }

    private Locator billingAddress() {
        return page().locator(Locators.Checkout.BILLING_ADDRESS);
    }

    private Locator reviewOrder() {
        return page().locator(Locators.Checkout.REVIEW_ORDER);
    }

    private Locator commentBox() {
        return page().locator(Locators.Checkout.COMMENT);
    }

    private Locator placeOrderButton() {
        return page().locator(Locators.Checkout.PLACE_ORDER);
    }

    public boolean isAddressAndOrderVisible() {
        waitForVisible(deliveryAddress());
        return deliveryAddress().isVisible()
                && billingAddress().isVisible()
                && reviewOrder().isVisible();
    }

    public PaymentPage placeOrder() {
        return placeOrder(PaymentData.ORDER_COMMENT);
    }

    public PaymentPage placeOrder(String comment) {
        safeFill(commentBox(), comment);
        safeClick(placeOrderButton());
        return PaymentPage.getInstance();
    }
}
