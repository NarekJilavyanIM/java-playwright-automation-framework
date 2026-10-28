package com.framework.constants;

import com.framework.constants.Constants.Country;
import com.framework.constants.Constants.GenderTitle;

public final class Users {

    private Users() {
    }

    /**
     * Values used when generating a unique user for registration.
     */
    public static final class Generated {
        public static final String NAME_PREFIX = "Test User ";
        public static final String EMAIL_PREFIX = "ae.user.";
        public static final String EMAIL_DOMAIN = "@mailsac.com";
        public static final String PASSWORD = "Password123!";
        public static final GenderTitle TITLE = GenderTitle.MR;
        public static final String DAY = "15";
        public static final String MONTH = "6";
        public static final String YEAR = "1990";
        public static final String FIRST_NAME = "Test";
        public static final String LAST_NAME = "User";
        public static final String COMPANY = "Automation Corp";
        public static final String ADDRESS1 = "123 Practice Street";
        public static final String ADDRESS2 = "Suite 9";
        public static final Country COUNTRY = Country.UNITED_STATES;
        public static final String STATE = "California";
        public static final String CITY = "Los Angeles";
        public static final String ZIPCODE = "90001";
        public static final String MOBILE_NUMBER = "5551234567";

        private Generated() {
        }
    }
}
