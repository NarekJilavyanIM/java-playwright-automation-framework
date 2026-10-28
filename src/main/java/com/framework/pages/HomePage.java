package com.framework.pages;

import com.framework.base.BasePage;
import com.framework.config.ConfigManager;
import com.framework.constants.Messages;
import com.framework.constants.Constants.Routes;
import com.framework.constants.Constants.Timeouts;
import com.framework.constants.Locators;
import com.framework.logging.CatchHandler;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage extends BasePage {

    private static final HomePage INSTANCE = new HomePage();

    private HomePage() {
    }

    public static HomePage getInstance() {
        return INSTANCE;
    }


    private Locator logo() {
        return page().locator(Locators.Home.LOGO);
    }

    private Locator slider() {
        return page().locator(Locators.Home.SLIDER);
    }

    private Locator signupLoginLink() {
        return page().locator(Locators.Home.SIGNUP_LOGIN_LINK);
    }

    private Locator productsLink() {
        return page().locator(Locators.Home.PRODUCTS_LINK);
    }

    private Locator deleteAccountLink() {
        return page().locator(Locators.Home.DELETE_ACCOUNT_LINK);
    }

    private Locator logoutLink() {
        return page().locator(Locators.Home.LOGOUT_LINK);
    }

    private Locator loggedInLabel() {
        return page().locator(Locators.Home.LOGGED_IN_LINK).filter(
                new Locator.FilterOptions().setHasText(Messages.Ui.LOGGED_IN_AS)
        );
    }

    private Locator subscriptionHeading() {
        return page().locator(Locators.Home.SUBSCRIPTION_HEADING).filter(
                new Locator.FilterOptions().setHasText(Messages.Ui.SUBSCRIPTION)
        );
    }

    private Locator scrollUpButton() {
        return page().locator(Locators.Home.SCROLL_UP_BUTTON);
    }

    private Locator heroHeading() {
        return page().getByText(Messages.Ui.HERO_HEADING).first();
    }

    public HomePage open() {
        navigateTo(ConfigManager.getInstance().getBaseUrl());
        return this;
    }

    public boolean isHomePageVisible() {
        waitForVisible(logo());
        return logo().isVisible() && slider().isVisible();
    }

    public LoginPage clickSignupLogin() {
        safeClickAndExpectUrl(signupLoginLink(), Routes.LOGIN,
                Routes.absolute(Routes.LOGIN));
        return LoginPage.getInstance();
    }

    public ProductsPage clickProducts() {
        safeClickAndExpectUrl(productsLink(), Routes.PRODUCTS,
                Routes.absolute(Routes.PRODUCTS));
        return ProductsPage.getInstance();
    }

    public HomePage addFirstProductToCart() {
        Locator addToCart = page().locator(
                Locators.Home.FEATURED_PRODUCT_WRAPPER)
                .first()
                .locator(Locators.Home.ADD_TO_CART)
                .first();
        safeClick(addToCart);
        waitForVisible(page().locator(Locators.Home.CART_MODAL));
        return this;
    }

    public CartPage clickViewCartFromModal() {
        safeClick(page().locator(Locators.Home.VIEW_CART_IN_MODAL));
        return CartPage.getInstance();
    }

    public boolean isLoggedInAs(String userName) {
        waitForVisible(loggedInLabel());
        return loggedInLabel().isVisible()
                && loggedInLabel().textContent().contains(userName);
    }

    public ConfirmationPage clickDeleteAccount() {
        safeClickAndExpectUrl(deleteAccountLink(), Routes.DELETE_ACCOUNT,
                Routes.absolute(Routes.DELETE_ACCOUNT));
        return ConfirmationPage.getInstance();
    }

    public HomePage clickLogout() {
        safeClick(logoutLink());
        waitForVisible(signupLoginLink());
        return this;
    }

    public HomePage scrollToBottom() {
        CatchHandler.getInstance().abortIfRequested();
        try {
            page().evaluate("""
                    (selector) => {
                      const footer = document.querySelector(selector);
                      if (footer) {
                        footer.scrollIntoView({
                          block: 'center',
                          behavior: 'instant'
                        });
                      } else {
                        window.scrollTo(
                          0, document.documentElement.scrollHeight);
                      }
                      window.dispatchEvent(new Event('scroll'));
                    }
                    """, Locators.Home.SUBSCRIPTION_WIDGET);
        } catch (RuntimeException exception) {
            CatchHandler.getInstance().logAndAbort("scroll to bottom",
                    exception);
        }
        return this;
    }

    public boolean isSubscriptionVisible() {
        waitForVisible(subscriptionHeading());
        return subscriptionHeading().isVisible();
    }

    public HomePage clickScrollUpArrow() {
        CatchHandler.getInstance().abortIfRequested();
        try {
            page().evaluate("""
                    (selector) => {
                      const button = document.querySelector(selector);
                      if (!button) {
                        return;
                      }
                      button.style.display = 'block';
                      button.click();
                    }
                    """, Locators.Home.SCROLL_UP_BUTTON);
            page().waitForFunction(
                    "threshold => window.scrollY < threshold",
                    Timeouts.SCROLL_TOP_PX,
                    new Page.WaitForFunctionOptions().setTimeout(
                            Timeouts.SCROLL_MS)
            );
        } catch (RuntimeException exception) {
            CatchHandler.getInstance().logAndAbort("click scroll-up arrow",
                    exception);
        }
        return this;
    }

    public boolean isHeroHeadingVisible() {
        waitForVisible(heroHeading());
        return heroHeading().isVisible();
    }
}
