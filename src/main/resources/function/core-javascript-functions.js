var Util = {};

(function() {

    var system = Java.type('java.lang.System');
    var uuid = Java.type('java.util.UUID');

    this.tmpdir = function() {
        return system.getProperty("java.io.tmpdir");
    };

    this.uuid = function() {
        return uuid.randomUUID().toString();
    };

}).call(Util);

var Log = {};

(function(logger) {

    this.info = function(message) {
        logger.info(message);
    };

    this.debug = function(message) {
        logger.debug(message);
    };

    this.warn = function(message) {
        logger.warn(message);
    };

    this.error = function(message) {
        logger.error(message);
    };

    this.trace = function(message) {
        logger.trace(message);
    };

}).call(Log, logger);

var Config = {};

(function (config) {

    // String

    this.getString = function (configKey) {
        return config.getString(configKey);
    };

    this.getStringWithDefault = function (configKey, defaultValue) {
        return config.getStringWithDefault(configKey, defaultValue);
    };

    // Int

    this.getInt = function (configKey) {
        return config.getInt(configKey);
    };

    this.getIntWithDefault = function (configKey, defaultValue) {
        return config.getIntWithDefault(configKey, defaultValue);
    };

    // Long

    this.getLong = function (configKey) {
        return config.getLong(configKey);
    };

    this.getLongWithDefault = function (configKey, defaultValue) {
        return config.getLongWithDefault(configKey, defaultValue);
    };

    // Double

    this.getDouble = function (configKey) {
        return config.getDouble(configKey);
    };

    this.getDoubleWithDefault = function (configKey, defaultValue) {
        return config.getDoubleWithDefault(configKey, defaultValue);
    };

    // Float

    this.getFloat = function (configKey) {
        return config.getFloat(configKey);
    };

    this.getFloatWithDefault = function (configKey, defaultValue) {
        return config.getFloatWithDefault(configKey, defaultValue);
    };

    // Boolean

    this.getBoolean = function (configKey) {
        return config.getBoolean(configKey);
    };

    this.getBooleanWithDefault = function (configKey, defaultValue) {
        return config.getBooleanWithDefault(configKey, defaultValue);
    };

}).call(Config, config);
