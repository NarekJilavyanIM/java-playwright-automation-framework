package com.framework.tests;

import com.framework.base.BaseTest;
import com.framework.constants.Constants;
import com.framework.constants.Messages;
import com.framework.pages.HomePage;
import com.framework.pages.ProductsPage;
import com.framework.utils.ProductSearch;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Automation Exercise")
@Feature("Catalog")
public class SearchProductTest extends BaseTest {

    @Test
    @Story("Catalog")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Test Case 9: Search Product")
    @Description(
            "Search the product catalog and verify that results match the"
                    + " search term.")
    void searchProduct() {
        String searchTerm = Constants.TestData.SEARCH_PRODUCT;

        HomePage homePage = Allure.step(
                "Navigate to url 'http://automationexercise.com'", () ->
                HomePage.getInstance().open());

        Allure.step("Verify that home page is visible successfully", () ->
                assertTrue(homePage.isHomePageVisible(),
                        Messages.Assertions.HOME_PAGE_VISIBLE));

        ProductsPage productsPage = Allure.step(
                "Click Products and verify ALL PRODUCTS page", () -> {
                    ProductsPage products = homePage.clickProducts();
                    assertTrue(products.isAllProductsPageVisible(),
                            Messages.Assertions.ALL_PRODUCTS_VISIBLE);
                    return products;
                });

        Allure.step(
                "Search for '" + searchTerm + "' and verify related products",
                () -> {
                    productsPage.searchProduct(searchTerm);
                    assertTrue(productsPage.hasSearchedProductsTitle(),
                            Messages.Assertions.SEARCHED_PRODUCTS_VISIBLE);
                    Map<String, Map<String, String>> displayed =
                            productsPage.collectProducts();
                    List<String> matched = ProductSearch.matchingNames(
                            displayed,
                            Constants.TestData.SEARCH_PRODUCT_OPTIONS);
                    assertFalse(matched.isEmpty(),
                            Messages.Assertions.searchedProductsRelatedTo(
                                    searchTerm));
                    assertTrue(ProductSearch.sameAsMatched(
                                    displayed, matched),
                            Messages.Assertions.displayedProductsMatch(
                                    searchTerm));
                });
    }
}
