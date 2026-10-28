package com.framework.pages;

import com.framework.base.BasePage;
import com.framework.constants.Messages;
import com.framework.constants.Locators;
import com.microsoft.playwright.Locator;

public class ConfirmationPage extends BasePage {

    private static final ConfirmationPage INSTANCE = new ConfirmationPage();

    private ConfirmationPage() {
    }

    public static ConfirmationPage getInstance() {
        return INSTANCE;
    }


    private Locator accountCreatedHeading() {
        return page().locator(Locators.Confirmation.ACCOUNT_CREATED);
    }

    private Locator accountDeletedHeading() {
        return page().locator(Locators.Confirmation.ACCOUNT_DELETED);
    }

    private Locator continueButton() {
        return page().locator(Locators.Confirmation.CONTINUE);
    }

    public boolean isAccountCreatedVisible() {
        waitForVisible(accountCreatedHeading());
        return accountCreatedHeading().textContent().toUpperCase().contains(
                Messages.Ui.ACCOUNT_CREATED);
    }

    public boolean isAccountDeletedVisible() {
        waitForVisible(accountDeletedHeading());
        return accountDeletedHeading().textContent().toUpperCase().contains(
                Messages.Ui.ACCOUNT_DELETED);
    }

    public HomePage clickContinue() {
        safeClick(continueButton());
        return HomePage.getInstance();
    }
}
