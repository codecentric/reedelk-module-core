package com.reedelk.core.internal.script;

import com.reedelk.core.component.LoggerComponent;
import com.reedelk.core.internal.commons.LoggerLevel;
import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.annotation.TypeFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Type(global = true, description =
        "The Log type provides a set of functions to log messages " +
                "from scripts for several logging levels.")
public class Log {

    private static final Logger logger = LoggerFactory.getLogger(LoggerComponent.class);

    // Protected
    Log() {
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "info(message: Object)",
            example = "Log.info('my info log message')",
            description = "Logs a message with INFO logger level.")
    public void info(Object message) {
        LoggerLevel.INFO.log(logger, message);
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "warn(message: Object)",
            example = "Log.warn('my warn log message')",
            description = "Logs a message with WARN logger level.")
    public void warn(Object message) {
        LoggerLevel.WARN.log(logger, message);
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "error(message: Object)",
            example = "Log.error('my error log message')",
            description = "Logs a message with ERROR logger level.")
    public void error(Object message) {
        LoggerLevel.ERROR.log(logger, message);
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "trace(message: Object)",
            example = "Log.trace('my trace log message')",
            description = "Logs a message with TRACE logger level.")
    public void trace(Object message) {
        LoggerLevel.TRACE.log(logger, message);
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "debug(message: Object)",
            example = "Log.debug('my debug log message')",
            description = "Logs a message with DEBUG logger level.")
    public void debug(Object message) {
        if (logger.isDebugEnabled()) {
            LoggerLevel.DEBUG.log(logger, message);
        }
    }
}
