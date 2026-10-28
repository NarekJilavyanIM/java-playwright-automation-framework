package com.framework.tests;

import com.framework.base.BaseTest;
import com.framework.constants.Messages;
import com.framework.pages.HomePage;
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
@Feature("Home page")
public class ScrollUpUsingArrowTest extends BaseTest {

    @Test
    @Story("Home page")
    @Severity(SeverityLevel.MINOR)
    @DisplayName(
            "Test Case 25: Verify Scroll Up using Arrow button and Scroll Down"
                    + " functionality")
    @Description(
            "Scroll to the footer, confirm Subscription, then use the arrow"
                    + " button to return to the top.")
    void scrollUpUsingArrow() {
        HomePage homePage = Allure.step(
                "Navigate to url 'http://automationexercise.com'", () ->
                HomePage.getInstance().open());

        Allure.step("Verify that home page is visible successfully", () ->
                assertTrue(homePage.isHomePageVisible(),
                        Messages.Assertions.HOME_PAGE_VISIBLE));

        Allure.step("Scroll to the bottom", () ->
                homePage.scrollToBottom());

        Allure.step("Verify Subscription is visible", () ->
                assertTrue(homePage.isSubscriptionVisible(),
                        Messages.Assertions.SUBSCRIPTION_VISIBLE));

        Allure.step("Click the scroll-up arrow button", () ->
                homePage.clickScrollUpArrow());

        Allure.step("Verify the hero heading is visible", () ->
                assertTrue(homePage.isHeroHeadingVisible(),
                        Messages.Assertions.HERO_HEADING_VISIBLE));
    }
}
