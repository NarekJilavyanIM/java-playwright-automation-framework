package com.framework.utils;

import com.framework.pages.ProductsPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class ProductSearch {

    private ProductSearch() {
    }

    public static List<String> matchingNames(
            Map<String, Map<String, String>> products, String[] options) {
        List<String> names = new ArrayList<>();
        if (products == null || options == null) {
            return names;
        }
        for (Map<String, String> product : products.values()) {
            String name = product.getOrDefault(ProductsPage.Field.NAME, "");
            if (matchesAny(name, options)) {
                names.add(name);
            }
        }
        return names;
    }

    public static boolean sameAsMatched(
            Map<String, Map<String, String>> displayed,
            List<String> matched) {
        return sorted(namesOf(displayed)).equals(sorted(matched));
    }

    private static List<String> sorted(List<String> names) {
        List<String> copy = new ArrayList<>();
        if (names != null) {
            copy.addAll(names);
        }
        copy.sort(String.CASE_INSENSITIVE_ORDER);
        return copy;
    }

    private static List<String> namesOf(
            Map<String, Map<String, String>> products) {
        List<String> names = new ArrayList<>();
        if (products == null) {
            return names;
        }
        for (Map<String, String> product : products.values()) {
            names.add(product.getOrDefault(ProductsPage.Field.NAME, ""));
        }
        return names;
    }

    private static boolean matchesAny(String name, String[] options) {
        String lowered = name.toLowerCase(Locale.ROOT);
        for (String option : options) {
            if (option == null || option.isBlank()) {
                continue;
            }
            if (lowered.contains(option.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }
}
