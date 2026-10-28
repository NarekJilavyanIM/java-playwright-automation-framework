package com.framework.constants;

/**
 * Element selectors, grouped by page.
 */
public final class Locators {

    private Locators() {
    }

    public static final class Home {
        public static final String LOGO =
                "img[alt='Website for automation practice']";
        public static final String SLIDER = "#slider-carousel";
        public static final String SIGNUP_LOGIN_LINK = "a[href='/login']";
        public static final String PRODUCTS_LINK = "a[href='/products']";
        public static final String DELETE_ACCOUNT_LINK =
                "a[href='/delete_account']";
        public static final String LOGOUT_LINK = "a[href='/logout']";
        public static final String LOGGED_IN_LINK = "a";
        public static final String SUBSCRIPTION_HEADING =
                "div.single-widget h2";
        public static final String SUBSCRIPTION_WIDGET = "div.single-widget";
        public static final String SCROLL_UP_BUTTON = "#scrollUp";
        public static final String FEATURED_PRODUCT_WRAPPER =
                ".features_items .product-image-wrapper";
        public static final String ADD_TO_CART = ".add-to-cart";
        public static final String CART_MODAL = ".modal-content";
        public static final String VIEW_CART_IN_MODAL =
                ".modal-content a[href='/view_cart']";

        private Home() {
        }
    }

    public static final class Login {
        public static final String SIGNUP_HEADING = ".signup-form h2";
        public static final String SIGNUP_NAME = "input[data-qa='signup-name']";
        public static final String SIGNUP_EMAIL =
                "input[data-qa='signup-email']";
        public static final String SIGNUP_BUTTON =
                "button[data-qa='signup-button']";
        public static final String LOGIN_EMAIL = "input[data-qa='login-email']";
        public static final String LOGIN_PASSWORD =
                "input[data-qa='login-password']";
        public static final String LOGIN_BUTTON =
                "button[data-qa='login-button']";

        private Login() {
        }
    }

    public static final class Signup {
        public static final String ACCOUNT_INFO_HEADING =
                ".login-form h2.title, .login-form b";
        public static final String TITLE_MR = "#id_gender1";
        public static final String TITLE_MRS = "#id_gender2";
        public static final String NAME = "input[data-qa='name']";
        public static final String EMAIL = "input[data-qa='email']";
        public static final String PASSWORD = "input[data-qa='password']";
        public static final String DAYS = "select[data-qa='days']";
        public static final String MONTHS = "select[data-qa='months']";
        public static final String YEARS = "select[data-qa='years']";
        public static final String NEWSLETTER = "#newsletter";
        public static final String OFFERS = "#optin";
        public static final String FIRST_NAME = "input[data-qa='first_name']";
        public static final String LAST_NAME = "input[data-qa='last_name']";
        public static final String COMPANY = "input[data-qa='company']";
        public static final String ADDRESS1 = "input[data-qa='address']";
        public static final String ADDRESS2 = "input[data-qa='address2']";
        public static final String COUNTRY = "select[data-qa='country']";
        public static final String STATE = "input[data-qa='state']";
        public static final String CITY = "input[data-qa='city']";
        public static final String ZIPCODE = "input[data-qa='zipcode']";
        public static final String MOBILE = "input[data-qa='mobile_number']";
        public static final String CREATE_ACCOUNT =
                "button[data-qa='create-account']";

        private Signup() {
        }
    }

    public static final class Confirmation {
        public static final String ACCOUNT_CREATED =
                "h2[data-qa='account-created']";
        public static final String ACCOUNT_DELETED =
                "h2[data-qa='account-deleted']";
        public static final String CONTINUE = "a[data-qa='continue-button']";

        private Confirmation() {
        }
    }

    public static final class Products {
        public static final String PAGE_HEADING = ".features_items h2.title";
        public static final String SEARCH_INPUT = "#search_product";
        public static final String SEARCH_BUTTON = "#submit_search";
        public static final String PRODUCT_CARD =
                ".features_items .product-image-wrapper";
        public static final String CARD_NAME = ".productinfo p";
        public static final String CARD_PRICE = ".productinfo h2";
        public static final String CARD_ID = ".productinfo .add-to-cart";

        private Products() {
        }
    }

    public static final class Cart {
        public static final String CART_INFO = "#cart_info";
        public static final String PROCEED_TO_CHECKOUT = "a.check_out";

        private Cart() {
        }
    }

    public static final class Checkout {
        public static final String DELIVERY_ADDRESS = "#address_delivery";
        public static final String BILLING_ADDRESS = "#address_invoice";
        public static final String REVIEW_ORDER = "#cart_info";
        public static final String COMMENT = "textarea[name='message']";
        public static final String PLACE_ORDER = "a[href='/payment']";

        private Checkout() {
        }
    }

    public static final class Payment {
        public static final String NAME_ON_CARD =
                "input[data-qa='name-on-card']";
        public static final String CARD_NUMBER = "input[data-qa='card-number']";
        public static final String CVC = "input[data-qa='cvc']";
        public static final String EXPIRY_MONTH =
                "input[data-qa='expiry-month']";
        public static final String EXPIRY_YEAR = "input[data-qa='expiry-year']";
        public static final String PAY_BUTTON = "button[data-qa='pay-button']";
        public static final String ORDER_PLACED =
                "h2[data-qa='order-placed'], p";
        public static final String BODY = "body";

        private Payment() {
        }
    }
}
