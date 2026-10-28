package com.framework.constants;

public final class Messages {

    private Messages() {
    }

    public static final class Ui {
        public static final String NEW_USER_SIGNUP = "NEW USER SIGNUP";
        public static final String ENTER_ACCOUNT_INFORMATION =
                "ENTER ACCOUNT INFORMATION";
        public static final String ACCOUNT_CREATED = "ACCOUNT CREATED";
        public static final String ACCOUNT_DELETED = "ACCOUNT DELETED";
        public static final String LOGGED_IN_AS = "Logged in as";
        public static final String ALL_PRODUCTS = "ALL PRODUCTS";
        public static final String SEARCHED_PRODUCTS = "SEARCHED PRODUCTS";
        public static final String SUBSCRIPTION = "Subscription";
        public static final String HERO_HEADING =
                "Full-Fledged practice website for Automation Engineers";
        public static final String ORDER_CONFIRMED =
                "Congratulations! Your order has been confirmed!";
        public static final String ORDER_PLACED =
                "Your order has been placed successfully!";

        private Ui() {
        }
    }

    public static final class Assertions {
        public static final String HOME_PAGE_VISIBLE =
                "Home page should be visible";
        public static final String NEW_USER_SIGNUP_VISIBLE =
                "'New User Signup!' should be visible";
        public static final String ENTER_ACCOUNT_INFORMATION_VISIBLE =
                "'ENTER ACCOUNT INFORMATION' should be visible";
        public static final String ACCOUNT_CREATED_VISIBLE =
                "'ACCOUNT CREATED!' should be visible";
        public static final String ACCOUNT_DELETED_VISIBLE =
                "'ACCOUNT DELETED!' should be visible";
        public static final String ALL_PRODUCTS_VISIBLE =
                "ALL PRODUCTS page should be visible";
        public static final String SEARCHED_PRODUCTS_VISIBLE =
                "'SEARCHED PRODUCTS' should be visible";
        public static final String CART_PAGE_VISIBLE =
                "Cart page should be displayed";
        public static final String ADDRESS_AND_ORDER_VISIBLE =
                "Address details and review order should be visible";
        public static final String ORDER_SUCCESS_VISIBLE =
                "Order confirmation should be visible";
        public static final String SUBSCRIPTION_VISIBLE =
                "'SUBSCRIPTION' should be visible";
        public static final String HERO_HEADING_VISIBLE =
                "'Full-Fledged practice website for Automation Engineers'"
                        + " should be visible after scroll up";

        private Assertions() {
        }

        public static String loggedInAs(String userName) {
            return "Logged in as " + userName + " should be visible";
        }

        public static String searchedProductsRelatedTo(String searchTerm) {
            return "Searched products related to '" + searchTerm
                    + "' should be visible";
        }

        public static String displayedProductsMatch(String searchTerm) {
            return "Displayed products should be exactly the products"
                    + " matching '" + searchTerm + "'";
        }
    }
}
