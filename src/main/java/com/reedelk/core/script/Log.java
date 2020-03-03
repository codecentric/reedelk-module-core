package com.reedelk.core.script;

import com.reedelk.core.commons.LoggerLevel;
import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@AutocompleteType(global = true, description =
        "The Log type provides a set of functions to log messages " +
                "from scripts for several logging levels.")
public class Log {

    private static final Logger logger = LoggerFactory.getLogger(Log.class);

    // Protected
    Log() {
    }

    @AutocompleteItem(
            signature = "info(message: Object)",
            cursorOffset = 1,
            example = "Log.info('my info log message')",
            description = "Logs a message with INFO logger level.")
    public void info(Object message) {
        LoggerLevel.INFO.log(logger, message);
    }

    @AutocompleteItem(
            signature = "warn(message: Object)",
            cursorOffset = 1,
            example = "Log.warn('my warn log message')",
            description = "Logs a message with WARN logger level.")
    public void warn(Object message) {
        LoggerLevel.WARN.log(logger, message);
    }

    @AutocompleteItem(
            signature = "error(message: Object)",
            cursorOffset = 1,
            example = "Log.error('my error log message')",
            description = "Logs a message with ERROR logger level.")
    public void error(Object message) {
        LoggerLevel.ERROR.log(logger, message);
    }

    @AutocompleteItem(
            signature = "trace(message: Object)",
            cursorOffset = 1,
            example = "Log.trace('my trace log message')",
            description = "Logs a message with TRACE logger level.")
    public void trace(Object message) {
        LoggerLevel.TRACE.log(logger, message);
    }

    @AutocompleteItem(
            signature = "debug(message: Object)",
            cursorOffset = 1,
            example = "Log.debug('my debug log message')",
            description = "Logs a message with DEBUG logger level.")
    public void debug(Object message) {
        if (logger.isDebugEnabled()) {
            LoggerLevel.DEBUG.log(logger, message);
        }
    }
}
