package com.reedelk.core.script;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.configuration.ConfigurationService;

@AutocompleteType(global = true, description = "Functions to retrieve runtime configuration data.")
public class Config {

    private final ConfigurationService configurationService;

    // Protected
    Config(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    // String

    @AutocompleteItem(replaceValue = "getString('')", cursorOffset = 2, example = "Config.getString('endpoint.host')", description = "Returns a config value from the given key as String type")
    public String getString(String configKey) {
        return configurationService.getString(configKey);
    }

    @AutocompleteItem(replaceValue = "getStringWithDefault('','')", cursorOffset = 5, description = "Returns a config value from the given key as String type if present, otherwise the provided default value is returned.")
    public String getStringWithDefault(String configKey, String defaultValue) {
        return configurationService.getString(configKey, defaultValue);
    }

    // Int

    @AutocompleteItem(replaceValue = "getInt('')", cursorOffset = 2, description = "Returns a config value from the given key as int type")
    public int getInt(String configKey) {
        return configurationService.getInt(configKey);
    }

    @AutocompleteItem(replaceValue = "getIntWithDefault('',5)", cursorOffset = 4, description = "Returns a config value from the given key as int type, otherwise the provided default value is returned")
    public int getIntWithDefault(String configKey, int defaultValue) {
        return configurationService.getInt(configKey, defaultValue);
    }

    // Long

    @AutocompleteItem(replaceValue = "getLong('')", cursorOffset = 2, description = "Returns a config value from the given key as long type")
    public long getLong(String configKey) {
        return configurationService.getLong(configKey);
    }

    @AutocompleteItem(replaceValue = "getLongWithDefault('',5)", cursorOffset = 4, description = "Returns a config value from the given key as long type, otherwise the provided default value is returned")
    public long getLongWithDefault(String configKey, long defaultValue) {
        return configurationService.getLong(configKey, defaultValue);
    }

    // Double

    @AutocompleteItem(replaceValue = "getDouble('')", cursorOffset = 2, description = "Returns a config value from the given key as double type")
    public double getDouble(String configKey) {
        return configurationService.getDouble(configKey);
    }

    @AutocompleteItem(replaceValue = "getDoubleWithDefault('',0.0)", cursorOffset = 6, description = "Returns a config value from the given key as double type, otherwise the provided default value is returned")
    public double getDoubleWithDefault(String configKey, double defaultValue) {
        return configurationService.getDouble(configKey, defaultValue);
    }

    // Float

    @AutocompleteItem(replaceValue = "getFloat('')", cursorOffset = 2, description = "Returns a config value from the given key as float type")
    public float getFloat(String configKey) {
        return configurationService.getFloat(configKey);
    }

    @AutocompleteItem(replaceValue = "getFloatWithDefault('',0.0)", cursorOffset = 6, description = "Returns a config value from the given key as float type, otherwise the provided default value is returned")
    public float getFloatWithDefault(String configKey, float defaultValue) {
        return configurationService.getFloat(configKey, defaultValue);
    }

    // Boolean

    @AutocompleteItem(replaceValue = "getBoolean('')", cursorOffset = 2, description = "Returns a config value from the given key as boolean type")
    public boolean getBoolean(String configKey) {
        return configurationService.getBoolean(configKey);
    }

    @AutocompleteItem(replaceValue = "getBooleanWithDefault('',true)", cursorOffset = 7, description = "Returns a config value from the given key as boolean type, otherwise the provided default value is returned")
    public boolean getBooleanWithDefault(String configKey, boolean defaultValue) {
        return configurationService.getBoolean(configKey, defaultValue);
    }

    // Big Decimal and Big Integer are not Javascript types,
    // therefore this service does not wrap those methods.
}
