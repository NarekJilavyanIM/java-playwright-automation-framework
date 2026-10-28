package com.framework.pages;

import com.framework.base.BasePage;
import com.framework.constants.Messages;
import com.framework.constants.PaymentData;
import com.framework.constants.Locators;
import com.microsoft.playwright.Locator;

public class PaymentPage extends BasePage {

    private static final PaymentPage INSTANCE = new PaymentPage();

    private PaymentPage() {
    }

    public static PaymentPage getInstance() {
        return INSTANCE;
    }


    private Locator nameOnCard() {
        return page().locator(Locators.Payment.NAME_ON_CARD);
    }

    private Locator cardNumber() {
        return page().locator(Locators.Payment.CARD_NUMBER);
    }

    private Locator cvc() {
        return page().locator(Locators.Payment.CVC);
    }

    private Locator expiryMonth() {
        return page().locator(Locators.Payment.EXPIRY_MONTH);
    }

    private Locator expiryYear() {
        return page().locator(Locators.Payment.EXPIRY_YEAR);
    }

    private Locator payButton() {
        return page().locator(Locators.Payment.PAY_BUTTON);
    }

    private Locator orderPlacedHeading() {
        return page().locator(Locators.Payment.ORDER_PLACED).filter(
                new Locator.FilterOptions().setHasText(
                        Messages.Ui.ORDER_CONFIRMED)
        ).first();
    }

    public PaymentPage fillPaymentDetails() {
        return fillPaymentDetails(
                PaymentData.NAME_ON_CARD,
                PaymentData.CARD_NUMBER,
                PaymentData.CVC,
                PaymentData.EXPIRY_MONTH,
                PaymentData.EXPIRY_YEAR);
    }

    public PaymentPage fillPaymentDetails(String name, String number,
            String cvcCode, String month, String year) {
        waitForVisible(nameOnCard());
        safeFill(nameOnCard(), name);
        safeFill(cardNumber(), number);
        safeFill(cvc(), cvcCode);
        safeFill(expiryMonth(), month);
        safeFill(expiryYear(), year);
        return this;
    }

    public PaymentPage clickPayAndConfirm() {
        safeClick(payButton());
        return this;
    }

    public boolean isOrderSuccessful() {
        waitForVisible(orderPlacedHeading());
        String bodyText = page().locator(Locators.Payment.BODY).textContent();
        return bodyText.contains(Messages.Ui.ORDER_CONFIRMED)
                || bodyText.contains(Messages.Ui.ORDER_PLACED);
    }
}
