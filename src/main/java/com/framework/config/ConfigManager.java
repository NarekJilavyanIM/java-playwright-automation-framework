package com.framework.config;

import com.framework.constants.Constants.BrowserName;
import com.framework.constants.Constants.TestData;
import com.framework.constants.Constants.Timeouts;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {

    private static final ConfigManager INSTANCE = new ConfigManager();
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input != null) {
                PROPERTIES.load(input);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load config.properties",
                    exception);
        }
    }

    private ConfigManager() {
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public String getBaseUrl() {
        return resolve("base.url", "BASE_URL", TestData.BASE_URL);
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(resolve("headless", "HEADLESS",
                TestData.HEADLESS_DEFAULT));
    }

    public int getTimeout() {
        return Integer.parseInt(resolve("timeout", "TIMEOUT",
                String.valueOf(Timeouts.DEFAULT_MS)));
    }

    public BrowserName getBrowser() {
        return BrowserName.from(resolve("browser", "BROWSER",
                BrowserName.CHROMIUM.value()));
    }

    private static String resolve(String propertyKey, String envKey,
            String defaultValue) {
        String systemValue = System.getProperty(propertyKey);
        if (isPresent(systemValue)) {
            return systemValue;
        }
        String envValue = System.getenv(envKey);
        if (isPresent(envValue)) {
            return envValue;
        }
        return PROPERTIES.getProperty(propertyKey, defaultValue);
    }

    private static boolean isPresent(String value) {
        return value != null && !value.isBlank();
    }
}
