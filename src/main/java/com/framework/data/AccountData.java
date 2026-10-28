package com.framework.data;

import com.framework.constants.Users;

/**
 * Registration payload for a newly created Automation Exercise account.
 */
public record AccountData(
        String name,
        String email,
        String password,
        String title,
        String day,
        String month,
        String year,
        String firstName,
        String lastName,
        String company,
        String address1,
        String address2,
        String country,
        String state,
        String city,
        String zipcode,
        String mobileNumber
) {

    public static AccountData uniqueUser() {
        String suffix = String.valueOf(System.currentTimeMillis());
        return new AccountData(
                Users.Generated.NAME_PREFIX + suffix,
                Users.Generated.EMAIL_PREFIX + suffix
                        + Users.Generated.EMAIL_DOMAIN,
                Users.Generated.PASSWORD,
                Users.Generated.TITLE.value(),
                Users.Generated.DAY,
                Users.Generated.MONTH,
                Users.Generated.YEAR,
                Users.Generated.FIRST_NAME,
                Users.Generated.LAST_NAME,
                Users.Generated.COMPANY,
                Users.Generated.ADDRESS1,
                Users.Generated.ADDRESS2,
                Users.Generated.COUNTRY.value(),
                Users.Generated.STATE,
                Users.Generated.CITY,
                Users.Generated.ZIPCODE,
                Users.Generated.MOBILE_NUMBER
        );
    }

}
