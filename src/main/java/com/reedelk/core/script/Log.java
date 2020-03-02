package com.reedelk.core.script;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@AutocompleteType(global = true, description =
        "The Log function provides a set of methods to log messages from scripts.")
public class Log {

    private static final Logger logger = LoggerFactory.getLogger(Log.class);

    // Protected
    Log() {
    }

    @AutocompleteItem(
            signature = "info(message: string)",
            cursorOffset = 1,
            example = "Log.info('my info log message')",
            description = "Logs a message with INFO level")
    public void info(String message) {
        logger.info(message);
    }

    @AutocompleteItem(signature = "warn('')", cursorOffset = 2, example = "Log.warn('my warn log message')", description = "Logs a message with WARN level")
    public void warn(String message) {
        logger.warn(message);
    }

    @AutocompleteItem(signature = "error('')", cursorOffset = 2, example = "Log.error('my error log message')", description = "Logs a message with ERROR level")
    public void error(String message) {
        logger.error(message);
    }

    @AutocompleteItem(signature = "trace('')", cursorOffset = 2, example = "Log.trace('my trace log message')", description = "Logs a message with TRACE level")
    public void trace(String message) {
        logger.trace(message);
    }

    @AutocompleteItem(signature = "debug('')", cursorOffset = 2, example = "Log.debug('my debug log message')", description = "Logs a message with DEBUG level")
    public void debug(String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }
}
