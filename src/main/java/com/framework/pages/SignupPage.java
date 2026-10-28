package com.framework.pages;

import com.framework.base.BasePage;
import com.framework.constants.Messages;
import com.framework.constants.Constants.GenderTitle;
import com.framework.constants.Locators;
import com.framework.data.AccountData;
import com.microsoft.playwright.Locator;

public class SignupPage extends BasePage {

    private static final SignupPage INSTANCE = new SignupPage();

    private SignupPage() {
    }

    public static SignupPage getInstance() {
        return INSTANCE;
    }


    private Locator accountInfoHeading() {
        return page().locator(Locators.Signup.ACCOUNT_INFO_HEADING).first();
    }

    private Locator titleMr() {
        return page().locator(Locators.Signup.TITLE_MR);
    }

    private Locator titleMrs() {
        return page().locator(Locators.Signup.TITLE_MRS);
    }

    private Locator nameInput() {
        return page().locator(Locators.Signup.NAME);
    }

    private Locator emailInput() {
        return page().locator(Locators.Signup.EMAIL);
    }

    private Locator passwordInput() {
        return page().locator(Locators.Signup.PASSWORD);
    }

    private Locator daysSelect() {
        return page().locator(Locators.Signup.DAYS);
    }

    private Locator monthsSelect() {
        return page().locator(Locators.Signup.MONTHS);
    }

    private Locator yearsSelect() {
        return page().locator(Locators.Signup.YEARS);
    }

    private Locator newsletterCheckbox() {
        return page().locator(Locators.Signup.NEWSLETTER);
    }

    private Locator offersCheckbox() {
        return page().locator(Locators.Signup.OFFERS);
    }

    private Locator firstNameInput() {
        return page().locator(Locators.Signup.FIRST_NAME);
    }

    private Locator lastNameInput() {
        return page().locator(Locators.Signup.LAST_NAME);
    }

    private Locator companyInput() {
        return page().locator(Locators.Signup.COMPANY);
    }

    private Locator address1Input() {
        return page().locator(Locators.Signup.ADDRESS1);
    }

    private Locator address2Input() {
        return page().locator(Locators.Signup.ADDRESS2);
    }

    private Locator countrySelect() {
        return page().locator(Locators.Signup.COUNTRY);
    }

    private Locator stateInput() {
        return page().locator(Locators.Signup.STATE);
    }

    private Locator cityInput() {
        return page().locator(Locators.Signup.CITY);
    }

    private Locator zipcodeInput() {
        return page().locator(Locators.Signup.ZIPCODE);
    }

    private Locator mobileInput() {
        return page().locator(Locators.Signup.MOBILE);
    }

    private Locator createAccountButton() {
        return page().locator(Locators.Signup.CREATE_ACCOUNT);
    }

    public boolean isEnterAccountInformationVisible() {
        waitForVisible(accountInfoHeading());
        String heading = accountInfoHeading().textContent();
        return heading != null && heading.toUpperCase().contains(
                Messages.Ui.ENTER_ACCOUNT_INFORMATION);
    }

    public SignupPage fillTitleNameEmailPasswordAndDateOfBirth(
            AccountData account) {
        waitForVisible(passwordInput());
        if (GenderTitle.MRS.matches(account.title())) {
            titleMrs().check();
        } else {
            titleMr().check();
        }
        fillWhenEnabled(nameInput(), account.name());
        fillWhenEnabled(emailInput(), account.email());
        safeFill(passwordInput(), account.password());
        safeSelect(daysSelect(), account.day());
        safeSelect(monthsSelect(), account.month());
        safeSelect(yearsSelect(), account.year());
        return this;
    }

    public SignupPage selectNewsletter() {
        newsletterCheckbox().check();
        return this;
    }

    public SignupPage selectOffers() {
        offersCheckbox().check();
        return this;
    }

    public SignupPage fillAddressDetails(AccountData account) {
        safeFill(firstNameInput(), account.firstName());
        safeFill(lastNameInput(), account.lastName());
        safeFill(companyInput(), account.company());
        safeFill(address1Input(), account.address1());
        safeFill(address2Input(), account.address2());
        safeSelect(countrySelect(), account.country());
        safeFill(stateInput(), account.state());
        safeFill(cityInput(), account.city());
        safeFill(zipcodeInput(), account.zipcode());
        safeFill(mobileInput(), account.mobileNumber());
        return this;
    }

    private void fillWhenEnabled(Locator locator, String value) {
        if (locator.count() > 0 && locator.isEnabled()) {
            safeFill(locator, value);
        }
    }

    public ConfirmationPage createAccount() {
        safeClick(createAccountButton());
        return ConfirmationPage.getInstance();
    }
}
