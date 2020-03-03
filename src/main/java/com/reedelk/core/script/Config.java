package com.reedelk.core.script;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.configuration.ConfigurationService;

@AutocompleteType(global = true, description =
        "The Config type provides a set of functions to retrieve " +
                "configuration properties given a config key and (optionally) a default value. " +
                "Configuration properties must be defined in the <i>{RUNTIME_HOME}/config/configuration.properties</i> file.")
public class Config {

    private final ConfigurationService configurationService;

    // Protected
    Config(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    // String

    @AutocompleteItem(
            signature = "asString(configKey: string)",
            cursorOffset = 1,
            example = "Config.asString('endpoint.host')",
            description = "Returns the configuration value of the given config key as String type.")
    public String asString(String configKey) {
        return configurationService.getString(configKey);
    }

    @AutocompleteItem(
            signature = "asStringWithDefault(configKey: string, defaultValue: string)",
            cursorOffset = 1,
            example = "Config.asStringWithDefault('endpoint.host', 'localhost')",
            description = "Returns the configuration value of the given config key as a String type if present, " +
                    "otherwise the given default value is returned.")
    public String asStringWithDefault(String configKey, String defaultValue) {
        return configurationService.getString(configKey, defaultValue);
    }

    // Int

    @AutocompleteItem(
            signature = "asInt(configKey: string)",
            cursorOffset = 1,
            example = "Config.asInt('endpoint.port')",
            description = "Returns the configuration value of the given config key as int type.")
    public int asInt(String configKey) {
        return configurationService.getInt(configKey);
    }

    @AutocompleteItem(
            signature = "asIntWithDefault(configKey: string, defaultValue: int)",
            cursorOffset = 1,
            example = "Config.asIntWithDefault('endpoint.port', 8282)",
            description = "Returns the configuration value of the given config key as a int type if present, " +
                    "otherwise the given default value is returned.")
    public int asIntWithDefault(String configKey, int defaultValue) {
        return configurationService.getInt(configKey, defaultValue);
    }

    // Long

    @AutocompleteItem(
            signature = "asLong(configKey: string)",
            cursorOffset = 1,
            example = "Config.asLong('my.config.property')",
            description = "Returns the configuration value of the given config key as long type.")
    public long asLong(String configKey) {
        return configurationService.getLong(configKey);
    }

    @AutocompleteItem(
            signature = "asLongWithDefault(configKey: string, defaultValue: long)",
            cursorOffset = 1,
            example = "Config.asLongWithDefault('my.config.property', 540221)",
            description = "Returns the configuration value of the given config key as a long type if present, " +
                    "otherwise the given default value is returned.")
    public long asLongWithDefault(String configKey, long defaultValue) {
        return configurationService.getLong(configKey, defaultValue);
    }

    // Double

    @AutocompleteItem(
            signature = "asDouble(configKey: string)",
            cursorOffset = 1,
            example = "Config.asDouble('my.config.property')",
            description = "Returns the configuration value of the given config key as double type.")
    public double asDouble(String configKey) {
        return configurationService.getDouble(configKey);
    }

    @AutocompleteItem(
            signature = "asDoubleWithDefault(configKey: string, defaultValue: double)",
            cursorOffset = 1,
            example = "Config.asDoubleWithDefault('my.config.property', 21.7823)",
            description = "Returns the configuration value of the given config key as a double type if present, " +
                    "otherwise the given default value is returned.")
    public double asDoubleWithDefault(String configKey, double defaultValue) {
        return configurationService.getDouble(configKey, defaultValue);
    }

    // Float

    @AutocompleteItem(
            signature = "asFloat(configKey: string)",
            cursorOffset = 1,
            example = "Config.asFloat('my.config.property')",
            description = "Returns the configuration value of the given config key as float type.")
    public float asFloat(String configKey) {
        return configurationService.getFloat(configKey);
    }

    @AutocompleteItem(
            signature = "asFloatWithDefault(configKey: string, defaultValue: float)",
            cursorOffset = 1,
            example = "Config.asFloatWithDefault('my.config.property', 3.1)",
            description = "Returns the configuration value of the given config key as a float type if present, " +
                    "otherwise the given default value is returned.")
    public float asFloatWithDefault(String configKey, float defaultValue) {
        return configurationService.getFloat(configKey, defaultValue);
    }

    // Boolean

    @AutocompleteItem(
            signature = "asBoolean(configKey: string)",
            cursorOffset = 1,
            example = "Config.asBoolean('my.config.property')",
            description = "Returns the configuration value of the given config key as boolean type.")
    public boolean asBoolean(String configKey) {
        return configurationService.getBoolean(configKey);
    }

    @AutocompleteItem(
            signature = "asBooleanWithDefault(configKey: string, defaultValue: boolean)",
            cursorOffset = 1,
            example = "Config.asBooleanWithDefault('my.config.property', true)",
            description = "Returns the configuration value of the given config key as a boolean type if present, " +
                    "otherwise the given default value is returned.")
    public boolean asBooleanWithDefault(String configKey, boolean defaultValue) {
        return configurationService.getBoolean(configKey, defaultValue);
    }

    // Big Decimal and Big Integer are not Javascript types,
    // therefore this service does not wrap those methods.
}
