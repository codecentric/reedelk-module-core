package com.reedelk.core.internal.script;

import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.annotation.TypeFunction;
import com.reedelk.runtime.api.configuration.ConfigurationService;

// TODO: Important! Switching to groovy there is no more constraint in having different
//   names for different methods with different arguments. These methods and all the overloads
//      must be made uniform: e.g: asString(String configKey) and asStringWithDefault(String configKey, String defaultValue).
@Type(global = true, description =
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

    @TypeFunction(
            cursorOffset = 1,
            signature = "asString(configKey: string)",
            example = "Config.asString('endpoint.host')",
            description = "Returns the configuration value of the given config key as String type.")
    public String asString(String configKey) {
        return configurationService.getString(configKey);
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "asStringWithDefault(configKey: string, defaultValue: string)",
            example = "Config.asStringWithDefault('endpoint.host', 'localhost')",
            description = "Returns the configuration value of the given config key as a String type if present, " +
                    "otherwise the given default value is returned.")
    public String asStringWithDefault(String configKey, String defaultValue) {
        return configurationService.getString(configKey, defaultValue);
    }

    // Int

    @TypeFunction(
            cursorOffset = 1,
            signature = "asInt(configKey: string)",
            example = "Config.asInt('endpoint.port')",
            description = "Returns the configuration value of the given config key as int type.")
    public int asInt(String configKey) {
        return configurationService.getInt(configKey);
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "asIntWithDefault(configKey: string, defaultValue: int)",
            example = "Config.asIntWithDefault('endpoint.port', 8282)",
            description = "Returns the configuration value of the given config key as a int type if present, " +
                    "otherwise the given default value is returned.")
    public int asIntWithDefault(String configKey, int defaultValue) {
        return configurationService.getInt(configKey, defaultValue);
    }

    // Long

    @TypeFunction(
            cursorOffset = 1,
            signature = "asLong(configKey: string)",
            example = "Config.asLong('my.config.property')",
            description = "Returns the configuration value of the given config key as long type.")
    public long asLong(String configKey) {
        return configurationService.getLong(configKey);
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "asLongWithDefault(configKey: string, defaultValue: long)",
            example = "Config.asLongWithDefault('my.config.property', 540221)",
            description = "Returns the configuration value of the given config key as a long type if present, " +
                    "otherwise the given default value is returned.")
    public long asLongWithDefault(String configKey, long defaultValue) {
        return configurationService.getLong(configKey, defaultValue);
    }

    // Double

    @TypeFunction(
            cursorOffset = 1,
            signature = "asDouble(configKey: string)",
            example = "Config.asDouble('my.config.property')",
            description = "Returns the configuration value of the given config key as double type.")
    public double asDouble(String configKey) {
        return configurationService.getDouble(configKey);
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "asDoubleWithDefault(configKey: string, defaultValue: double)",
            example = "Config.asDoubleWithDefault('my.config.property', 21.7823)",
            description = "Returns the configuration value of the given config key as a double type if present, " +
                    "otherwise the given default value is returned.")
    public double asDoubleWithDefault(String configKey, double defaultValue) {
        return configurationService.getDouble(configKey, defaultValue);
    }

    // Float

    @TypeFunction(
            cursorOffset = 1,
            signature = "asFloat(configKey: string)",
            example = "Config.asFloat('my.config.property')",
            description = "Returns the configuration value of the given config key as float type.")
    public float asFloat(String configKey) {
        return configurationService.getFloat(configKey);
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "asFloatWithDefault(configKey: string, defaultValue: float)",
            example = "Config.asFloatWithDefault('my.config.property', 3.1)",
            description = "Returns the configuration value of the given config key as a float type if present, " +
                    "otherwise the given default value is returned.")
    public float asFloatWithDefault(String configKey, float defaultValue) {
        return configurationService.getFloat(configKey, defaultValue);
    }

    // Boolean

    @TypeFunction(
            cursorOffset = 1,
            signature = "asBoolean(configKey: string)",
            example = "Config.asBoolean('my.config.property')",
            description = "Returns the configuration value of the given config key as boolean type.")
    public boolean asBoolean(String configKey) {
        return configurationService.getBoolean(configKey);
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "asBooleanWithDefault(configKey: string, defaultValue: boolean)",
            example = "Config.asBooleanWithDefault('my.config.property', true)",
            description = "Returns the configuration value of the given config key as a boolean type if present, " +
                    "otherwise the given default value is returned.")
    public boolean asBooleanWithDefault(String configKey, boolean defaultValue) {
        return configurationService.getBoolean(configKey, defaultValue);
    }

    // Big Decimal and Big Integer are not script types,
    // therefore this service does not wrap those methods.
}
