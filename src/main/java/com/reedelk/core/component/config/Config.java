package com.reedelk.core.component.config;

import com.reedelk.runtime.api.configuration.ConfigurationService;

public class Config {

    private final ConfigurationService configurationService;

    public Config(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    // String

    public String getString(String configKey) {
        return configurationService.getString(configKey);
    }

    public String getStringWithDefault(String configKey, String defaultValue) {
        return configurationService.getString(configKey, defaultValue);
    }

    // Int

    public int getInt(String configKey) {
        return configurationService.getInt(configKey);
    }

    public int getIntWithDefault(String configKey, int defaultValue) {
        return configurationService.getInt(configKey, defaultValue);
    }

    // Long

    public long getLong(String configKey) {
        return configurationService.getLong(configKey);
    }

    public long getLongWithDefault(String configKey, long defaultValue) {
        return configurationService.getLong(configKey, defaultValue);
    }

    // Double

    public double getDouble(String configKey) {
        return configurationService.getDouble(configKey);
    }

    public double getDoubleWithDefault(String configKey, double defaultValue) {
        return configurationService.getDouble(configKey, defaultValue);
    }

    // Float

    public float getFloat(String configKey) {
        return configurationService.getFloat(configKey);
    }

    public float getFloatWithDefault(String configKey, float defaultValue) {
        return configurationService.getFloat(configKey, defaultValue);
    }

    // Boolean

    public boolean getBoolean(String configKey) {
        return configurationService.getBoolean(configKey);
    }

    public boolean getBooleanWithDefault(String configKey, boolean defaultValue) {
        return configurationService.getBoolean(configKey, defaultValue);
    }

    // Big Decimal and Big Integer are not Javascript types,
    // therefore this service does not wrap those methods.
}
