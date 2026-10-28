package com.framework.pages;

import com.framework.base.BasePage;
import com.framework.constants.Messages;
import com.framework.constants.Constants.Routes;
import com.framework.constants.Locators;
import com.microsoft.playwright.Locator;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class ProductsPage extends BasePage {

    public static final class Field {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String PRICE = "price";

        private Field() {
        }
    }

    private static final ProductsPage INSTANCE = new ProductsPage();

    private ProductsPage() {
    }

    public static ProductsPage getInstance() {
        return INSTANCE;
    }


    private Locator pageHeading() {
        return page().locator(Locators.Products.PAGE_HEADING);
    }

    private Locator searchInput() {
        return page().locator(Locators.Products.SEARCH_INPUT);
    }

    private Locator searchButton() {
        return page().locator(Locators.Products.SEARCH_BUTTON);
    }

    private Locator productCards() {
        return page().locator(Locators.Products.PRODUCT_CARD);
    }

    public boolean isAllProductsPageVisible() {
        waitForVisible(pageHeading());
        return pageHeading().textContent().toUpperCase().contains(
                Messages.Ui.ALL_PRODUCTS)
                && currentUrl().contains(Routes.PRODUCTS);
    }

    public ProductsPage searchProduct(String productName) {
        safeFill(searchInput(), productName);
        safeClick(searchButton());
        return this;
    }

    public boolean hasSearchedProductsTitle() {
        Locator heading = pageHeading();
        waitForVisible(heading);
        String actual = heading.textContent();
        if (actual == null) {
            return false;
        }
        return actual.replaceAll("\\s+", " ").trim()
                .toUpperCase(Locale.ROOT)
                .equals(Messages.Ui.SEARCHED_PRODUCTS);
    }

    public Map<String, Map<String, String>> collectProducts() {
        waitForVisible(productCards().first());
        Map<String, Map<String, String>> products = new LinkedHashMap<>();
        Locator cards = productCards();
        for (int index = 0; index < cards.count(); index++) {
            Locator card = cards.nth(index);
            String id = card.locator(Locators.Products.CARD_ID).first()
                    .getAttribute("data-product-id");
            if (id == null || id.isBlank()) {
                id = String.valueOf(index);
            }
            Map<String, String> details = new LinkedHashMap<>();
            details.put(Field.ID, id);
            details.put(Field.NAME, textOf(card, Locators.Products.CARD_NAME));
            details.put(Field.PRICE,
                    textOf(card, Locators.Products.CARD_PRICE));
            products.put(id, details);
        }
        return products;
    }

    private String textOf(Locator card, String selector) {
        String text = card.locator(selector).first().textContent();
        return text == null ? "" : text.trim();
    }
}
